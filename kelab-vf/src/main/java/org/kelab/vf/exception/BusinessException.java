package org.kelab.vf.exception;

/**
 * @author wwhhf
 * @comment 业务运行时异常
 * @since 2016年5月20日
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 3152616724785436891L;

	public BusinessException(String frdMessage) {
		super(createFriendlyErrMsg(frdMessage));
	}

	public BusinessException(Throwable throwable) {
		super(throwable);
	}

	public BusinessException(Throwable throwable, String frdMessage) {
		super(throwable);
	}

	/**
	 * 抛异常打印信息
	 *
	 * @param msgBody
	 * @return
	 */
	private static String createFriendlyErrMsg(String msgBody) {
		String prefixStr = "抱歉，";
		String suffixStr = " 请稍后再试或与管理员联系！";

		StringBuffer friendlyErrMsg = new StringBuffer("");
		friendlyErrMsg.append(prefixStr);
		friendlyErrMsg.append(msgBody);
		friendlyErrMsg.append(suffixStr);
		return friendlyErrMsg.toString();
	}
}