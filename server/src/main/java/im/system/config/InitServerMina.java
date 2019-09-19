package im.system.config;

import im.system.handler.ServerHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * 初始化服务端Mina
 */
@Configuration
@Slf4j
public class InitServerMina implements CommandLineRunner {

    @Value("${im.port}")
    private int port;

    @Autowired
    private ServerHandler serverHandler;

    @Override
    public void run(String... args) throws Exception {
        SocketAcceptor acceptor = acceptor();
        DefaultIoFilterChainBuilder chainBuilder = acceptor.getFilterChain();
        ProtocolCodecFilter filter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
        chainBuilder.addLast("objectFilter",filter);
        acceptor.setHandler(serverHandler);
        acceptor.bind(new InetSocketAddress(port));
        log.info("初始化通讯服务器完成!");
    }

    @Bean
    public SocketAcceptor acceptor(){
        SocketAcceptor acceptor = new NioSocketAcceptor();
        return acceptor;
    }

}
