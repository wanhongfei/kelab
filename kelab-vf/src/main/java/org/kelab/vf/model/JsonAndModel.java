package org.kelab.vf.model;

import lombok.Data;
import lombok.ToString;

/**
 * json 返回实例
 *
 * @author wwhhf
 */
@ToString(includeFieldNames = true)
@Data
public class JsonAndModel {

	private static final String SUFFIX = "; Secure; HttpOnly;";

	/**
	 * 接口返回数据
	 */
	private Object data = null;

	/**
	 * 证书
	 */
	private String access_token = null;

	/**
	 * 访问状态
	 */
	private String code = null;

	/**
	 * 私有构造器
	 *
	 * @param builder
	 */
	private JsonAndModel(Builder builder) {
		this.setData(builder.getData());
		this.setAccess_token(builder.getAccess_token());
		this.setCode((builder.getCode()));
	}

	/**
	 * 构造函数
	 * 必须带回响应code
	 *
	 * @param code
	 */
	public static Builder builder(String code) {
		Builder builder = new Builder();
		builder.setCode(code);
		return builder;
	}

	/**
	 * 建造者模式
	 */
	@Data
	public static class Builder {

		private Object data = null;
		private String access_token = null;
		private String code = null;

		/**
		 * 链式调用
		 *
		 * @param code
		 */
		public Builder code(String code) {
			this.code = code;
			return this;
		}

		public Builder token(String access_token) {
			this.access_token = access_token;
			return this;
		}

		public Builder data(Object data) {
			this.data = data;
			return this;
		}

		public JsonAndModel build() {
			return new JsonAndModel(this);
		}
	}
}
