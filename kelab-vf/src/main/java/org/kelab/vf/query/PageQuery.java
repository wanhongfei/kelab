package org.kelab.vf.query;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by hongfei.whf on 2016/8/26.
 */
@ToString(includeFieldNames = true, callSuper = true)
public class PageQuery extends RoleQuery {

	public static final int MAX_ROWS = 200;

	/**
	 * 当前页
	 */
	@Getter
	protected Integer page = null;

	/**
	 * 每页访问条数
	 */
	@Getter
	protected Integer rows = null;

	/**
	 * 开始条数
	 */
	@Getter
	protected Integer start = null;

	public void setPage(Integer page) {
		this.page = page;
		if (this.rows != null && this.page != null) {
			this.start = (page - 1) * rows;
		}
	}

	public void setRows(Integer rows) {
		// 防止过大的行数导致的内存溢出
		if (rows > MAX_ROWS) rows = MAX_ROWS;
		this.rows = rows;
		if (this.rows != null && this.page != null) {
			this.start = (page - 1) * rows;
		}
	}

}
