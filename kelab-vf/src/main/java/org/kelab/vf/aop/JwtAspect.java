package org.kelab.vf.aop;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.kelab.util.PropertiesUtil;
import org.kelab.vf.constant.BaseRetCodeConstant;
import org.kelab.vf.controller.BaseController;
import org.kelab.vf.model.JsonAndModel;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wwhhf
 * @comment Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.
 * eyJleHAiOjE0NjU2MjcxNDYsInVzZXJuYW1lIjoid3doaGZmMTEiLCJoYWhhaCI6MSwiaXNzIjoiU3d1c3RPSiBKV1QiLCJpYXQiOjE0NjU2MjY1NDZ9
 * .2KkBpekFqNqiRBcOj9XvSTLUDVY_l-fRdTatXMSMMIo
 * @since 2016年6月11日
 */
@Slf4j
public class JwtAspect {

	public static final String JWT_REQUEST_HEADER = "jwt.request_header";
	public static final String JWT_AUTH_HEADER = "jwt.auth_header";

	/**
	 * 负责jwt解析和刷新
	 *
	 * @param joinpoint
	 * @return
	 * @throws Throwable
	 */
	public Object resolveRequestJwtHeader(ProceedingJoinPoint joinpoint) throws Throwable {
		// 获取配置信息
		String REQUEST_HEADER = PropertiesUtil.getPropertyByName(JWT_REQUEST_HEADER);
		String AUTH_HEADER = PropertiesUtil.getPropertyByName(JWT_AUTH_HEADER);
		Object[] args = joinpoint.getArgs();
		BaseController controller = (BaseController) joinpoint.getTarget();
		HttpServletRequest request = controller.getHttpServletRequest();
		String reqAuthHeader = request.getHeader(REQUEST_HEADER);
		if (reqAuthHeader != null && reqAuthHeader.startsWith(AUTH_HEADER)) {
			// 如果存在jwt则解析
			String token = reqAuthHeader.substring(AUTH_HEADER.length()).trim();
			try {
				controller.saveClaims2Request(request, token);
			} catch (ExpiredJwtException ex) {
				JsonAndModel jsonAndModel = (JsonAndModel) joinpoint.proceed(args);
				jsonAndModel.setCode(BaseRetCodeConstant.JWT_IS_EXPIRE_ERROR);
				return jsonAndModel;
			}
		}
		// 不存在jwt则直接执行
		return joinpoint.proceed(args);
	}

}
