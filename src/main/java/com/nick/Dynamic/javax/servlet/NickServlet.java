package com.nick.Dynamic.javax.servlet;

/**
 * @author Nick
 * @Classname NickServlet
 * @Date 2023/07/20 20:12
 * @Description TODO
 */
public interface NickServlet {
	//小型服务程序接口
	//构造方法后调用一次
	void init();
	void destroy();
	//每次请求都会调用
	void service(NickServletRequest request,NickServletResponse response);

}
