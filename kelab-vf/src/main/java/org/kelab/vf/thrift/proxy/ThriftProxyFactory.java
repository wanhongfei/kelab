package org.kelab.vf.thrift.proxy;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.kelab.util.CollectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by hongfei.whf on 2017/5/27.
 */
public class ThriftProxyFactory {

	/**
	 * 实例化函数
	 *
	 * @param serviceClass
	 * @param thriftUrls
	 * @return
	 */
	public static Object newInstance(Class serviceClass, String thriftUrls) {
		ThriftProxy proxy = new ThriftProxy();
		return proxy.getInstance(serviceClass, thriftUrls);
	}

	/**
	 * 代理实现类
	 */
	public static class ThriftProxy implements InvocationHandler {

		private List<String> thriftUrls;
		private Class clientClass;
		private Class serviceClass;

		@SneakyThrows
		public Object getInstance(Class serviceClass, String thriftUrls) {
			this.serviceClass = serviceClass;
			this.clientClass = Class.forName(serviceClass.getName() + "$Client");
			this.thriftUrls = CollectionUtil.string2StrList(thriftUrls, ",");
			return Proxy.newProxyInstance(clientClass.getClassLoader(),
					clientClass.getInterfaces(), this);
		}

		/**
		 * 获取被代理的对象
		 *
		 * @param transport
		 * @return
		 */
		@SneakyThrows
		private Object getProxy(@NonNull TTransport transport) {
			TBinaryProtocol protocol = new TBinaryProtocol(transport);
			TMultiplexedProtocol tMultiplexedProtocol = new TMultiplexedProtocol(protocol,
					this.serviceClass.getCanonicalName());
			Constructor constructor = this.clientClass.getConstructor(TProtocol.class);
			TServiceClient client = (TServiceClient) constructor.newInstance(tMultiplexedProtocol);
			return client;
		}

		/**
		 * 执行方法
		 *
		 * @param proxy
		 * @param method
		 * @param args
		 * @return
		 * @throws Throwable
		 */
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (!CollectionUtil.isEmpty(this.thriftUrls)) {
				for (String thriftUrl : this.thriftUrls) {
					thriftUrl = thriftUrl.trim();
					int pos = -1;
					if ((pos = thriftUrl.indexOf(":")) != -1) {
						String host = thriftUrl.substring(0, pos);
						int port = Integer.parseInt(thriftUrl.substring(pos + 1));
						TTransport transport = new TFramedTransport(new TSocket(host, port));
						try {
							Object clientProxy = getProxy(transport);
							transport.open();
							return method.invoke(clientProxy, args);
						} finally {
							if (transport != null && transport.isOpen()) {
								transport.close();
							}
						}
					}
				}
			}
			return null;
		}
	}
}
