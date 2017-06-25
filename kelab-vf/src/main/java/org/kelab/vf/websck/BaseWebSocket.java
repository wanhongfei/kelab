package org.kelab.vf.websck;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by hongfei.whf on 2017/1/20.
 * 容器会为每一个连接创建一个EndPoint的实例，需要利用实例变量来保存一些状态信息。
 * Session代表着服务端点与远程客户端点的一次会话
 */
@Slf4j
public class BaseWebSocket {

	// webSockets
	protected static final List<Session> webSockets = new CopyOnWriteArrayList<Session>();

	/**
	 * 对所有websocket会话发出消息
	 *
	 * @param msg
	 */
	public static void sendMsg2WebSockets(String msg) {
		if (webSockets.size() > 0) {
			for (Session session : webSockets) {
				if (session.isOpen()) {
					try {
						synchronized (session) {
							session.getBasicRemote().sendText(msg);
						}
					} catch (IOException e) {
						log.error("sendMsg2WebSockets error:{}", msg);
					}
				}
			}
		}
	}
}
