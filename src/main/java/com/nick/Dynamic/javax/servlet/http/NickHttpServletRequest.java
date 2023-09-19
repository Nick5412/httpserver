package com.nick.Dynamic.javax.servlet.http;

import com.nick.Dynamic.javax.servlet.NickServletRequest;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nick
 * @Classname NickHttpServletRequest
 * @Date 2023/07/20 10:05
 * @Description 从输入流中取出http请求, 解析相应信息, 保存
 */
public class NickHttpServletRequest implements NickServletRequest {
	private Logger logger = Logger.getLogger(NickHttpServletRequest.class);
	private Socket socket;
	private InputStream in;
	private String method;
	//定位符
	private String requestURL;
	//标识符
	private String requestURI;
	//上下文
	private String contextPath;
	//请求字符串
	private String queryString;
	//地址栏参数,包括表单提交
	private Map<String, String[]> parameterMap = new ConcurrentHashMap<>();
	//协议类型
	private String scheme;
	//协议版本
	private String protocol;
	//项目真实路径
	private String realPath;

	public NickHttpServletRequest(Socket socket, InputStream in) {
		this.socket = socket;
		this.in = in;
		this.parseRequest();
	}

	private void parseRequest() {
		// 从输入流中读取请求信息
		String requestInfoString = readFromInputStream();
		if (requestInfoString == null || requestInfoString.trim().equals("")) {
			throw new RuntimeException("读取输入流异常...,请求信息为: " + requestInfoString);
		}
		//	解析http请求
		parseRequestInfoString(requestInfoString);
	}

	private void parseRequestInfoString(String requestInfoString) {
		StringTokenizer tokenizer = new StringTokenizer(requestInfoString);
		//请求方法
		this.method = tokenizer.nextToken();
		//统一资源标识符
		this.requestURI = tokenizer.nextToken();
		// int index = this.requestURI.indexOf('?');
		// this.requestURI = this.requestURI.substring(index);
		int questionIndex = this.requestURI.lastIndexOf('?');
		if (questionIndex >= 0) {
			this.queryString = this.requestURI.substring(questionIndex + 1);
			this.requestURI = this.requestURI.substring(0, questionIndex);
		}
		//	协议版本 HTTP/1.1
		this.protocol = tokenizer.nextToken();
		// HTTP
		this.scheme = this.protocol.substring(0, this.protocol.indexOf('/'));
		//	上下文路径
		//	1.   /项目名/index.html
		//	2.   /
		int slash2Index = this.requestURI.indexOf('/', 1);
		if (slash2Index >= 0) {
			this.contextPath = this.requestURI.substring(0, slash2Index);
		} else {
			this.contextPath = this.requestURI;
		}
		//	统一资源定位符
		this.requestURL = this.scheme + "://" + this.socket.getLocalSocketAddress() + this.requestURI;
		//	查询字符串的切割
		if (this.queryString != null && this.queryString.length() > 0) {
			String[] ps = this.queryString.split("&");
			for (String s : ps) {
				String[] params = s.split("=");
				//1. username=zhangsan
				//2. hobby=basketball,soccer
				this.parameterMap.put(params[0], params[1].split(","));
			}
		}
		//	真实路径
		this.realPath = System.getProperty("user.dir") + File.separator + "webapps";
	}

	private String readFromInputStream() {
		byte[] bytes = new byte[300 * 1024];
		int length = -1;
		StringBuffer sb = null;
		try {
			length = this.in.read(bytes, 0, bytes.length);
			sb = new StringBuffer();
			for (int i = 0; i < length; i++) {
				sb.append((char) bytes[i]);
			}
		} catch (Exception e) {
			logger.error("读取请求信息失败...");
			e.printStackTrace();
		}
		return sb.toString();
	}

	public String getMethod() {
		return method;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getQueryString() {
		return queryString;
	}

	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	public String getScheme() {
		return scheme;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getRealPath() {
		return realPath;
	}

	public String getParameter(String name) {
		String[] values = getParameterValues(name);
		values = this.parameterMap.get(name);
		if (values == null || values.length <= 0) {
			return null;
		}
		return values[0];
	}

	public String[] getParameterValues(String name) {
		if (parameterMap == null || parameterMap.size() <= 0) {
			return null;
		}
		String[] values = this.parameterMap.get(name);
		if (values == null || values.length <= 0) {
			return null;
		}
		return values;
	}
}
