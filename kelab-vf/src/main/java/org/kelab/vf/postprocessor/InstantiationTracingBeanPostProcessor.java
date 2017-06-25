package org.kelab.vf.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.kelab.util.ConsoleUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Created by wanhongfei on 2016/12/26.
 */
@Slf4j
public class InstantiationTracingBeanPostProcessor implements BeanPostProcessor {

	// simply return the instantiated bean as-is
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean; // we could potentially return any object reference here...
	}

	//在创建bean后输出bean的信息
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		log.info("Bean '{}' created:'{}'", beanName + bean.toString());
		ConsoleUtil.println("==>" + bean.toString());
		return bean;
	}
}