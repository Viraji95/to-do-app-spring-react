package lk.ijse.dep11.app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan
@EnableWebMvc
@Configuration
@PropertySource("classpath:/application.properties")
public class WebAppConfig {
}
