package cn.edu.ruc.data;

import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* ConfigManager Tester. 
* 
* @author <Authors name> 
* @since <pre>Feb 8, 2018</pre> 
* @version 1.0 
*/ 
public class ConfigManagerTest { 

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
    *
    * Method: getValue(String key)
    *
    */
    @Test
    public void testGetValue() throws Exception {
        ConfigManager configManager = new ConfigManager("conf.properties");
        System.out.println(configManager.getValue("dir"));
    }


} 
