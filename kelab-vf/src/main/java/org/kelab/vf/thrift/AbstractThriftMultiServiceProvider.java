package org.kelab.vf.thrift;

import lombok.SneakyThrows;
import org.apache.thrift.TProcessor;
import org.kelab.util.model.Pair;

import java.lang.reflect.Constructor;

/**
 * Created by hongfei.whf on 2017/3/29.
 */
public abstract class AbstractThriftMultiServiceProvider {

	/**
	 * 初始化函数
	 */
	public abstract void init();

	/**
	 * 关闭服务
	 */
	public abstract void destory();

	/**
	 * 根据serviceImplName获取对应的processor
	 *
	 * @param serviceImplInstance
	 * @return
	 */
	@SneakyThrows
	protected Pair<String/*serviceName*/, TProcessor> serviceName2Processor(
			Object serviceImplInstance) {
		Class serviceImplClass = serviceImplInstance.getClass();
		Class ifaceClasses[] = serviceImplClass.getInterfaces();
		for (Class ifaceClass : ifaceClasses) {
			if (ifaceClass.getName().endsWith("$Iface")) {
				Class serviceClass = ifaceClass.getEnclosingClass();
				Class innerClasses[] = serviceClass.getDeclaredClasses();
				for (Class innerClass : innerClasses) {
					if (innerClass.getName().endsWith("$Processor")) {
						Constructor constructor = innerClass.getConstructor(ifaceClass);
						TProcessor processor = (TProcessor) constructor.newInstance(serviceImplInstance);
						return new Pair<>(serviceClass.getCanonicalName(), processor);
					}
				}
			}
		}
		return null;
	}

}
