package im;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "im.system.dao")
public class IMApplication {

    public static void main(String[] args) {
        SpringApplication.run(IMApplication.class,args);
    }
}
