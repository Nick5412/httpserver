package com.nick.HttpServer;


import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
		try (ServerSocket ss = new ServerSocket(port);
		) {
			logger.debug("服务器启动，配置端口为: " + port);
			//todo: 读取server.xml中关于是否开启线程的配置项,决定是否使用线程池
			while (flag) {
				try {
					Socket socket = ss.accept();
					logger.debug("客户端：" + socket.getRemoteSocketAddress() + " 连接上服务器");
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
