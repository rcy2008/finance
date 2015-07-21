package com.cwmd.core.cfg;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;

@Configuration
@ComponentScan(basePackages = "com.cwmd", includeFilters = @Filter(Controller.class), useDefaultFilters = false)

public class MvcConfig extends AbstractMvcConfig {

    @Override
    protected String resourceContext() {
        return CfgConstants.SERVLET_NAME;
    }

    @Bean(name = CfgConstants.SERVLET_NAME + "ViewResolver")
    public ViewResolver jspResolver() {
        return super.jspResolver();
    }

    @Bean
    public MessageSource messageSource() {
        return super.messageSource();
    }

    /**
     * Handles favicon.ico requests assuring no <code>404 Not Found</code> error is returned.
     */
    @Controller
    static class FaviconController {
        @RequestMapping("favicon.ico")
        String favicon() {
            return "forward:/resources/images/favicon.ico";
        }
    }

    @Override
    protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers){

        super.configureHandlerExceptionResolvers(exceptionResolvers);
    }


}
