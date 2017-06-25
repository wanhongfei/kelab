package org.kelab.vf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.kelab.vf.redis.impl.RedisTemplate;

/**
 * @author wwhhf
 * @comment redis 方法监控
 * @since 2016年6月11日
 */
@Slf4j
public class RedisEnableAspect {

	/**
	 * 验证redis是否可用
	 *
	 * @param joinpoint
	 * @return
	 * @throws Throwable
	 */
	public Object checkRedisEnable(ProceedingJoinPoint joinpoint) throws Throwable {
		Object[] args = joinpoint.getArgs();
		Object target = joinpoint.getTarget();
		if (target instanceof RedisTemplate) {
			RedisTemplate redisTemplate = (RedisTemplate) target;
			if (redisTemplate.isEnable()) {
				return joinpoint.proceed(args);
			} else {
				log.error("<===== redis is disable.please check zknode and cfgfile. =====>");
			}
		}
		return joinpoint.proceed(args);
	}

}
