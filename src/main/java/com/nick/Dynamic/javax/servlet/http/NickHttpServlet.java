package com.nick.Dynamic.javax.servlet.http;

import com.nick.Dynamic.javax.servlet.NickServlet;
import com.nick.Dynamic.javax.servlet.NickServletRequest;
import com.nick.Dynamic.javax.servlet.NickServletResponse;


/**
 * @author Nick
 * @Classname NickHttpServlet
 * @Date 2023/07/20 20:16
 * @Description TODO
 */
public abstract class NickHttpServlet implements NickServlet {
	@Override
	public void init() {

	}

	@Override
	public void destroy() {

	}

	protected void doPost(NickHttpServletRequest request, NickHttpServletResponse response) {

	}

	protected void doGet(NickHttpServletRequest request, NickHttpServletResponse response) {

	}

	protected void doHead(NickHttpServletRequest request, NickHttpServletResponse response) {

	}

	protected void doDelete(NickHttpServletRequest request, NickHttpServletResponse response) {

	}

	protected void doTrace(NickHttpServletRequest request, NickHttpServletResponse response) {

	}

	protected void doOption(NickHttpServletRequest request, NickHttpServletResponse response) {

	}

	/**
	 * 模板设计模式: 规范httpservlet各方法调用顺序
	 *
	 * @param request
	 * @param response
	 */
	@Override
	public void service(NickServletRequest request, NickServletResponse response) {
		String method = ((NickHttpServletRequest) request).getMethod();
		if ("post".equalsIgnoreCase(method)) {
			doPost((NickHttpServletRequest) request, (NickHttpServletResponse) response);
		} else if ("get".equalsIgnoreCase(method)) {
			doGet((NickHttpServletRequest) request, (NickHttpServletResponse) response);
		} else if ("head".equalsIgnoreCase(method)) {
			doHead((NickHttpServletRequest) request, (NickHttpServletResponse) response);
		} else if ("delete".equalsIgnoreCase(method)) {
			doDelete((NickHttpServletRequest) request, (NickHttpServletResponse) response);
		} else if ("trace".equalsIgnoreCase(method)) {
			doTrace((NickHttpServletRequest) request, (NickHttpServletResponse) response);
		} else if ("option".equalsIgnoreCase(method)) {
			doOption((NickHttpServletRequest) request, (NickHttpServletResponse) response);
		} else {
			// TODO:错误响应
		}
	}

	//重载
	public void service(NickHttpServletRequest request,NickHttpServletResponse response){
		service(request,response);
	}
}
