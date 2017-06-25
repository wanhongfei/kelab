package org.kelab.vf.controller;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.kelab.vf.constant.BaseRetCodeConstant;
import org.kelab.vf.model.JsonAndModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by hongfei.whf on 2016/8/29.
 */
@Slf4j
public abstract class AbstractDownloadController extends BaseController {

	protected String RESPONSE_HEADER_KEY = "Content-Disposition";
	protected String RESPONSE_HEADER_VALUE = "attachment;filename=";

	@ResponseBody
	@RequestMapping(value = "/download.do", method = {RequestMethod.GET})
	public JsonAndModel execute() throws IOException {
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse response = getHttpServletResponse();
		HttpSession session = getHttpSession();
		// 下载前执行
		if (!downloadBefore(request, response, session)) {
			return JsonAndModel.builder(BaseRetCodeConstant.DOWNLOAD_BEFORE_ERROR).build();
		}
		// 获取输出文件名
		String fileName = fileName();
		if (StringUtils.isEmpty(fileName)) {
			log.error("File Name is Null.");
			throw new RuntimeException("File Name is Null.");
		}
		// 设置response的Header
		response.addHeader(RESPONSE_HEADER_KEY,
				RESPONSE_HEADER_VALUE + fileName);
		// 设置respone的Content-Type
		String contentType = responseContentType();
		if (StringUtils.isEmpty(contentType)) {
			log.error("response Content-Type Name is Null.");
			throw new RuntimeException("response Content-Type Name is Null.");
		}
		response.setContentType(contentType);
		// 获取输出流
		@Cleanup OutputStream out = null;
		try {
			out = response.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("getOutputStream error");
			return JsonAndModel.builder(BaseRetCodeConstant.SYSTERM_ERROR).build();
		}
		try {
			// 填充输出流
			if (!processDownload(out)) {
				log.error("processDownload return false");
				return JsonAndModel.builder(BaseRetCodeConstant.DOWNLOAD_RUNTIME_ERROR).build();
			}
			// 下载后执行
			downloadAfter(request, response, session);
			return JsonAndModel.builder(BaseRetCodeConstant.SUCCESS).build();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("download error");
			// 输出流异常处理
			downloadException(request, response, session);
			return JsonAndModel.builder(BaseRetCodeConstant.DOWNLOAD_RUNTIME_ERROR).build();
		}
	}

	/**
	 * 下载前执行
	 *
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	protected abstract boolean downloadBefore(HttpServletRequest request,
	                                          HttpServletResponse response,
	                                          HttpSession session);

	/**
	 * 下载后执行
	 *
	 * @param request
	 * @param response
	 * @param session
	 */
	protected abstract void downloadAfter(HttpServletRequest request,
	                                      HttpServletResponse response,
	                                      HttpSession session);

	/**
	 * 下载异常执行
	 *
	 * @param request
	 * @param response
	 * @param session
	 */
	protected abstract void downloadException(HttpServletRequest request,
	                                          HttpServletResponse response,
	                                          HttpSession session);

	/**
	 * 填充输出流
	 *
	 * @param out
	 * @return
	 */
	protected abstract boolean processDownload(OutputStream out) throws IOException;

	/**
	 * 设置response的contentType
	 */
	protected abstract String responseContentType();

	/**
	 * 获取下载文件名
	 */
	protected abstract String fileName();
}
