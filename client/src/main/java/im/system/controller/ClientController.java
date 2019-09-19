package im.system.controller;

import im.system.handler.ClientHandler;
import im.system.model.IMUserInfo;
import im.system.model.MessageInfo;
import im.system.service.IMUserInfoService;
import im.system.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户端控制器
 */
@Controller
@RequestMapping("/client")
@Slf4j
public class ClientController {

    @Resource
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private IMUserInfoService userInfoService;

    @Autowired
    private NioSocketConnector connector;

    @Autowired
    private IoSession ioSession;

    /**
     * 进入聊天室界面
     * @return
     */
    @RequestMapping("/toChatHome")
    public String toChatHome(String userNo,ModelMap modelMap){
        List<IMUserInfo> userInfoList = userInfoService.getUserInfoList();
        IMUserInfo myInfo = userInfoList.stream().filter(u -> u.getUserNo().equals(userNo)).findAny().orElse(null);
        modelMap.put("friendList",userInfoList);
        modelMap.put("myInfo",myInfo);
        return "/chatHome";
    }

    /**
     * 发起聊天
     * @return Object
     */
    @RequestMapping(value = "/toChat",method = RequestMethod.POST)
    @ResponseBody
    public Object toChat(MessageInfo messageInfo){
        Map<String,Object> result = new HashMap<String, Object>();
        log.info("开启会话:{}",messageInfo);
        if(ObjectUtils.isEmpty(messageInfo.getSenderId())||ObjectUtils.isEmpty(messageInfo.getReceiverId())){
            result.put(Constants.ResponseStatus.CODE,Constants.ResponseStatus.FAILED);
            result.put(Constants.ResponseStatus.MESSAGE,"发送人或接收人不能为空");
        }
        Map<Long,IoSession> sessionMap = connector.getManagedSessions();
        IoSession session =null;
        for(Map.Entry<Long,IoSession> entry : sessionMap.entrySet()){
            System.out.println(entry.getValue().getAttribute("userNo"));
        }
        if(!ObjectUtils.isEmpty(sessionMap)){
            Object sessionId = redisTemplate.opsForValue().get(messageInfo.getSenderId());
            if(!ObjectUtils.isEmpty(sessionId)) {
                session = sessionMap.get(sessionId);
            }
        }
        if (!ObjectUtils.isEmpty(session)) {
            session.write(messageInfo);
        }else{
            //保存至缓存
            redisTemplate.opsForValue().set(messageInfo.getSenderId(),ioSession.getId());
            System.out.println(redisTemplate.opsForValue().get(messageInfo.getSenderId()));
            ioSession.write(messageInfo);
            log.info("新建会话:{}",ioSession);
        }
        result.put(Constants.ResponseStatus.CODE,Constants.ResponseStatus.SUCCESS);
        result.put(Constants.ResponseStatus.MESSAGE,"消息发送成功");
        return result;
    }
}
