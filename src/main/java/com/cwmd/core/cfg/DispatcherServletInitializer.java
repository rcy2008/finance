package com.cwmd.core.cfg;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.support.MultipartFilter;

import com.cwmd.ApplicationConfig;
import com.cwmd.core.filter.HessianFilter;

public class DispatcherServletInitializer extends AbstractAnnotationServletInitializer {


    @Override
    protected String getServletName(){
        return CfgConstants.SERVLET_NAME;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{String.format("/%s/*", CfgConstants.SERVLET_NAME)};
    }

    @Override
    protected Class<?>[] getServletConfigClasses(){
        return new Class<?>[]{MvcConfig.class};
    }

    @Override
    protected Class<?>[] getRootConfigClasses(){

        return new Class<?>[]{ApplicationConfig.class, JpaConfig.class};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        MultipartFilter multipartFilter = new MultipartFilter();
        servletContext.addFilter("characterEncodingFilter",characterEncodingFilter).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
        servletContext.addFilter("multipartFilter",multipartFilter).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
        servletContext.addFilter("openEntityManagerInViewFilter",new OpenEntityManagerInViewFilter()).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
        servletContext.addFilter("hessianFilter",new HessianFilter()).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/remoting/*");
        super.onStartup(servletContext);
    }

    @Override
    protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
        return super.registerServletFilter(servletContext, filter);
    }
}