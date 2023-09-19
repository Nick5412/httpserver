package com.nick.Dynamic.javax.servlet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nick
 * @Classname NickWebServlet
 * @Date 2023/07/20 19:47
 * @Description TODO
 */
// Type表示这个注解只能放在类，接口....
@Target({ElementType.TYPE})
// Runtime表示这个注解在运行时还有.
@Retention(RetentionPolicy.RUNTIME)
public @interface NickWebServlet {
	String value() default "";
}
