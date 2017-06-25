package org.kelab.vf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.kelab.vf.controller.BaseController;

/**
 * 清除Threadlocal绑定的request和response
 * 防止内存泄漏
 */
@Slf4j
public class ThreadLocalCleanAspect {

	/**
	 * 清除Threadlocal绑定的request和response
	 *
	 * @param joinpoint
	 * @return
	 * @throws Throwable
	 */
	public Object clean(ProceedingJoinPoint joinpoint) throws Throwable {
		Object[] args = joinpoint.getArgs();
		BaseController controller = (BaseController) joinpoint.getTarget();
		Object result = joinpoint.proceed(args);
		// 请求返回时，清除Threadlocal绑定的request和response
		controller.threadLocalClean();
		return result;
	}

}
