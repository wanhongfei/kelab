package org.kelab.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wwhhf
 * @comment { "iss": "John Wu JWT", "iat": 1441593502, "exp": 1441594722, "aud":
 * "www.example.com", "sub": "jrocket@example.com" ...}
 * @since 2016年6月3日
 */
public class JsonWebTokenUtil {

	/**
	 * @param sub
	 * @param claims
	 * @return
	 * @throws Exception
	 * @author wwhhf
	 * @comment 创建jwt
	 * @since 2016年6月3日
	 */
	@SneakyThrows
	public static String tokens(@NonNull String sub, @NonNull Map<String, Object> claims) {
		int millisecond = PropertiesUtil.getPropertyByName(JwtAttrConstant.DEFAULT_EXP_TIME, Integer.class);
		return tokens(sub, claims, millisecond);
	}

	/**
	 * @param sub
	 * @param obj
	 * @return
	 * @throws Exception
	 * @author wwhhf
	 * @comment 创建jwt
	 * @since 2016年6月3日
	 */
	@SneakyThrows
	public static String tokens(@NonNull String sub, @NonNull Object obj) {
		int millisecond = PropertiesUtil.getPropertyByName(JwtAttrConstant.DEFAULT_EXP_TIME, Integer.class);
		return tokens(sub, BeanUtil.bean2Map(obj), millisecond);
	}

	/**
	 * @param sub
	 * @param claims
	 * @param millisecond
	 * @return
	 * @throws Exception
	 * @author wwhhf
	 * @comment 创建jwt
	 * @since 2016年6月3日
	 */
	@SneakyThrows
	public static String tokens(@NonNull String sub, @NonNull Map<String, Object> claims, long millisecond) {
		String secretKey = PropertiesUtil.getPropertyByName(JwtAttrConstant.SECRET_KEY);
		String iss = PropertiesUtil.getPropertyByName(JwtAttrConstant.ISS);
		String aud = PropertiesUtil.getPropertyByName(JwtAttrConstant.AUD);
		Date currTime = DateUtil.currentDate();
		Date expirationTime = DateUtil.add2Date(currTime, Calendar.MILLISECOND, millisecond);
		// build jwt
		JwtBuilder jwt = Jwts.builder()
				.signWith(SignatureAlgorithm.HS256, secretKey).setSubject(sub)
				.setClaims(claims)
				.setIssuedAt(currTime)
				.setExpiration(expirationTime)
				.setIssuer(iss)
				.setAudience(aud);
		return jwt.compact();
	}

	/**
	 * @param sub
	 * @param obj
	 * @param millisecond
	 * @return
	 * @throws Exception
	 * @author wwhhf
	 * @comment 创建jwt
	 * @since 2016年6月3日
	 */
	@SneakyThrows
	public static String tokens(@NonNull String sub, @NonNull Object obj, long millisecond) {
		Map<String, Object> claims = BeanUtil.bean2Map(obj);
		return tokens(sub, claims, millisecond);
	}

	/**
	 * @param sub
	 * @param key
	 * @param value
	 * @param millisecond
	 * @return
	 * @throws Exception
	 * @author wwhhf
	 * @comment 创建jwt
	 * @since 2016年6月3日
	 */
	@SneakyThrows
	public static String tokens(@NonNull String sub, @NonNull String key,
	                            @NonNull Object value, long millisecond) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(key, value);
		return tokens(sub, claims, millisecond);
	}

	/**
	 * @param sub
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 * @author wwhhf
	 * @comment 创建jwt
	 * @since 2016年6月3日
	 */
	@SneakyThrows
	public static String tokens(@NonNull String sub, @NonNull String key, @NonNull Object value) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(key, value);
		return tokens(sub, claims);
	}

	/**
	 * @return
	 * @author wwhhf
	 * @comment tokens=>claims,若过期则抛出异常
	 * @since 2016年6月3日
	 */
	public static Claims token2Claims(@NonNull String token) {
		String secretKey = PropertiesUtil.getPropertyByName(JwtAttrConstant.SECRET_KEY);
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	/**
	 * 属性
	 */
	@ToString
	public static class JwtAttrConstant {

		public static final String ISS = "jwt.iss";
		public static final String DEFAULT_EXP_TIME = "jwt.default_exp";
		public static final String DEFAULT_ADVANCE_REFRESH_TIME = "jwt.advance_refresh";
		public static final String AUD = "jwt.aud";
		public static final String SECRET_KEY = "jwt.secret_key";

	}

}