package org.kelab.vf.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.LogManager;
import org.kelab.util.LogUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by hongfei.whf on 2017/3/30.
 */
@Slf4j
public class Log4jShutdownListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		log.info("Tomcat已启动!");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		log.info("Tomcat已关闭!");
		// log4j内存释放前写入文件中
		LogUtil.flush();
		// 关闭log4j
		LogManager.shutdown();
	}
}
