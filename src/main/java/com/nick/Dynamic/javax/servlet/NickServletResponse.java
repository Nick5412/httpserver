package com.nick.Dynamic.javax.servlet;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author Nick
 * @Classname NickServletResponse
 * @Date 2023/07/20 20:25
 * @Description TODO
 */
public interface NickServletResponse {
	void send();

	OutputStream getOutputStream();

	PrintStream getPrintWriter();
}
