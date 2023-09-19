package com.nick.Dynamic.javax;

import com.nick.Dynamic.javax.servlet.NickServletRequest;
import com.nick.Dynamic.javax.servlet.NickServletResponse;

/**
 * @author Nick
 * @Classname Processor
 * @Date 2023/07/21 14:41
 * @Description 资源处理接口
 */
public interface Processor {
	void process(NickServletRequest request, NickServletResponse response);
}
