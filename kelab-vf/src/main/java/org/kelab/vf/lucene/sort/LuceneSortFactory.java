package org.kelab.vf.lucene.sort;

import lombok.NonNull;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

/**
 * Created by wwhhff11 on 16-8-15.
 */
public class LuceneSortFactory {

	/**
	 * 获取排序对象
	 * 按照某个字段进行排序
	 *
	 * @param field
	 * @return
	 */
	public static Sort getSortByField(@NonNull String field, @NonNull Boolean desc) {
		Sort sort = new Sort();
		sort.setSort(new SortField(field, SortField.Type.DOC, desc));
		return sort;
	}

}
