package org.kelab.vf.thrift.impl;

import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.kelab.util.CollectionUtil;
import org.kelab.util.ConsoleUtil;
import org.kelab.util.model.Pair;
import org.kelab.vf.thrift.AbstractThriftMultiServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hongfei.whf on 2017/3/29.
 */
@Slf4j
public class ThriftMultiServiceProvider extends AbstractThriftMultiServiceProvider {

	/**
	 * 默认开放端口为9090
	 */
	@Setter
	private int port = 9090;

	/**
	 * 需要开放的服务集合
	 */
	@Setter
	private List<Object> serviceImplInstances = new ArrayList<>();

	/**
	 * thrift 服务
	 */
	private TServer server;

	/**
	 * 单线程池
	 */
	private ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

	@Override
	@SneakyThrows
	public void init() {
		if (!CollectionUtil.isEmpty(serviceImplInstances)) {
			TMultiplexedProcessor processor = new TMultiplexedProcessor();
			// 注册服务
			registerServices(processor);
			// 传输通道 - 非阻塞方式
			TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
			//多线程半同步半异步
			TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverTransport);
			tArgs.processor(processor);
			tArgs.transportFactory(new TFramedTransport.Factory());
			//二进制协议
			tArgs.protocolFactory(new TBinaryProtocol.Factory());
			// 多线程半同步半异步的服务模型
			this.server = new TThreadedSelectorServer(tArgs);
			this.singleThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					server.serve();//启动服务
				}
			});
		}
	}

	@Override
	public void destory() {
		if (this.server != null && this.server.isServing()) {
			this.server.setShouldStop(true);
			this.server.stop();
			this.server = null;
		}
		this.singleThreadPool.shutdown();
	}

	/**
	 * 注册服务
	 *
	 * @param processor
	 */
	@SneakyThrows
	private void registerServices(@NonNull TMultiplexedProcessor processor) {
		for (Object serviceImplInstance : this.serviceImplInstances) {
			Pair<String, TProcessor> pair = this.serviceName2Processor(serviceImplInstance);
			if (pair != null) {
				String serviceName = pair.getValue1();
				TProcessor serviceProcessor = pair.getValue2();
				processor.registerProcessor(serviceName, serviceProcessor);
				ConsoleUtil.println("%s -> %s", serviceName, serviceProcessor);
			} else {
				ConsoleUtil.println("%s is illegal", serviceImplInstance);
			}
		}
	}

}
