package org.kelab.vf.constant;

/**
 * 返回码
 * 注意，为了前端统一处理：
 * success返回码：以"_SUCCESS"结尾
 * error返回码：以"_ERROR"结尾
 */
public class BaseRetCodeConstant {

	/**
	 * base
	 */
	// 操作成功
	public static String SUCCESS = "SUCCESS";
	// 操作失败
	public static String ERROR = "ERROR";

	/**
	 * user
	 */
	// 登陆成功
	public static String LOGIN_SUCCESS = "LOGIN_SUCCESS";
	// 密码错误
	public static String PWD_ERROR = "PWD_ERROR";
	// 用户不存在
	public static String USER_NOT_EXIST_ERROR = "USER_NOT_EXIST_ERROR";
	// 用户已存在
	public static String USER_EXISTED_ERROR = "USER_EXISTED_ERROR";
	// 系统错误
	public static String SYSTERM_ERROR = "SYSTERM_ERROR";
	// 非法访问
	public static String ILLEGAL_ACCESS_ERROR = "ILLEGAL_ACCESS_ERROR";
	// 邮箱非法
	public static String EMAIL_IS_ILLEGAL_ERROR = "EMAIL_IS_ILLEGAL_ERROR";
	// 证书过期
	public static String JWT_IS_EXPIRE_ERROR = "JWT_IS_EXPIRE_ERROR";
	// 权限不存在
	public static String AUTH_NOT_EXIST_ERROR = "AUTH_NOT_EXIST_ERROR";
	// URL未被定义
	public static String URL_NOT_DEFINED_ERROR = "URL_NOT_DEFINED_ERROR";

	/**
	 * file
	 */
	// 上传文件错误
	public static String UPLOAD_FILE_ERROE = "UPLOAD_FILE_ERROE";
	// 上传文件前验证操作发生错误
	public static String UPLOAD_BEFORE_ERROR = "UPLOAD_BEFORE_ERROR";
	// 解析上传文件发生错误
	public static String UPLOAD_RUNTIME_ERROR = "UPLOAD_RUNTIME_ERROR";
	// 下载文件前验证操作发生错误
	public static String DOWNLOAD_BEFORE_ERROR = "DOWNLOAD_BEFORE_ERROR";
	// 下载文件操作出错
	public static String DOWNLOAD_RUNTIME_ERROR = "DOWNLOAD_RUNTIME_ERROR";

	/**
	 * vaild
	 */
	// 方法参数校验失败
	public static String METHOD_PARAMETER_VALID_ERROR = "METHOD_PARAMETER_VALID_ERROR";
	// JSON转换失败
	public static String JSON_CONVERT_ERROR = "JSON_CONVERT_ERROR";

}