package org.kelab.vf.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hongfei.whf on 2016/8/27.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult {

	/**
	 * 登陆用户名
	 */
	private String username;

	/**
	 * 登陆角色
	 */
	private Integer roleId;

	/**
	 * 登陆状态
	 */
	private String status;

	/**
	 * 用户id
	 */
	private Integer userId;
}
