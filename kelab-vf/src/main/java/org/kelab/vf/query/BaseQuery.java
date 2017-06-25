package org.kelab.vf.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.kelab.util.CollectionUtil;

import java.util.List;

/**
 * Created by hongfei.whf on 2016/8/26.
 */
@ToString(includeFieldNames = true, callSuper = true)
public class BaseQuery extends PageQuery {

	/**
	 * id
	 */
	@Getter
	@Setter
	protected Integer id;

	/**
	 * 对象id列表
	 */
	@Getter
	protected List<Integer> ids;

	/**
	 * 是否查询自定义的字段（实现impl中）
	 */
	@Getter
	protected boolean customFields = false;

	/**
	 * 设置ids
	 *
	 * @param ids
	 */
	public void setIds(String ids) {
		if (ids != null) {
			this.ids = CollectionUtil.string2IntList(ids, ",");
		}
	}

	/**
	 * 设置ids
	 *
	 * @param ids
	 */
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

}
