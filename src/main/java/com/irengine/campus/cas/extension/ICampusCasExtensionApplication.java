package com.irengine.campus.cas.extension;

import javax.servlet.MultipartConfigElement;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ICampusCasExtensionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ICampusCasExtensionApplication.class, args);
	}
	
	/*限制上传文件大小*/
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("4096KB");
        factory.setMaxRequestSize("4096KB");
        return factory.createMultipartConfig();
    }
    
	// run h2 server for development
	// console: http://localhost:8082
	// database: jdbc:h2:mem:testdb
	// user: sa
	// password:
	// <bean id="h2Server" class="org.h2.tools.Server"
	// factory-method="createTcpServer" init-method="start"
	// destroy-method="stop" depends-on="h2WebServer">
	// <constructor-arg value="-tcp,-tcpAllowOthers,-tcpPort,9092"/>
	// </bean>
	// <bean id="h2WebServer" class="org.h2.tools.Server"
	// factory-method="createWebServer" init-method="start"
	// destroy-method="stop">
	// <constructor-arg value="-web,-webAllowOthers,-webPort,8082"/>
	// </bean>
	 /*上传时注视掉*/
	@Bean
	Server h2Server() {
		Server server = new Server();
		try {
			server.runTool("-tcp");
			server.runTool("-tcpAllowOthers");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return server;
	}
}
