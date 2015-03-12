package com.irengine.commons;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertyUtility {
	
	private static Properties props;
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtility.class);

	
	static {
		Resource resource = new ClassPathResource("/campus.properties");
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			logger.error("fail to load campus.properties.");
		}
	}
	
	public static String get(String key) {
		return props.getProperty(key);
	}

}
