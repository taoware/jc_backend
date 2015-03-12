package com.irengine.campus.cas.extension;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ICampusCasExtensionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ICampusCasExtensionApplication.class, args);
    }
    
    // run h2 server for development
    // console: http://localhost:8082
    // database: jdbc:h2:mem:testdb
    // user: sa
    // password: 
	//<bean id="h2Server" class="org.h2.tools.Server" factory-method="createTcpServer" init-method="start" destroy-method="stop" depends-on="h2WebServer">
	//	<constructor-arg value="-tcp,-tcpAllowOthers,-tcpPort,9092"/>
	//</bean>
	//<bean id="h2WebServer" class="org.h2.tools.Server" factory-method="createWebServer" init-method="start" destroy-method="stop">
	//    <constructor-arg value="-web,-webAllowOthers,-webPort,8082"/>
	//</bean>
//	@Bean
//    Server h2Server() {
//        Server server = new Server();
//        try {
//            server.runTool("-tcp");
//            server.runTool("-tcpAllowOthers");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return server;
//
//    }
}
