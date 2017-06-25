package org.kelab.vf.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.kelab.vf.constant.UserRoleConstant;

/**
 * Created by hongfei.whf on 2016/12/31.
 */
@ToString(includeFieldNames = true)
public class RoleQuery {

	/**
	 * 当前用户等级
	 */
	@Getter
	@Setter
	protected int userRoleId = UserRoleConstant.NOT_LOGIN;

}
