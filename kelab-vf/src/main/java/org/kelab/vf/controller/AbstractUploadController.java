package org.kelab.vf.controller;

import org.kelab.vf.constant.BaseRetCodeConstant;
import org.kelab.vf.model.JsonAndModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by hongfei.whf on 2016/8/30.
 */
public abstract class AbstractUploadController extends BaseController {

	/**
	 * 默认上传执行接口
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/upload.do", method = {RequestMethod.POST})
	public JsonAndModel execute() {
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse response = getHttpServletResponse();
		HttpSession session = getHttpSession();
		if (!uploadBefore(request, response, session)) {
			return JsonAndModel.builder(BaseRetCodeConstant.UPLOAD_BEFORE_ERROR).build();
		}
		Map<String, List<MultipartFile>> filesMap = uploadFiles();
		if (filesMap == null || filesMap.size() == 0) {
			return JsonAndModel.builder(BaseRetCodeConstant.UPLOAD_FILE_ERROE).build();
		}
		for (Map.Entry<String, List<MultipartFile>> entry : filesMap.entrySet()) {
			if (!proccessUpload(entry.getKey(), entry.getValue())) {
				uploadException(request, response, session);
				return JsonAndModel.builder(BaseRetCodeConstant.UPLOAD_RUNTIME_ERROR).build();
			}
		}
		uploadAfter(request, response, session);
		return JsonAndModel.builder(BaseRetCodeConstant.SUCCESS).build();
	}

	/**
	 * 上传前执行
	 *
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	protected abstract boolean uploadBefore(HttpServletRequest request,
	                                        HttpServletResponse response,
	                                        HttpSession session);

	/**
	 * 上传后执行
	 *
	 * @param request
	 * @param response
	 * @param session
	 */
	protected abstract void uploadAfter(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    HttpSession session);

	/**
	 * 上传出现异常执行
	 *
	 * @param request
	 * @param response
	 * @param session
	 */
	protected abstract void uploadException(HttpServletRequest request,
	                                        HttpServletResponse response,
	                                        HttpSession session);

	/**
	 * 处理上传文件
	 *
	 * @param fileName
	 * @param files
	 * @return
	 */
	protected abstract boolean proccessUpload(String fileName, List<MultipartFile> files);

}
