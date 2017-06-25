package org.kelab.vf.constant;

/**
 * Created by wanhongfei on 2016/12/28.
 */
public class CacheConstant {

	// 忘记密码验证码key的前缀：RESET_USER_PWD_1000(userid)
	public static final String RESET_PWD_CODE_PREFIX_KEY = "RESET_USER_PWD_";

	// 类切面缓存后缀
	public static final String CLASS_VERSION_SUFFIX = "VERSION";

	// 类切面缓存返回类型
	public static final Class<Long> CLASS_VERSION_TYPE = Long.class;
}
