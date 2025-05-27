import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ClientSession {
    private final SocketChannel channel;
    private String userName;
    private final List<Byte> messageBuffer = new ArrayList<>();

    public ClientSession(SocketChannel channel) {
        this.channel = channel;
    }

    public void appendData(byte[] data) {
        for (byte b : data) {
            messageBuffer.add(b);
        }
    }

    public boolean hasCompleteMessage() {
        for (int i = 0; i < messageBuffer.size(); i++) {
            if (messageBuffer.get(i) == '\n') {
                return true;
            }
        }
        return false;
    }

    public String getNextMessage() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < messageBuffer.size()) {
            byte b = messageBuffer.get(i);
            if (b == '\n') {
                messageBuffer.subList(0, i + 1).clear();
                return sb.toString();
            }
            sb.append((char) b);
            i++;
        }
        return "";
    }

    public void sendResponse(String response) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap((response + "\n").getBytes(StandardCharsets.UTF_8));
            channel.write(buffer);
        } catch (IOException e) {
            close();
        }
    }

    public void close() {
        try {
            if (userName != null) {
                OptimizedChatServerGUI.onlineUsers.remove(userName);
            }
            channel.close();
        } catch (IOException e) {
            // 忽略关闭异常
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SocketChannel getChannel() {
        return channel;
    }
}