package im.system.config;

import im.system.handler.ClientHandler;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class MinaConfig {

    @Value("${im.serverPath}")
    private String serverPath;

    @Value("${im.port}")
    private int port;

    @Bean
    public NioSocketConnector nioSocketConnector(){
        return new NioSocketConnector();
    }

    @Bean
    public IoSession ioSession(){
        NioSocketConnector connector = nioSocketConnector();
        DefaultIoFilterChainBuilder chain = connector.getFilterChain();
        ProtocolCodecFilter codecFilter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
        chain.addLast("objectFilter",codecFilter);
        connector.setHandler(new ClientHandler());
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,60);
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(serverPath,port));
        connectFuture.awaitUninterruptibly();
        IoSession ioSession = connectFuture.getSession();
        return ioSession;
    }
}
