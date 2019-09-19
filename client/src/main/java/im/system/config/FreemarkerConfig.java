package im.system.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;

/**
 * freemarker配置
 */
@Configuration
public class FreemarkerConfig implements InitializingBean, ServletContextAware {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public void afterPropertiesSet() throws Exception {
        freemarker.template.Configuration configuration = freeMarkerConfigurer.getConfiguration();
        configuration.setSharedVariable("ctx",contextPath);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
