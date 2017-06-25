package org.kelab.vf.configurer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.kelab.util.JdbcUtil;
import org.kelab.util.PropertiesUtil;
import org.kelab.util.StringUtil;
import org.kelab.util.model.Pair;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by wanhongfei on 2016/12/28.
 * 使用db的配置信息
 */
@Deprecated
@Slf4j
public class DBPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private static final String cfgFileName = "cs.properties";
	private static final String cfgUriKeyName = "cs.uri";
	private static final String cfgUsernameKeyName = "cs.username";
	private static final String cfgPasswordName = "cs.password";

	@Override
	@SneakyThrows
	protected Properties mergeProperties() {
		Properties properties = new Properties();
		PropertiesConfiguration propertiesConfiguration = PropertiesUtil.getPropertiesConfiguration(cfgFileName);
		String uri = propertiesConfiguration.getString(cfgUriKeyName);
		String username = propertiesConfiguration.getString(cfgUsernameKeyName);
		String password = propertiesConfiguration.getString(cfgPasswordName);
		if (StringUtil.isBlank(uri)) throw new Exception("config uri is not exist.");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(uri, username, password);
			ps = conn.prepareStatement("select * from Config");
			rs = ps.executeQuery();
			List<Pair<String, String>> cfgs = JdbcUtil.execute(rs, new JdbcUtil.ResultSetCallBack<Pair<String, String>>() {
				@Override
				public Pair<String, String> callback(ResultSet rs) throws SQLException {
					System.out.println(new Pair<String, String>(rs.getString(1), rs.getString(2)));
					return new Pair<String, String>(rs.getString(1), rs.getString(2));
				}
			});
			for (Pair<String, String> cfg : cfgs) {
				properties.put(cfg.getValue1(), cfg.getValue2());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("请检查uri和sql是否正确，sql格式：select key,value from xxx");
		} finally {
			if (ps != null) ps.close();
			if (conn != null) conn.close();
		}
		return properties;
	}
}
