package com.nick.Dynamic.javax;


import com.nick.Dynamic.javax.servlet.NickServletContext;
import com.nick.Dynamic.javax.servlet.NickWebServlet;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

/**
 * @author Nick
 * @Classname TomcatServer
 * @Date 2023/07/19 21:09
 * @Description TODO
 */
public class TomcatServer {
	static Logger logger = Logger.getLogger(TomcatServer.class);

	public static void main(String[] args) {
		logger.info("程序启动...");
		TomcatServer ts = new TomcatServer();
		int port = ts.parsePortFromXMl();
		try {
			startServer(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void startServer(int port) throws IOException {
		boolean flag = true;

		String packageName = "com.nick";
		String packagePath = packageName.replaceAll("\\.", "/");
		//服务器启动时扫描它所有的classes，使用注解@NickWebServlet()时,存到map中
		// jvm类加载器
		Enumeration<URL> files = Thread.currentThread().getContextClassLoader().getResources(packagePath);
		while (files.hasMoreElements()) {
			URL url = files.nextElement();
			// file:/D:/IntelliJ%20IDEA%202020.3.4/code/HttpServer/target/classes/com/nick
			// System.out.println("url = " + url);
			logger.info("正在扫描的包路径为: " + url.getFile());
			//	查找此包下的文件
			// url.getFile() => /D:/IntelliJ%20IDEA%202020.3.4/code/HttpServer/target/classes/com/nick
			findPackageClasses(url.getFile(), packageName);
		}

		try (ServerSocket ss = new ServerSocket(port);
		) {
			logger.debug("服务器启动，配置端口为: " + port);
			//todo: 读取server.xml中关于是否开启线程的配置项,决定是否使用线程池
			while (flag) {
				try {
					Socket socket = ss.accept();
					logger.debug("客户端: " + socket.getRemoteSocketAddress() + " 连接上服务器");
					//连接服务器启动线程
					TaskService task = new TaskService(socket);
					Thread t = new Thread(task);
					t.start();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("客户端连接失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("服务器套接字创建失败");
		}

	}

	/**
	 * @param packagePath com/nick
	 * @param packageName com.nick
	 */
	private static void findPackageClasses(String packagePath, String packageName) {
		boolean isSlash = packagePath.startsWith("/");
		if (isSlash) {
			packagePath = packagePath.substring(1);
		}
		//路径是否含空格
		boolean contains = packagePath.contains("%20");
		if (contains) {
			packagePath = packagePath.replaceAll("%20", " ");
		}
		//取这个路径下的所有字节码文件和目录
		File file = new File(packagePath);
		File[] classFiles = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith(".class") || pathname.isDirectory()) {
					return true;
				}
				return false;
			}
		});
		// System.out.println(classFiles);
		if (classFiles != null && classFiles.length > 0) {
			for (File cf : classFiles) {
				if (cf.isDirectory()) {
					//该文件是 -> 递归查找此子目录
					findPackageClasses(cf.getAbsolutePath(), packageName + "." + cf.getName());
				} else {
					//	是字节码文件,用类加载器加载此文件
					URLClassLoader ucl = new URLClassLoader(new URL[]{});
					try {
						Class<?> cls = ucl.loadClass(packageName + "." + cf.getName().replaceAll(".class", ""));
						// System.out.println("cls = " + cls);
						if (cls.isAnnotationPresent(NickWebServlet.class)) {
							// logger.info("加载了一个类: " + cls.getName());
							//	通过注解的value()方法取出url地址存到NickServletContext.servletClass这个map中
							NickWebServlet annotation = cls.getAnnotation(NickWebServlet.class);
							String value = annotation.value();
							// value => /hello
							NickServletContext.servletClass.put(value, cls);
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private int parsePortFromXMl() {
		int port = 8080;
		String serverXmlPath = System.getProperty("user.dir") + File.separator + "conf" + File.separator + "server.xml";
		try (FileInputStream in = new FileInputStream(serverXmlPath)) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(in);
			NodeList list = document.getElementsByTagName("Connector");
			for (int i = 0; i < list.getLength(); i++) {
				Element node = (Element) list.item(i);
				port = Integer.parseInt(node.getAttribute("port"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return port;
	}
}
