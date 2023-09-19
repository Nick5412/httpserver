package com.nick.Dynamic.javax;

import com.nick.Dynamic.javax.servlet.NickServlet;
import com.nick.Dynamic.javax.servlet.NickServletContext;
import com.nick.Dynamic.javax.servlet.NickServletRequest;
import com.nick.Dynamic.javax.servlet.NickServletResponse;
import com.nick.Dynamic.javax.servlet.http.NickHttpServletRequest;

import java.io.PrintStream;

/**
 * @author Nick
 * @Classname DynamicProcessor
 * @Date 2023/07/21 14:44
 * @Description 动态资源处理
 */
public class DynamicProcessor implements Processor {
	@Override
	public void process(NickServletRequest request, NickServletResponse response) {
		// request中的参数已经解析好了,
		// 1.从request中取出RequestURI /hello,到ServletContext的map中去取class
		int length = ((NickHttpServletRequest) request).getContextPath().length();
		String uri = ((NickHttpServletRequest) request).getRequestURI().substring(length);
		NickServlet servlet = null;
		try {
			// 2．为了保证单例，先看另一个map中是否已经有这个class的实例,
			// a.如有,说明是第二次访问,则直接取,再调用 service()
			if (NickServletContext.servletInstance.containsKey(uri)) {
				servlet = NickServletContext.servletInstance.get(uri);
			} else {
				// b.如果没有·则说明此servlet是第一次调用
				// 先利用反射创建servlet(调用servlet的无参构造方法),存到另一个map再调用init()->service
				Class cls = NickServletContext.servletClass.get(uri);
				Object obj = cls.newInstance();
				if (obj instanceof NickServlet) {
					servlet = (NickServlet) obj;
					servlet.init();
					NickServletContext.servletInstance.put(uri, servlet);
				}
			}
			// 此servlet就是你要访问的servlet
			servlet.service(request, response);
		} catch (Exception e) {
			// 还要考虑servlet执行失败的情况,则输出500错误响应给客户端。
			String bodyEntity = e.toString();
			String protocal = gen500(bodyEntity);
			//	以输出流返回到客户端
			PrintStream pw = response.getPrintWriter();
			pw.println(protocal);
			pw.println(bodyEntity);
			pw.flush();
		}

	}

	private String gen500(String bodyEntity) {
		String protocal500 = "HTTP/1.1 500 Internal Server Error\r\n";
		protocal500 += "Content-Type: text/html; charset=utf-8\r\n";
		protocal500 += "Content-Length: " + bodyEntity.getBytes().length + "\r\n\r\n";
		return protocal500;
	}
}
