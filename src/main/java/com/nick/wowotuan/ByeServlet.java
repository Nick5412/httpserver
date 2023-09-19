package com.nick.wowotuan;

import com.nick.Dynamic.javax.servlet.NickWebServlet;
import com.nick.Dynamic.javax.servlet.http.NickHttpServlet;
import com.nick.Dynamic.javax.servlet.http.NickHttpServletRequest;
import com.nick.Dynamic.javax.servlet.http.NickHttpServletResponse;

/**
 * @author Nick
 * @Classname ByeServlet
 * @Date 2023/07/21 14:32
 * @Description TODO
 */
@NickWebServlet("/bye")
public class ByeServlet extends NickHttpServlet {
	public ByeServlet() {
		System.out.println("ByeServlet 构造方法");
	}

	@Override
	public void init() {
		System.out.println("ByeServlet init方法");
	}

	@Override
	protected void doGet(NickHttpServletRequest request, NickHttpServletResponse response) {
		System.out.println("ByeServlet doGet方法");
	}
}
