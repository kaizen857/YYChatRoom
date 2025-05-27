import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class OptimizedChatServerGUI extends JFrame {
    private JButton startButton;
    private JButton stopButton;
    private JTextArea logArea;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private ThreadPoolExecutor executor;
    private Thread serverThread;
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    // 存储在线用户信息: <用户名, ClientSession>
    public static final ConcurrentHashMap<String, ClientSession> onlineUsers = new ConcurrentHashMap<>();

    // 消息队列和工作线程池
    private final BlockingQueue<ClientMessage> messageQueue = new LinkedBlockingQueue<>();
    private final ExecutorService messageProcessor = Executors.newFixedThreadPool(4);

    public OptimizedChatServerGUI() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("优化版聊天服务器控制台");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 控制面板
        JPanel controlPanel = new JPanel();
        startButton = new JButton("启动服务器");
        stopButton = new JButton("停止服务器");
        stopButton.setEnabled(false);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        add(controlPanel, BorderLayout.NORTH);

        // 日志区域
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);

        // 按钮事件
        startButton.addActionListener(e -> startServer());
        stopButton.addActionListener(e -> stopServer());
    }

    private void startServer() {
        if (isRunning.get()) {
            log("服务器已经在运行中");
            return;
        }

        try {
            // 初始化NIO组件
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(3456));

            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            // 创建线程池
            executor = new ThreadPoolExecutor(
                    4, // 核心线程数 (减少线程数量，因为每个线程可以处理多个连接)
                    8, // 最大线程数
                    60,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(1000),
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );

            // 启动消息处理线程
            startMessageProcessors();

            // 启动服务器主线程
            isRunning.set(true);
            serverThread = new Thread(this::runServer);
            serverThread.start();

            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            log("服务器已启动，监听端口: 3456");
        } catch (IOException e) {
            log("服务器启动失败: " + e.getMessage());
        }
    }

    private void runServer() {
        while (isRunning.get()) {
            try {
                // 等待事件发生
                int readyChannels = selector.select(500);
                if (readyChannels == 0) continue;

                // 处理就绪的通道
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        acceptConnection(key);
                    } else if (key.isReadable()) {
                        readData(key);
                    }
                }
            } catch (IOException e) {
                if (isRunning.get()) {
                    log("服务器处理连接时出错: " + e.getMessage());
                }
            }
        }
    }

    private void acceptConnection(SelectionKey key) throws IOException {
        SocketChannel clientChannel;
        try (ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel()) {
            clientChannel = serverChannel.accept();
        }
        clientChannel.configureBlocking(false);

        // 注册读事件
        ClientSession session = new ClientSession(clientChannel);
        clientChannel.register(selector, SelectionKey.OP_READ, session);

        log("新客户端连接: " + clientChannel.getRemoteAddress());
    }

    private void readData(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ClientSession session = (ClientSession) key.attachment();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead == -1) {
                // 连接关闭
                session.close();
                key.cancel();
                log("客户端断开连接: " + clientChannel.getRemoteAddress());
                return;
            }

            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            session.appendData(bytes);

            // 处理完整消息
            while (session.hasCompleteMessage()) {
                String message = session.getNextMessage();
                messageQueue.put(new ClientMessage(session, message));
            }
        } catch (Exception e) {
            log("读取数据时出错: " + e.getMessage());
            session.close();
            key.cancel();
        }
    }

    private void startMessageProcessors() {
        for (int i = 0; i < 4; i++) {
            messageProcessor.execute(() -> {
                while (isRunning.get()) {
                    try {
                        ClientMessage clientMessage = messageQueue.take();
                        processClientMessage(clientMessage);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    } catch (Exception e) {
                        log("消息处理出错: " + e.getMessage());
                    }
                }
            });
        }
    }

    private void processClientMessage(ClientMessage clientMessage) {
        ClientSession session = clientMessage.session;
        String message = clientMessage.message;

        // 处理逻辑与之前类似，但使用session代替handler
        String[] parts = message.split("\\|");
        if (parts.length < 2) {
            session.sendResponse(CommandType.GENERAL_ACK.getCommandCode() + "|server|invalid_command_format");
            return;
        }

        int commandCode;
        CommandType command;
        try {
            commandCode = Integer.parseInt(parts[0]);
            command = CommandType.fromCode(commandCode);
            if (parts.length - 1 < CommandType.getMinParamCount(command)) {
                session.sendResponse(CommandType.GENERAL_ACK.getCommandCode() + "|server|invalid_param_count");
                return;
            }
            switch (command) {
                case LOGIN_REQUEST: // 用户登录
                    handleLogin(session, parts);
                    break;
                case FRIEND_REQUEST: // 发送新好友请求
                    handleFriendRequest(session, parts);
                    break;
                case QUERY_ONLINE_FRIENDS: // 查询所有在线好友
                    handleOnlineFriendsQuery(session, parts);
                    break;
                case LOGOUT: // 用户登出
                    handleLogout(session, parts);
                    break;
                case SEND_MESSAGE: // 发送消息给好友
                    handleSendMessage(session, parts);
                    break;
                case REQUEST_CHAT_HISTORY: // 请求聊天记录
                    handleChatHistoryRequest(session, parts);
                    break;
                case RESPOND_FRIEND_REQUEST: // 回应好友请求
                    handleFriendRequestResponse(session, parts);
                    break;
                default:
                    session.sendResponse("8|server|unsupported_command");
                    break;
            }
        } catch (NumberFormatException e) {
            session.sendResponse("8|server|invalid_command");
        }
    }

    private void handleLogin(ClientSession session, String[] parts) {
        if (parts.length < 3) {
            session.sendResponse("8|server|invalid_login_format");
            return;
        }

        String userName = parts[1];
        String password = parts[2];

        // TODO: 验证用户名和密码
        boolean loginSuccess = DBUtil.loginValidate(userName, password);

        if (loginSuccess) {
            session.setUserName(userName);
            onlineUsers.put(userName, session);
            session.sendResponse("2|" + userName + "|success");
            log("用户登录成功: " + userName);
        } else {
            session.sendResponse("2|" + userName + "|failure");
        }
    }

    private void handleFriendRequest(ClientSession session, String[] parts) {
        if (parts.length < 3) {
            session.sendResponse("8|server|invalid_friend_request_format");
            return;
        }

        String userName = parts[1];
        String friendName = parts[2];

        // TODO: 检查好友是否存在
        boolean friendExists = DBUtil.hasUser(friendName);

        if (friendExists) {
            // 检查好友是否在线
            ClientSession friendSession = onlineUsers.get(friendName);
            if (friendSession != null) {
                // 转发好友请求
                friendSession.sendResponse("3|" + userName + "|" + friendName);
                session.sendResponse("8|server|friend_request_sent");
            } else {
                session.sendResponse("8|server|friend_offline");
            }
        } else {
            session.sendResponse("8|server|friend_not_exist");
        }
    }

    private void handleOnlineFriendsQuery(ClientSession session, String[] parts) {
        if (parts.length < 2) {
            session.sendResponse("8|server|invalid_query_format");
            return;
        }

        String userName = parts[1];

        // TODO: 从数据库获取用户好友列表
        List<String> allFriends = Arrays.asList("friend1", "friend2", "friend3"); // 模拟好友列表

        // 筛选在线好友
        StringBuilder onlineFriends = new StringBuilder();
        for (String friend : allFriends) {
            if (onlineUsers.containsKey(friend)) {
                if (onlineFriends.length() > 0) {
                    onlineFriends.append("|");
                }
                onlineFriends.append(friend);
            }
        }

        session.sendResponse("6|" + userName + "|" + onlineFriends);
    }

    private void handleLogout(ClientSession session, String[] parts) {
        if (parts.length < 2) {
            session.sendResponse("8|server|invalid_logout_format");
            return;
        }

        String userName = parts[1];
        onlineUsers.remove(userName);
        session.sendResponse("8|server|logout_success");
        session.close();
        log("用户登出: " + userName);
    }

    private void handleSendMessage(ClientSession session, String[] parts) {
        if (parts.length < 5) {
            session.sendResponse("8|server|invalid_message_format");
            return;
        }

        String sender = parts[1];
        String receiver = parts[2];
        String content = parts[3];
        String time = parts[4];

        // TODO: 存储消息到数据库

        // 检查接收者是否在线
        ClientSession receiverSession = onlineUsers.get(receiver);
        if (receiverSession != null) {
            // 转发消息
            receiverSession.sendResponse("7|" + sender + "|" + receiver + "|" + content + "|" + time);
            session.sendResponse("8|server|message_sent");
        } else {
            session.sendResponse("8|server|receiver_offline");
        }
    }

    private void handleChatHistoryRequest(ClientSession session, String[] parts) {
        if (parts.length < 4) {
            session.sendResponse("8|server|invalid_history_request_format");
            return;
        }

        String userName = parts[1];
        String friendName = parts[2];
        String lastTime = parts[3];

        // TODO: 从数据库获取聊天记录
        List<String> chatHistory = Arrays.asList(
                "message1|sender1|receiver1|content1|time1",
                "message2|sender2|receiver2|content2|time2"
        ); // 模拟聊天记录

        // 发送聊天记录
        for (String record : chatHistory) {
            session.sendResponse("7|" + record);
        }
        session.sendResponse("8|server|history_sent");
    }

    private void handleFriendRequestResponse(ClientSession session, String[] parts) {
        if (parts.length < 4) {
            session.sendResponse("8|server|invalid_friend_response_format");
            return;
        }

        String userName = parts[1];
        String friendName = parts[2];
        String status = parts[3];

        // TODO: 更新好友关系状态

        // 通知请求发起者
        ClientSession requester = onlineUsers.get(userName);
        if (requester != null) {
            // 这里简化处理，实际应该发送更完整的信息
            requester.sendResponse("4|" + friendName + "|" + userName + "|" + status + "|default_icon");
        }
    }

    private void stopServer() {
        if (!isRunning.get()) {
            log("服务器未运行");
            return;
        }

        isRunning.set(false);

        try {
            // 关闭服务器通道
            if (serverChannel != null) {
                serverChannel.close();
            }

            // 关闭选择器
            if (selector != null) {
                selector.close();
            }

            // 关闭线程池
            if (executor != null) {
                executor.shutdownNow();
            }

            // 关闭消息处理器
            messageProcessor.shutdownNow();

            // 关闭所有客户端连接
            for (ClientSession session : onlineUsers.values()) {
                session.close();
            }
            onlineUsers.clear();

            log("服务器已停止");
        } catch (Exception e) {
            log("停止服务器时出错: " + e.getMessage());
        } finally {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }

    public void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OptimizedChatServerGUI server = new OptimizedChatServerGUI();
            server.setVisible(true);
        });
    }

    // 内部类，表示客户端消息
    private static class ClientMessage {
        final ClientSession session;
        final String message;

        ClientMessage(ClientSession session, String message) {
            this.session = session;
            this.message = message;
        }
    }
}