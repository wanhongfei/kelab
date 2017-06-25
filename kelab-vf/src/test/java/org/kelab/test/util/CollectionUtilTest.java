package org.kelab.test.util;

import org.junit.Test;
import org.kelab.util.CollectionUtil;

import java.util.List;

/**
 * Created by hongfei.whf on 2017/3/3.
 */
public class CollectionUtilTest {

	@Test
	public void test() {
		List<Integer> a = CollectionUtil.string2IntList("1,2,4,5,6,7,8,10", ",");
		System.out.println(CollectionUtil.sum(a, 0));
	}
}
