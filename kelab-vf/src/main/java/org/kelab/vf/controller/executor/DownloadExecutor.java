package org.kelab.vf.controller.executor;

import java.io.OutputStream;

/**
 * Created by hongfei.whf on 2017/4/21.
 */
public interface DownloadExecutor {

	/**
	 * 下载操作
	 *
	 * @param os
	 * @param os
	 */
	void execute(OutputStream os);

}
