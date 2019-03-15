/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogManager {
    private Log logger;

    public LogManager() {
        logger = LogFactory.getLog(LogManager.class);
    }

    public void appendInfo(String s) {
        logger.info(s);
    }
}
