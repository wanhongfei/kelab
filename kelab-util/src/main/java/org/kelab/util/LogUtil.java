package org.kelab.util;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hongfei.whf on 2017/3/30.
 */
public class LogUtil {

	/**
	 * 从所有的容器中读取出所有的logger 设置成true 之后再设置成flase。
	 * 设置为true 需要再接着info 一下，只设置为true 是不能触发flush.
	 * 只有再接着打条日志，才能触发
	 */
	public static void flush() {
		Set<FileAppender> flushedFileAppenders = new HashSet<FileAppender>();
		Enumeration currentLoggers = LogManager.getLoggerRepository().getCurrentLoggers();
		while (currentLoggers.hasMoreElements()) {
			Object nextLogger = currentLoggers.nextElement();
			if (nextLogger instanceof Logger) {
				Logger currentLogger = (Logger) nextLogger;
				Enumeration allAppenders = currentLogger.getAllAppenders();
				while (allAppenders.hasMoreElements()) {
					Object nextElement = allAppenders.nextElement();
					if (nextElement instanceof DailyRollingFileAppender) {
						DailyRollingFileAppender fileAppender = (DailyRollingFileAppender) nextElement;
						ConsoleUtil.println(fileAppender.getName());
						if (!flushedFileAppenders.contains(fileAppender)
								&& !fileAppender.getImmediateFlush()) {
							flushedFileAppenders.add(fileAppender);
							fileAppender.setImmediateFlush(true);
							currentLogger.info("====> FLUSH <====");
							fileAppender.setImmediateFlush(false);
						}
					}
				}
			}
		}
	}

}
