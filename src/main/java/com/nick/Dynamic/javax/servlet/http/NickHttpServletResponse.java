package com.nick.Dynamic.javax.servlet.http;

import com.nick.Dynamic.javax.servlet.NickServletResponse;

import java.io.*;

/**
 * @author Nick
 * @Classname NickHttpServletResponse
 * @Date 2023/07/20 15:03
 * @Description TODO
 */
public class NickHttpServletResponse implements NickServletResponse {
	private NickHttpServletRequest request;
	private OutputStream out;

	public NickHttpServletResponse(NickHttpServletRequest request, OutputStream out) {
		this.request = request;
		this.out = out;
	}

	@Override
	public void send() {
		String realPath = this.request.getRealPath();
		String uri = this.request.getRequestURI();
		File file = new File(realPath, uri);
		byte[] fileContent = null;
		String responseProtocal = null;
		//资源不存在
		if (!file.exists()) {
			//读取404页面
			fileContent = readFile(new File(realPath + "/404.html"));
			//获取输出协议
			responseProtocal = gen404(fileContent);
		} else {
			//读取资源页面
			fileContent = readFile(new File(realPath, uri));
			//获取输出协议
			responseProtocal = gen200(fileContent);
		}
		try {
			//输出响应头域
			out.write(responseProtocal.getBytes());
			out.flush();
			//输出响应实体
			out.write(fileContent);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					this.out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public OutputStream getOutputStream() {
		return this.out;
	}

	@Override
	public PrintStream getPrintWriter() {
		return new PrintStream(this.out);
	}

	private String gen200(byte[] fileContent) {
		String protocal200 = "HTTP/1.1 200 OK\r\n";
		//资源类型判断
		String uri = this.request.getRequestURI();
		int index = uri.lastIndexOf('.');
		if (index >= 0) {
			index += 1;
		}
		String fileExtension = uri.substring(index);
		// 策略模式 读取server.xml中的文件类型配置
		if ("jpg".equalsIgnoreCase(fileExtension)) {
			protocal200 += "Content-Type: image/jpeg; charset=utf-8\r\n";
		} else if ("png".equalsIgnoreCase(fileExtension)) {
			protocal200 += "Content-Type: image/png; charset=utf-8\r\n";
		} else if ("gif".equalsIgnoreCase(fileExtension)) {
			protocal200 += "Content-Type: image/gif; charset=utf-8\r\n";
		} else if ("css".equalsIgnoreCase(fileExtension)) {
			protocal200 += "Content-Type: text/css; charset=utf-8\r\n";
		} else if ("js".equalsIgnoreCase(fileExtension)) {
			protocal200 += "Content-Type: application/javascript; charset=utf-8\r\n";
		} else {
			protocal200 += "Content-Type: text/html; charset=utf-8\r\n";
		}
		protocal200 += "Content-Length: " + fileContent.length + "\r\n\r\n";
		return protocal200;
	}

	private String gen404(byte[] fileContent) {
		String protocal404 = "HTTP/1.1 404 Not Found\r\n";
		protocal404 += "Content-Type: text/html; charset=utf-8\r\n";
		protocal404 += "Content-Length: " + fileContent.length + "\r\n";
		protocal404 += "Server: kitty server\r\n\r\n";
		return protocal404;
	}

	private byte[] readFile(File file) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] bytes = new byte[100 * 1024];
			int length = -1;
			while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
				baos.write(bytes, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return baos.toByteArray();
	}
}
