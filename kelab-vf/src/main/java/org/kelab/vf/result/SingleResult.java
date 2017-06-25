package org.kelab.vf.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hongfei.whf on 2016/8/28.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleResult<T> {

	/**
	 * 该对象可为返回单个对像时使用
	 */
	private T obj;

}
