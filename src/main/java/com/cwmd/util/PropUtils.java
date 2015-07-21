package com.cwmd.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;

public class PropUtils {
    public static Properties systemProperties;
    static {
        try {
            systemProperties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/system.properties"));
        } catch (IOException e) {
        }
    }

    public static int getRpcPort(){
        String port = systemProperties.getProperty("rpc.port");

        if(StringUtils.isEmpty(port)){
            return 8888;
        }
        return new Integer(port).intValue();
    }

    public static String getRpcHost(){
        return systemProperties.getProperty("rpc.host");
    }
    
    public static String getRMIHost(){
    	return systemProperties.getProperty("rmi.host");
    }
    
    public static String getRMIService(){
    	return systemProperties.getProperty("rmi.host");
    }

}
