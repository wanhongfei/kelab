package org.kelab.vf.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.kelab.util.StringUtil;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hongfei.whf on 2017/1/6.
 * 将日期时间戳（INT）转为long型
 */
public class Int2LongTypeHandler extends BaseTypeHandler<Long> {

	@Override
	public void setNonNullParameter(PreparedStatement preparedStatement, int i, Long value, JdbcType jdbcType) throws SQLException {
		preparedStatement.setInt(i, Integer.parseInt(value.toString()));
	}

	@Override
	public Long getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
		String time = resultSet.getString(columnName);
		// 防止精度不够，转换失败
		if (time != null && StringUtil.isNumeric(time)) {
			return Long.valueOf(time);
		}
		return null;
	}

	@Override
	public Long getNullableResult(ResultSet resultSet, int i) throws SQLException {
		String time = resultSet.getString(i);
		if (time != null && StringUtil.isNumeric(time)) {
			return Long.valueOf(time);
		}
		return null;
	}

	@Override
	public Long getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
		String time = callableStatement.getString(i);
		if (time != null && StringUtil.isNumeric(time)) {
			return Long.valueOf(time);
		}
		return null;
	}
}
