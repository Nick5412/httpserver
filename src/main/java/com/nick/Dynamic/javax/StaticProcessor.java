package com.nick.Dynamic.javax;

import com.nick.Dynamic.javax.servlet.NickServletRequest;
import com.nick.Dynamic.javax.servlet.NickServletResponse;

/**
 * @author Nick
 * @Classname StaticProcessor
 * @Date 2023/07/21 14:43
 * @Description 静态资源处理
 */
public class StaticProcessor implements Processor {
	@Override
	public void process(NickServletRequest request, NickServletResponse response) {
		//不应该依赖于具体实现,而应该依赖于抽象父类
		// ((NickHttpServletResponse)response).send();
		response.send();
	}
}
