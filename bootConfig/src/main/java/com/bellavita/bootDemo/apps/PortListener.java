package com.bellavita.bootDemo.apps;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PortListener implements ApplicationListener<ServletWebServerInitializedEvent> {
	
	private int port = 0;

	@Override
	public void onApplicationEvent(ServletWebServerInitializedEvent event) {
		port = event.getWebServer().getPort();
		System.out.println(" >>>>> " + getPort());		
	}
	
	public int getPort() {
		return port;
	}

}
