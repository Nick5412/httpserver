版本一: Web服务器是一个基于HTTP协议的服务器软件。它主要用于接收来自Web浏览器的请求，并向浏览器发送HTML和其他文件。
    Web服务器主要的职责是处理从浏览器发送的请求，处理Web页面、图像和其他文件的静态资源，并将它们返回给请求的浏览器.

版本二:　应用服务器与Web服务器类似，但它不仅仅处理HTML页面和图像文件，而是执行应用程序代码。这个应用程序可以是基于Java、Python或其他语言的Web应用程序。
通常，应用服务器能够与数据库连接并处理并发请求。应用服务器从客户端传递的请求通常是动态的，与Web服务器不同


Web服务器：　ServerSocket+Socket
while( true){
   Socket s=ss.accept();
   Task t=new Task( s );
   Thread t=new Thread( t);
   t.start();
}

class Task implements Runnable{
        	public void run(){
		//解析发送过来的http的请求     index.html, a.jpg, a.css, a.js,
		//1. 以流的方式读取http的请求, 得到请求的字符串
		//2. 对字符串完成截取.     StringTokenizer     nextToken()
		//3. ***从 StringTokenizer中取出各种值 如下:
		//4.  ****用户路径(指的是你的项目tomcat的路径)  System.getProperty("user.dir")+ File.separator +"webapps" + 根据要访问的资源: /wowotuan/index.html
		//5. 以输入流从磁盘读取文件
		//6. ***拼接响应
	}
}



http响应:
HTTP/1.1 200 OK
Content-Type: text/html; charset=utf-8
Content-Length: 响应实体的字节数
空行
响应的内容

http://localhost:8080/wowotuan/index.html?uname=nick&pwd=1234
******http请求: 格式
GET /wowotuan/index.html?uname=nick&pwd=1234 HTTP/1.1
host: localhost
User-Agent: Mozillaxxxxxxxx
xxx: xxx
yyyy: yyy
空行


method:         GET
requestURI:    /wowotuan/index.html?uname=nick&pwd=1234
queryString:  ?uname=nick&pwd=1234
protocol:      HTTP/1.1
scheme:        http
contextPath:   /wowotuan

parameterMap  Map<String,String[]>     "uname"-> nick    "pwd"->1234

关于System.getProperty( "环境变量" )
