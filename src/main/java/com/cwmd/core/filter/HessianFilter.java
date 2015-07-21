package com.cwmd.core.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationContext;
import org.springframework.remoting.caucho.HessianExporter;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cwmd.core.exception.RemotingServiceException;

/**
 * hessian 远程调用过滤器
 * Created by CWMD . 
 * @author: chaoyang.ren  
 * @date:2015年7月21日  
 * @time:上午10:23:16   
 * @email:chaoyang.ren@foxmail.com  
 * @version: 1.0
 */
@Slf4j
public class HessianFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(!"POST".equalsIgnoreCase(request.getMethod())){
			String error = "Hessian Service Util only supports POST requests";
			log.error(error);
			throw new RemotingServiceException(error);
		}
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		HessianExporter hessianExporter = (HessianExporter)applicationContext.getBean("hessianServiceExporter");
		try {
			hessianExporter.invoke(request.getInputStream(), response.getOutputStream());
		} catch (Throwable e) {
			log.error("====远程调用发生异常：",e);
			throw new RemotingServiceException("Hessian skeleton invocation failed",e);
		}
	}
	
}

