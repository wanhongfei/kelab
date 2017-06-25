package org.kelab.vf.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by hongfei.whf on 2016/8/27.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaginationResult<T> {

	/**
	 * 分页列表
	 */
	private List<T> pagingList;

	/**
	 * 总数
	 */
	private Integer total;

}
