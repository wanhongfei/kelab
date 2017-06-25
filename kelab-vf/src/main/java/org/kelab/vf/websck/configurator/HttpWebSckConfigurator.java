package org.kelab.vf.websck.configurator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * Created by hongfei.whf on 2017/1/20.
 * 为WebSocket提供Http相关对象
 */
public class HttpWebSckConfigurator extends ServerEndpointConfig.Configurator {

	@Override
	public void modifyHandshake(ServerEndpointConfig config,
	                            HandshakeRequest request,
	                            HandshakeResponse response) {
		// request
		config.getUserProperties().put(HttpServletRequest.class.getCanonicalName(), request);
		// response
		config.getUserProperties().put(HttpServletResponse.class.getCanonicalName(), response);
		// session
		HttpSession httpSession = (HttpSession) request.getHttpSession();
		config.getUserProperties().put(HttpSession.class.getCanonicalName(), httpSession);
		// 预处理,可重载
		httpHandle(request, response, httpSession, config);
	}

	/**
	 * 预处理http的请求
	 *
	 * @param request
	 * @param response
	 * @param session
	 * @param config
	 */
	protected void httpHandle(HandshakeRequest request,
	                          HandshakeResponse response,
	                          HttpSession session,
	                          ServerEndpointConfig config) {

	}

}
