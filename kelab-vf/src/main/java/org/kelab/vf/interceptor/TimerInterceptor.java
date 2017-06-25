package org.kelab.vf.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.kelab.util.ConsoleUtil;
import org.kelab.util.DateUtil;
import org.kelab.util.FastJsonUtil;

import java.util.Date;
import java.util.Properties;

/**
 * Created by hongfei.whf on 2016/11/26.
 */
@Intercepts({
		// 增加删除更新
		@Signature(type = Executor.class, method = "update",
				args = {MappedStatement.class, Object.class}),
		// 查询
		@Signature(type = Executor.class, method = "query",
				args = {MappedStatement.class, Object.class,
						RowBounds.class, ResultHandler.class})})
@Slf4j
public class TimerInterceptor implements Interceptor {

	public static final String SQL_LOG_MINIMUM_TIME = "sql_log_minimum_time";
	// 默认打印日志最小时间为5秒钟，即sql运行1时间超过5秒会打印日志记录下来
	private long sql_log_minimum_time = 1000 * 5;

	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		log.debug("parameter: {}", parameter);
		log.debug("sql: {}", boundSql.getSql());
		ConsoleUtil.println(boundSql.getSql().replaceAll("\\s+", " "));
		long start = System.currentTimeMillis();
		Object returnValue = invocation.proceed();
		long end = System.currentTimeMillis();
		if (end - start >= sql_log_minimum_time) {
			this.log(mappedStatement.getId(), boundSql.getSql(), parameter,
					System.currentTimeMillis(), end - start);
		}
		return returnValue;
	}

	private void log(String methodId, String sql, Object parameter,
	                 long excuteTime, long costTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("=========================================").append('\n');
		sb.append("[MappedStatementId]: " + methodId).append('\n');
		sb.append("[bound sql]: " + sql.replaceAll("\\s+", " ")).append('\n');
		sb.append("[sql parameter - json format]: " + FastJsonUtil.object2Json(parameter)).append('\n');
		sb.append("[execute start time]: " + DateUtil.date2String(new Date(excuteTime),
				DateUtil.DatePatternConstant.HOUR_MINUTE_SECOND)).append('\n');
		sb.append("[execute cost time]: " + DateUtil.date2String(new Date(costTime),
				DateUtil.DatePatternConstant.HOUR_MINUTE_SECOND)).append('\n');
		sb.append("=========================================");
		log.error(sb.toString());
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		String sql_log_minimum_time = properties.getProperty(SQL_LOG_MINIMUM_TIME);
		if (sql_log_minimum_time == null) return;
		else {
			this.sql_log_minimum_time = Long.parseLong(sql_log_minimum_time);
		}
	}
}
