package com.nick.wowotuan;

import com.nick.Dynamic.javax.servlet.NickWebServlet;
import com.nick.Dynamic.javax.servlet.http.NickHttpServlet;
import com.nick.Dynamic.javax.servlet.http.NickHttpServletRequest;
import com.nick.Dynamic.javax.servlet.http.NickHttpServletResponse;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author Nick
 * @Classname HelloServlet
 * @Date 2023/07/20 20:55
 * @Description TODO
 */
@NickWebServlet("/hello")
public class HelloServlet extends NickHttpServlet {
	public HelloServlet() {
		System.out.println("HelloServlet 构造方法");
	}

	@Override
	public void init() {
		System.out.println("HelloServlet init方法");
	}

	@Override
	protected void doGet(NickHttpServletRequest request, NickHttpServletResponse response) {
		System.out.println("HelloServlet doGet方法\r\n");
		String info = "Hello World!";
		info += "终于手写Tomcat成功了...\n";
		String result = "HTTP/1.1 200 OK\r\n";
		result += "Content-Type: text/html; charset=utf-8\r\n";
		result += "Content-Length: " + info.getBytes().length + "\r\n";
		result += "Server: kitty server\r\n\r\n";
		PrintStream pw = response.getPrintWriter();
		pw.println(result);
		pw.println(info);
		pw.flush();
		// pw.close();
	}
}
