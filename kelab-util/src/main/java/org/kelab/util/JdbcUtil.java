package org.kelab.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wanhongfei on 2016/12/21.
 */
@Slf4j
public class JdbcUtil {

	/**
	 * 解析resultset
	 *
	 * @param <T>
	 * @param rs
	 * @param callBack @return
	 */
	public static <T> List<T> execute(ResultSet rs, ResultSetCallBack<T> callBack) {
		return new JdbcResultSetResolver<T>(rs).execute(callBack);
	}

	/**
	 * 预编译
	 *
	 * @param connection
	 * @param sql
	 * @return
	 */
	@SneakyThrows
	public static PreparedStatement createPreparedStatement(@NonNull Connection connection, @NonNull String sql) {
		return connection.prepareStatement(sql);
	}

	/**
	 * 设置参数
	 *
	 * @param args
	 */
	@SneakyThrows
	public static void precompile(@NonNull PreparedStatement ps, @NonNull Object... args) {
		for (int i = 0, len = args.length; i < len; i++) {
			setParameter(ps, i + 1, args[i]);
		}
	}

	/**
	 * 设置参数
	 *
	 * @param ps
	 * @param pos
	 * @param parameter
	 * @throws Exception
	 */
	public static void setParameter(@NonNull PreparedStatement ps, int pos, @NonNull Object parameter) throws Exception {
		Class clazz = parameter.getClass();
		if (clazz.equals(Integer.class)) {
			ps.setInt(pos, (Integer) parameter);
		} else if (clazz.equals(Long.class)) {
			ps.setLong(pos, (Long) parameter);
		} else if (clazz.equals(Float.class)) {
			ps.setFloat(pos, (Float) parameter);
		} else if (clazz.equals(Double.class)) {
			ps.setDouble(pos, (Double) parameter);
		} else if (clazz.equals(Date.class)) {
			ps.setDate(pos, DateUtil.date2sqlDate((Date) parameter));
		} else if (clazz.equals(java.sql.Date.class)) {
			ps.setDate(pos, (java.sql.Date) parameter);
		} else if (clazz.equals(String.class)) {
			ps.setString(pos, (String) parameter);
		} else if (clazz.equals(Boolean.class)) {
			ps.setBoolean(pos, (Boolean) parameter);
		} else {
			throw new Exception("only support int|long|float|double|date|string|boolean");
		}
	}

	/**
	 * resultset调用callback
	 */
	public static interface ResultSetCallBack<T> {

		/**
		 * callback
		 *
		 * @return
		 */
		public T callback(ResultSet rs) throws SQLException;

	}

	@Data
	@AllArgsConstructor
	public static class JdbcResultSetResolver<T> {
		// rs
		private ResultSet rs;

		/**
		 * 对象进行映射
		 *
		 * @param callBack
		 * @return
		 */
		protected List<T> execute(ResultSetCallBack<T> callBack) {
			List<T> list = new ArrayList<>();
			try {
				while (rs.next()) {  //通过next来索引：判断是否有下一个记录
					list.add(callBack.callback(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage());
			} finally {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					log.error(e.getMessage());
				}
			}
			return list;
		}

	}

}
