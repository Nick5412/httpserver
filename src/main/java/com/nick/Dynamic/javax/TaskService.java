package com.nick.Dynamic.javax;

import com.nick.Dynamic.javax.servlet.NickServletContext;
import com.nick.Dynamic.javax.servlet.http.NickHttpServletRequest;
import com.nick.Dynamic.javax.servlet.http.NickHttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Nick
 * @Classname TaskService
 * @Date 2023/07/20 9:46
 * @Description TODO
 */
public class TaskService implements Runnable {

	private Logger logger = Logger.getLogger(TaskService.class);
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private boolean flag = true;

	public TaskService(Socket socket) {
		this.socket = socket;
		try {
			this.in = this.socket.getInputStream();
			this.out = this.socket.getOutputStream();
		} catch (IOException e) {
			flag = false;
			logger.error("socket获取流异常...");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if (this.flag) {
			// NickHttpServletRequest request = new NickHttpServletRequest(this.socket, this.in);
			// NickHttpServletResponse response = new NickHttpServletResponse(request, this.out);
			// response.send();

			//解析http请求
			NickHttpServletRequest request = new NickHttpServletRequest(this.socket, this.in);
			NickHttpServletResponse response = new NickHttpServletResponse(request, this.out);
			//	根据request中的URI判断是动态还是静态资源
			int length = request.getContextPath().length();
			//查询是 /hello 还是 index.html
			String uri = request.getRequestURI().substring(length);
			Processor processor = null;
			boolean isDynamicRequest = NickServletContext.servletClass.containsKey(uri);
			if (isDynamicRequest) {
				processor = new DynamicProcessor();
			} else {
				processor = new StaticProcessor();
			}
			processor.process(request, response);
		}
		try {
			this.in.close();
			this.out.close();
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 通过socket的InputStream 读取客户请求，解析
		// 处理请求资源
		// 返回响应
		// 结束
	}
}
