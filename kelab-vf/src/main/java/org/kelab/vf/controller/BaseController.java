package org.kelab.vf.controller;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.kelab.util.FastJsonUtil;
import org.kelab.util.JsonWebTokenUtil;
import org.kelab.vf.constant.BaseRetCodeConstant;
import org.kelab.vf.controller.executor.DownloadExecutor;
import org.kelab.vf.model.JsonAndModel;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * controller 封装
 *
 * @author wwhhf
 */
@Slf4j
public class BaseController {

	public static final String REQUEST_CLAIMS = "claims";

	protected final static ThreadLocal<HttpServletRequest> requests = new ThreadLocal<>();
	protected final static ThreadLocal<HttpServletResponse> responses = new ThreadLocal<>();

	/**
	 * 获取request和response
	 *
	 * @param request
	 * @param response
	 */
	@ModelAttribute
	public void init(HttpServletRequest request,
	                 HttpServletResponse response) {
		requests.set(request);
		responses.set(response);
	}

	/**
	 * 清除Threadlocal绑定的request和response
	 * 防止内存泄漏
	 *
	 * @return
	 */
	public void threadLocalClean() {
		requests.remove();
		responses.remove();
	}

	/**
	 * 获取当前request
	 *
	 * @return
	 */
	public HttpServletRequest getHttpServletRequest() {
		return requests.get();
	}

	/**
	 * 获取当前response
	 *
	 * @return
	 */
	public HttpServletResponse getHttpServletResponse() {
		return responses.get();
	}

	/**
	 * 获取当前session
	 *
	 * @return
	 */
	public HttpSession getHttpSession() {
		return requests.get().getSession();
	}

