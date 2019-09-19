package im.system.handler;

import com.sun.org.apache.xpath.internal.objects.XObject;
import im.system.model.MessageInfo;
import im.system.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 服务器端处理器
 */
@Component
@Slf4j
public class ServerHandler extends IoHandlerAdapter {

    @Autowired
    private SocketAcceptor acceptor;

    @Resource
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        log.info("存储客户端通讯会话:{}",session);
        super.sessionCreated(session);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.info("通信会话连接打开:{}",session);
        super.sessionOpened(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        Object userNo = session.getAttribute("userNo");
        redisTemplate.delete(userNo);
        log.info("客户端:{}会话关闭",session);
        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        log.info("会话：{}空闲",session);
        super.sessionIdle(session, status);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.error("通信异常:{}",session);
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        log.info("收到:{}消息",message);
        MessageInfo messageInfo = (MessageInfo) message;
        IoSession recSession = null;
        Map<Long,IoSession> sessionMap = acceptor.getManagedSessions();
        if(!ObjectUtils.isEmpty(sessionMap)) {
            Object sessionId = redisTemplate.opsForValue().get(messageInfo.getReceiverId());
            if(!ObjectUtils.isEmpty(sessionId)) {
                recSession = sessionMap.get(sessionId);
            }
        }
        if(!ObjectUtils.isEmpty(recSession)){
            log.info("好友:{}在线",messageInfo.getReceiverName());
            recSession.write(messageInfo.getMessageContent());
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        log.info("推送消息:{}至:{}",message,session);
        super.messageSent(session, message);
    }
}
