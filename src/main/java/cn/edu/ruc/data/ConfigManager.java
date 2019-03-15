package cn.edu.ruc.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
	private Properties properties = null;

	public ConfigManager(String file){
		properties = new Properties();
		try {
			InputStream in = ConfigManager.class.getClassLoader().getResourceAsStream(file);
			properties = new Properties();
			properties.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getValue(String key){
		if (properties.containsKey(key))
			return properties.getProperty(key);
		else
			return "";
	}
}