package im.system.utils;

/**
 * 全局变量
 */
public class Constants {

    /**
     * 返回状态吗
     */
    public static class ResponseStatus{
        public static final String CODE = "code";
        public static final String MESSAGE = "message";
        public static final int SUCCESS = 1002;
        public static final int FAILED = 1005;
    }

    /**
     * redis键前缀
     */
    public static class RedisPrefix{
        public static final String CHAT = "chat:";
    }
}
