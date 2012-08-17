package com.ngt.jopenmetaverse.shared.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JLogger {

	static Logger logger = Logger.getLogger("ApplicationRoot");
	
	public static void debug(String msg)
	{
//		logger.log(Level.ALL, msg);
//		System.out.println(msg);
		logger.log(Level.INFO, msg);		
	}	
	
	public static void info(String msg)
	{
		logger.log(Level.INFO, msg);		
	}

	public static void warn(String msg)
	{
		logger.log(Level.WARNING, msg);
	}
	
	public static void error(String msg)
	{
		logger.log(Level.SEVERE, msg);
	}
	
	public static void setLevel(Level newLevel)
	{
		logger.setLevel(newLevel);
	}
	
	public static Logger getLogger(String logger)
	{
		return Logger.getLogger(logger);
	}
	
	public static void addHandler(Handler handler)
	{
		logger.addHandler(handler);
	}

	public static Handler[] getHandlers()
	{
		return logger.getHandlers();
	}
	
	public static void deleteHandler(Handler[] handlerList)
	{
		for(Handler h: handlerList)
		logger.removeHandler(h);
	}
	
	
}