	/**
	 * 参数校验
	 *
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public JsonAndModel handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		StringBuilder sb = new StringBuilder("Invalid Request:");
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			sb.append(fieldError.getDefaultMessage()).append(",");
		}
		log.error("handleMethodArgumentNotValidException:{}", sb.toString());
		log.error(bindingResult.getFieldError().getDefaultMessage());
		return JsonAndModel.builder(BaseRetCodeConstant.METHOD_PARAMETER_VALID_ERROR).build();
	}

	/**
	 * json对象转换失败
	 *
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public JsonAndModel handleHttpMessageNotReadableException(
			HttpMessageNotReadableException ex) {
		log.error("handleHttpMessageNotReadableException:{}", ex.getMessage());
		return JsonAndModel.builder(BaseRetCodeConstant.JSON_CONVERT_ERROR).build();
	}

	/**
	 * 从Request中获取上传文件
	 *
	 * @return
	 */
	public Map<String, List<MultipartFile>> uploadFiles() {
		HttpServletRequest request = getHttpServletRequest();
		// 上传文件
		Map<String, List<MultipartFile>> filesMap = new HashMap<>();
		// 创建一个通用的文件解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 是否有文件上传(Post方法 + content-Type = multipart/form-data)
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = null;
			if (request instanceof MultipartHttpServletRequest) {
				multiRequest = (MultipartHttpServletRequest) request;
			} else {
				multiRequest = multipartResolver.resolveMultipart(request);
			}
			// 获取所有filename
			Iterator<String> varnames = multiRequest.getFileNames();
			while (varnames.hasNext()) {
				// 取得当前上传文件的文件名称
				String fileName = varnames.next();
				if (!StringUtils.isEmpty(fileName)) {
					// 获取文件
					List<MultipartFile> files = multiRequest.getFiles(fileName);
					filesMap.put(fileName, files);
				}
			}
		}
		return filesMap;
	}

	/**
	 * 下载ContestClassExcel
	 *
	 * @return
	 */
	@SneakyThrows
	protected void download(@NonNull HttpServletResponse response, @NonNull String fileName,
	                        @NonNull String contentType, @NonNull DownloadExecutor executor) {
		// 设置header
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		response.setContentType(contentType);
		// 写数据
		OutputStream os = response.getOutputStream();
		try {
			executor.execute(os);
		} finally {
			os.flush();
			os.close();
		}
	}

	/**
	 * 从Request根据varname中获取上传文件
	 *
	 * @return
	 */
	public List<MultipartFile> uploadFiles(@NonNull String varname) {
		Map<String, List<MultipartFile>> filesMap = uploadFiles();
		return filesMap.get(varname);
	}

	/**
	 * 保存session
	 *
	 * @param args
	 * @throws Exception
	 */
	public void saveSession(Object... args) throws Exception {
		HttpSession session = this.getHttpSession();
		if (args.length >= 2 && args.length % 2 == 0) {
			for (int i = 0, len = args.length; i < len; i += 2) {
				session.setAttribute(args[i].toString(), args[i + 1]);
			}
		} else {
			throw new Exception("save(String key1,Object obj1,String key2 ....)");
		}
	}

	/**
	 * 保存session
	 *
	 * @param params
	 * @throws Exception
	 */
	public void saveSession(Map<Object, Object> params) {
		HttpSession session = this.getHttpSession();
		for (Map.Entry<Object, Object> entry : params.entrySet()) {
			session.setAttribute(entry.getKey().toString(), entry.getValue());
		}
	}

	/**
	 * 获取session对象
	 *
	 * @param paramName
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> T getSession(String paramName, Class<T> clazz) {
		HttpSession session = this.getHttpSession();
		return (T) session.getAttribute(paramName);
	}

	/**
	 * 删除session对象
	 *
	 * @param paramName
	 */
	public void removeSession(String paramName) {
		HttpSession session = this.getHttpSession();
		session.removeAttribute(paramName);
	}

	/**
	 * 清空session对象
	 */
	public void clearSession() {
		HttpSession session = this.getHttpSession();
		session.invalidate();
	}

	/**
	 * 保存cookie
	 *
	 * @param path
	 * @param maxAge
	 * @param args
	 * @throws Exception
	 */
	public void saveCookie(String path, Integer maxAge, Object... args) throws Exception {
		HttpServletResponse response = getHttpServletResponse();
		if (args.length >= 2 && args.length % 2 == 0) {
			for (int i = 0, len = args.length; i < len; i += 2) {
				Cookie cookie = new Cookie(args[i].toString(),
						FastJsonUtil.object2Json(args[i + 1]));
				if (path != null) {
					cookie.setPath(path);
				}
				if (maxAge != null) {
					cookie.setMaxAge(maxAge);
				}
				response.addCookie(cookie);
			}
		} else {
			throw new Exception("save(String path,Integer maxage,String key1,Object obj1,String key2...)");
		}
	}

	/**
	 * 保存cookie对象
	 *
	 * @param path
	 * @param maxAge
	 * @param params
	 * @throws Exception
	 */
	public void saveCookie(String path, Integer maxAge, Map<Object, Object> params) {
		HttpServletResponse response = getHttpServletResponse();
		for (Map.Entry<Object, Object> entry : params.entrySet()) {
			Cookie cookie = new Cookie(entry.getKey().toString(),
					FastJsonUtil.object2Json(entry.getValue()));
			if (path != null) {
				cookie.setPath(path);
			}
			if (maxAge != null) {
				cookie.setMaxAge(maxAge);
			}
			response.addCookie(cookie);
		}
	}

	/**
	 * 获取cookie对象
	 *
	 * @param name
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> T getCookie(String name, Class<T> clazz) {
		HttpServletRequest request = getHttpServletRequest();
		Cookie cookies[] = request.getCookies();
		for (int i = 0, length = cookies.length; i < length; i++) {
			if (cookies[i].getName().equals(name)) {
				return (T) FastJsonUtil.json2Object(cookies[i].getValue(), clazz);
			}
		}
		return null;
	}

	/**
	 * 删除cookie对象
	 *
	 * @param paramName
	 */
	public void removeCookie(String paramName) {
		HttpServletRequest request = getHttpServletRequest();
		Cookie cookies[] = request.getCookies();
		for (int i = 0, length = cookies.length; i < length; i++) {
			if (cookies[i].getName().equals(paramName)) {
				cookies[i].setMaxAge(0);
			}
		}
	}

	/**
	 * 清空cookie对象
	 */
	public void clearCookie() {
		HttpServletRequest request = getHttpServletRequest();
		Cookie cookies[] = request.getCookies();
		for (int i = 0, length = cookies.length; i < length; i++) {
			cookies[i].setMaxAge(0);
		}
	}

	/**
	 * 向request中设置claims
	 *
	 * @param request
	 * @param token
	 */
	public void saveClaims2Request(HttpServletRequest request, String token) {
		Claims claims = JsonWebTokenUtil.token2Claims(token);
		request.setAttribute(REQUEST_CLAIMS, claims);
	}

	/**
	 * 获取JsonWebToken的Claims对象
	 *
	 * @return
	 */
	public Claims getClaimsFromRequest() {
		HttpServletRequest request = getHttpServletRequest();
		return (Claims) request.getAttribute(REQUEST_CLAIMS);
	}

	/**
	 * 获取JsonWebToken的Claims对象
	 *
	 * @return
	 */
	public <T> T getClaimsValue(@NonNull String key, Class<T> clazz) {
		Claims claims = getClaimsFromRequest();
		if (claims == null) return null;
		return (T) claims.get(key);
	}

	/**
	 * 获取ip地址
	 * 127.0.0.1
	 *
	 * @return
	 */
	public String getClientIpAddr() {
		HttpServletRequest request = getHttpServletRequest();
		return request.getRemoteAddr();
	}

	/**
	 * 获取域名
	 * zhidao.baidu.com
	 *
	 * @return
	 */
	public String getServerName() {
		HttpServletRequest request = getHttpServletRequest();
		return request.getServerName();
	}

	/**
	 * 获取请求方法
	 * GET,POST,PUT,DELETE
	 *
	 * @return
	 */
	public String getRequestMethod() {
		HttpServletRequest request = getHttpServletRequest();
		return request.getMethod();
	}

	/**
	 * 获取请求完整的url
	 * http://localhost:8080/test/servlet/RequestDemo
	 *
	 * @return
	 */
	public String getRequestUrl() {
		HttpServletRequest request = getHttpServletRequest();
		return request.getRequestURL().toString();
	}

	/**
	 * 获取请求资源名字
	 * /test/servlet/RequestDemo
	 *
	 * @return
	 */
	public String getRequestUri() {
		HttpServletRequest request = getHttpServletRequest();
		return request.getRequestURI();
	}

	/**
	 * 内部跳转
	 *
	 * @param url
	 * @throws ServletException
	 * @throws IOException
	 */
	public void forword(String url) throws ServletException, IOException {
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse response = getHttpServletResponse();
		request.getRequestDispatcher(url).forward(request, response);
	}

	/**
	 * 外部跳转，location
	 *
	 * @param url
	 * @throws IOException
	 */
	public void sendRedirect(String url) throws IOException {
		HttpServletResponse response = getHttpServletResponse();
		response.sendRedirect(url);
	}

}
