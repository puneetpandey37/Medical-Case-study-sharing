package com.elyx.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ElyxPropertiesUtil {
	private static boolean isLoaded;
	InputStream commonInputStream = getClass().getClassLoader()
			.getResourceAsStream("elyx_common.properties");
	InputStream gcmInputStream = getClass().getClassLoader()
			.getResourceAsStream("elyx_gcm.properties");
	Properties prop = new Properties();

	public void loadElyxProperty() {
		try {
			prop.load(commonInputStream);
			prop.load(gcmInputStream);
			isLoaded = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getElyxProperty(String key) {
		String propertyValue = null;
		loadElyxProperty();
		propertyValue = prop.getProperty(key);
		return propertyValue;
	}

}
