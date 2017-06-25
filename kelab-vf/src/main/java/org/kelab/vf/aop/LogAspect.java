package org.kelab.vf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.kelab.util.BeanUtil;
import org.kelab.vf.constant.BaseRetCodeConstant;
import org.kelab.vf.exception.BusinessException;
import org.kelab.vf.model.JsonAndModel;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author wwhhf
 * @comment 缓存切面管理 order值越小，顺序越高
 * @since 2016年6月1日
 */
@Slf4j
public class LogAspect {

	/**
	 * service和Dao的日志记录，一切异常往controller层抛出，由controller捕获
	 *
	 * @param ex
	 * @throws Throwable
	 */
	public void serviceAndDaoAfterThrowing(Throwable ex) throws Throwable {
		throw new BusinessException(ex);
	}

	/**
	 * controller层异常捕获，记录日志
	 * 在顶层必须捕获异常
	 *
	 * @param joinPoint
	 * @return
	 */
	public Object controllerAfterThrowing(ProceedingJoinPoint joinPoint) {
		Class clazz = joinPoint.getTarget().getClass();
		String clazzName = clazz.getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		Object result = null;
		try {
			result = joinPoint.proceed();
			return result;
		} catch (Throwable ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			log.error("**************************************************************");
			log.error("method info: " + BeanUtil.methodInfo(clazzName, methodName, args));
			log.error("Exception class: " + ex.getClass().getName());
			log.error("Exception msg:" + sw.toString());
			log.error("**************************************************************");
			return JsonAndModel.builder(BaseRetCodeConstant.ERROR).build();
		}
	}

}
