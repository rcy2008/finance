package com.cwmd;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.stefan.snrpc.SnRpcClient;
import org.stefan.snrpc.SnRpcConnectionFactory;
import org.stefan.snrpc.client.CommonSnRpcClient;
import org.stefan.snrpc.client.PoolableRpcConnectionFactory;
import org.stefan.snrpc.client.SnNettyRpcConnectionFactory;
import org.stefan.snrpc.service.AccountService;
import org.stefan.snrpc.service.UserDetailsService;
import org.stefan.snrpc.service.UserRelationService;

import com.cwmd.core.cfg.JpaConfig;
import com.cwmd.finance.jms.Sender;
import com.cwmd.finance.service.impl.RemoteFinanceServiceImpl;
import com.cwmd.remoting.finance.FinanceService;
import com.cwmd.util.PropUtils;
import com.google.common.collect.Lists;

@Configuration
@EnableScheduling
@ComponentScan(basePackageClasses = ApplicationConfig.class, excludeFilters = @Filter({
        Controller.class, Configuration.class}))
@Import(JpaConfig.class)
@Slf4j
public class ApplicationConfig {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        List<Resource> list = Lists.newArrayList();
        list.add(new ClassPathResource("/persistence.properties"));
        list.add(new ClassPathResource("/system.properties"));
//        list.add(new FileSystemResource(PropUtils.systemProperties.getProperty("sys.security.conf.path")));
        ppc.setLocations(list.stream().toArray(Resource[]::new));
        return ppc;
    }

    @Bean
    public Properties sysProperties() throws IOException {
        return PropertiesLoaderUtils.loadProperties(new ClassPathResource("/system.properties"));
    }

    @Bean
    public SnRpcClient snRpcClient() {
        SnRpcConnectionFactory factory = new PoolableRpcConnectionFactory(new SnNettyRpcConnectionFactory(
                PropUtils.getRpcHost(), PropUtils.getRpcPort()));
        factory = new PoolableRpcConnectionFactory(factory);
        SnRpcClient client = new CommonSnRpcClient(factory);
        return client;
    }
    
    @Bean
    public UserDetailsService userDetailsService(){
    	UserDetailsService userDetailsService = null;
		try {
			userDetailsService = snRpcClient().proxy(UserDetailsService.class);
		} catch (Throwable throwable) {
			log.error("====getting rpc userDetailsService failure====");
		}
		return userDetailsService;
    }
    
    @Bean
    public UserRelationService userRelationService(){
    	UserRelationService userRelationService = null;
    	try {
    		userRelationService = snRpcClient().proxy(UserRelationService.class);
    	} catch (Throwable throwable) {
    		log.error("====getting rpc userRelationService failure====");
    	}
    	return userRelationService;
    }
    
    @Bean
    public AccountService accountService(){
    	AccountService accountService = null;
    	try {
    		accountService = snRpcClient().proxy(AccountService.class);
    	} catch (Throwable throwable) {
    		log.error("====getting rpc accountService failure====");
    	}
    	return accountService;
    }
    
    /*@Bean
    public HessianProxyFactoryBean hessianProxyFactoryBean(){
    	HessianProxyFactoryBean hessianProxyFactoryBean = new HessianProxyFactoryBean();
    	hessianProxyFactoryBean.setServiceUrl(PropUtils.getRMIHost());
    	hessianProxyFactoryBean.setServiceInterface(RemoteFinanceService.class);
    	return hessianProxyFactoryBean;
    }*/
    
    @Bean
    public FinanceService remoteFinanceService(){
    	RemoteFinanceServiceImpl remoteFinanceService = new RemoteFinanceServiceImpl();
    	return remoteFinanceService;
    }
    
    @Bean
    public HessianServiceExporter hessianServiceExporter(){
    	HessianServiceExporter hessianServiceExporter = new HessianServiceExporter();
    	hessianServiceExporter.setService(remoteFinanceService());
    	hessianServiceExporter.setServiceInterface(FinanceService.class);
    	return hessianServiceExporter;
    }
    
    @Bean
    public RedeliveryPolicy redeliveryPolicy(){
    	RedeliveryPolicy rdPolicy = new RedeliveryPolicy();
    	rdPolicy.setUseExponentialBackOff(true);
    	rdPolicy.setBackOffMultiplier(2);
    	rdPolicy.setInitialRedeliveryDelay(1000);
    	rdPolicy.setMaximumRedeliveries(0);
    	return rdPolicy;
    }
    
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(){
    	ActiveMQConnectionFactory amqcf = new ActiveMQConnectionFactory();
    	amqcf.setBrokerURL("tcp://127.0.0.1:61616");
    	amqcf.setRedeliveryPolicy(redeliveryPolicy());
    	amqcf.setUserName("admin");
    	amqcf.setPassword("admin");
    	return amqcf;
    }
    
    @Bean
    public PooledConnectionFactory pooledConnectionFactory(){
    	PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
    	pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory());
    	pooledConnectionFactory.setMaxConnections(100);
    	return pooledConnectionFactory;
    }
    
    @Bean
    public JmsTemplate jmsTemplate(){
    	JmsTemplate jmsTemplate = new JmsTemplate();
    	jmsTemplate.setConnectionFactory(pooledConnectionFactory());
    	return jmsTemplate;
    }
    
    @Bean
    public ActiveMQQueue financeSendQueue(){
    	ActiveMQQueue activeMQQueue = new ActiveMQQueue("PD_SEND_QUEUE_1");
    	return activeMQQueue;
    }
    
    @Bean
    public ActiveMQQueue financeReplyQueue(){
    	ActiveMQQueue activeMQQueue = new ActiveMQQueue("PD_REPLY_QUEUE_1");
    	return activeMQQueue;
    }
    
    @Bean
    public Sender sender(){
    	Sender sender = new Sender();
    	sender.setJmsTemplate(jmsTemplate());
    	sender.setReplyQueue(financeReplyQueue());
    	sender.setSendQueue(financeSendQueue());
    	return sender;
    }
    
    @Bean
    public ActiveMQQueue neoSendQueue(){
    	ActiveMQQueue activeMQQueue = new ActiveMQQueue("NEO_SEND_QUEUE_1");
    	return activeMQQueue;
    }
    
    /*@Bean
    public Consumer consumer(){
    	Consumer consumer = new Consumer(jmsTemplate() ,financeSendQueue());
    	return consumer;
    }*/
    
}
