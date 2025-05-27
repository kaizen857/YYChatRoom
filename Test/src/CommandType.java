/**
 * 业务命令枚举，定义所有支持的聊天业务命令
 */
public enum CommandType {
    // 用户登录请求 (1|userName|password)
    LOGIN_REQUEST(1),

    // 用户登录请求ACK (2|userName|status)
    LOGIN_RESPONSE(2),

    // 发送新好友请求 (3|userName|friendName)
    FRIEND_REQUEST(3),

    // 发送新好友请求ACK (4|userName|friendName|status|ImageIcon)
    FRIEND_REQUEST_RESPONSE(4),

    // 查询所有在线的好友 (5|userName)
    QUERY_ONLINE_FRIENDS(5),

    // 查询所有在线的好友ACK (6|userName|onlineFriend)
    QUERY_ONLINE_FRIENDS_RESPONSE(6),

    // 登出 (-1|userName)
    LOGOUT(-1),

    // 发送消息 (7|userName|friendName|content|time)
    SEND_MESSAGE(7),

    // 通用ACK信号 (8|userName|ack)
    GENERAL_ACK(8),

    // 请求聊天记录 (9|userName|friendName|lastTime)
    REQUEST_CHAT_HISTORY(9),

    // 回应好友请求 (10|userName|friendName|status)
    RESPOND_FRIEND_REQUEST(10);

    private final int commandCode;

    CommandType(int commandCode) {
        this.commandCode = commandCode;
    }

    public int getCommandCode() {
        return commandCode;
    }

    /**
     * 根据命令码获取对应的枚举值
     * @param code 命令码
     * @return 对应的CommandType枚举
     * @throws IllegalArgumentException 如果找不到对应的命令码
     */
    public static CommandType fromCode(int code) {
        for (CommandType cmd : values()) {
            if (cmd.commandCode == code) {
                return cmd;
            }
        }
        throw new IllegalArgumentException("未知的命令码: " + code);
    }

    /**
     * 检查命令码是否有效
     * @param code 要检查的命令码
     * @return 如果有效返回true，否则返回false
     */
    public static boolean isValidCommand(int code) {
        for (CommandType cmd : values()) {
            if (cmd.commandCode == code) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取命令的最小参数数量(不包括命令码本身)
     * @param command 业务命令
     * @return 该命令所需的最小参数数量
     */
    public static int getMinParamCount(CommandType command) {
        switch (command) {
            case LOGIN_REQUEST:
            case LOGIN_RESPONSE:
            case FRIEND_REQUEST:
            case QUERY_ONLINE_FRIENDS:
            case LOGOUT:
                return 2;
            case FRIEND_REQUEST_RESPONSE:
            case SEND_MESSAGE:
            case REQUEST_CHAT_HISTORY:
            case RESPOND_FRIEND_REQUEST:
                return 3;
            case QUERY_ONLINE_FRIENDS_RESPONSE:
            case GENERAL_ACK:
                return 2; // 实际可能更多，但至少需要2个
            default:
                return 1; // 默认至少1个参数(用户名)
        }
    }
}