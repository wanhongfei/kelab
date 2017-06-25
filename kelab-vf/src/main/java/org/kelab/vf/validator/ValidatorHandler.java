package org.kelab.vf.validator;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
public interface ValidatorHandler<T> {

	/**
	 * 验证操作
	 */
	boolean vaild(T obj);

	/**
	 * 验证失败错误信息
	 *
	 * @return
	 */
	String failMsg();

}
