package com.nick.Dynamic.javax.servlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nick
 * @Classname NickServletContext
 * @Date 2023/07/21 14:19
 * @Description 应用程序上下文类, 常量类
 */


public class NickServletContext {
	/**
	 * Map<String,Class>
	 * Map<url地址,servlet的字节码路径>
	 */
	public static Map<String, Class> servletClass = new ConcurrentHashMap<>();
	/**
	 * 每个servlet都是单例,当第一次访问这个servlet时,创建后保存到这个map中
	 */
	public static Map<String, NickServlet> servletInstance = new ConcurrentHashMap<>();
}
