package com.paymybuddy.webapp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix="com.paymybuddy.api")
public class CustomProperties {

	private String apiUrl;
	
}
