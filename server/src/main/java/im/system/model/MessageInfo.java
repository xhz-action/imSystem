package im.system.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息实体
 */
@Data
public class MessageInfo implements Serializable {

    private static final long serialVersionUID = 5946793305821453676L;

    /**
     * 发送人id
     */
    private Long senderId;

    /**
     * 发送人姓名
     */
    private String senderName;

    /**
     * 发送人昵称
     */
    private String senderNick;

    /**
     * 接收人id
     */
    private Long receiverId;

    /**
     * 接收人姓名
     */
    private String receiverName;

    /**
     * 接收人昵称
     */
    private String receiverNick;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 消息内容
     */
    private String messageContent;
}

