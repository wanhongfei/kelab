package org.kelab.vf.thrift;

/**
 * Created by hongfei.whf on 2017/3/29.
 */
public interface IThriftServiceConsumer {

	/**
	 * 获取客户端
	 *
	 * @return
	 */
	Object getServiceClient(Class serviceClass, String thriftUrls);
}
