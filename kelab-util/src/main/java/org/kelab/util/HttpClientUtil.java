package org.kelab.util;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.kelab.util.constant.CharsetConstant;

import java.util.Map;

/**
 * Created by hongfei.whf on 2016/8/26.
 */
@Slf4j
public class HttpClientUtil {

	/**
	 * get请求+返回Json对象
	 *
	 * @param url
	 * @param params
	 * @param clazz
	 * @return
	 */
	@SneakyThrows
	public static <T> T get(@NonNull String url,
	                        @NonNull Map<String, Object> params,
	                        @NonNull Class<T> clazz) {
		String json = get(url, params);
		return FastJsonUtil.json2Object(json, clazz);
	}

	/**
	 * post请求+返回Json对象
	 *
	 * @param url
	 * @param params
	 * @param clazz
	 * @return
	 */
	@SneakyThrows
	public static <T> T post(@NonNull String url,
	                         @NonNull Map<String, Object> params,
	                         @NonNull Class<T> clazz) {
		String json = post(url, params);
		return FastJsonUtil.json2Object(json, clazz);
	}

	/**
	 * 发送get请求+返回文本
	 *
	 * @param api
	 * @param params
	 * @return
	 */
	@SneakyThrows
	public static String get(@NonNull String api,
	                         @NonNull Map<String, Object> params) {
		api = checkPrefix(api);
		api = joinParamIn2GetUrl(api, params);
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(api);
		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// 执行getMethod
		try {
			int statusCode = client.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				throw new Exception("Method failed: "
						+ getMethod.getStatusLine());
			}
			// 读取内容
			byte[] responseBody = getMethod.getResponseBody();
			// 处理内容
			return new String(responseBody, CharsetConstant.UTF_8).trim();
		} finally {
			getMethod.releaseConnection();
		}
	}

	/**
	 * 发送post请求+返回文本
	 * application/x-www-form-urlencoded 原生浏览器表单提交
	 * multipart/form-data 文件上传
	 * application/json 序列化反序列化对象
	 * text/xml xml形式传输
	 *
	 * @param api
	 * @param params
	 * @return
	 */
	@SneakyThrows
	public static String post(@NonNull String api,
	                          @NonNull Map<String, Object> params) {
		api = checkPrefix(api);
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(api);
		if (!params.isEmpty()) {
			NameValuePair[] pairs = params2NameValuePairs(params);
			postMethod.setRequestBody(pairs);
		}
		// 执行postMethod
		try {
			int statusCode = client.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				throw new Exception("Method failed: "
						+ postMethod.getStatusLine());
			}
			// 读取内容
			byte[] responseBody = postMethod.getResponseBody();
			// 处理内容
			return new String(responseBody, CharsetConstant.UTF_8).trim();
		} finally {
			postMethod.releaseConnection();
		}
	}

	/**
	 * 补充http前缀
	 *
	 * @param url
	 * @return
	 */
	private static String checkPrefix(String url) {
		return url.startsWith("http://") ? url : "http://" + url;
	}

	/**
	 * 拼装成真实的url
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	private static String joinParamIn2GetUrl(String url, Map<String, Object> params) {
		StringBuffer sb = new StringBuffer(url);
		if (params != null && params.size() > 0) {
			sb.append("?");
			sb.append(params2String(params));
		}
		return sb.toString();
	}

	/**
	 * 将参数转化为字符串
	 *
	 * @param params
	 * @return
	 */
	private static String params2String(Map<String, Object> params) {
		if (params == null || params.size() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		boolean isFirst = true;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append("&");
			}
			sb.append(entry.getKey()).append("=").append(entry.getValue());
		}
		return sb.toString();
	}

	/**
	 * 将参数转化为NameValuePair
	 *
	 * @param params
	 * @return
	 */
	private static NameValuePair[] params2NameValuePairs(Map<String, Object> params) {
		if (params == null || params.size() == 0) {
			return null;
		}
		NameValuePair[] pairs = new NameValuePair[params.size()];
		int pos = 0;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			pairs[pos++] = new NameValuePair(entry.getKey(), entry.getValue().toString());
		}
		return pairs;
	}

}
