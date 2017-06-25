package org.kelab.vf.thrift.impl;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.kelab.vf.thrift.IThriftServiceConsumer;
import org.kelab.vf.thrift.proxy.ThriftProxyFactory;

/**
 * Created by hongfei.whf on 2017/3/29.
 */
public class ThriftServiceConsumer implements IThriftServiceConsumer {

	/**
	 * 获取指定的thrift的service里面的client实例对象
	 *
	 * @param serviceClass
	 * @return
	 */
	@Override
	@SneakyThrows
	public Object getServiceClient(@NonNull Class serviceClass, @NonNull String thriftUrls) {
		return ThriftProxyFactory.newInstance(serviceClass, thriftUrls);
	}
}
