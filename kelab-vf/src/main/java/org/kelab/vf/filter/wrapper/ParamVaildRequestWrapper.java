package org.kelab.vf.filter.wrapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kelab.util.CollectionUtil;
import org.kelab.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongfei.whf on 2017/3/8.
 * 过滤无效空参数
 */
@Slf4j
public class ParamVaildRequestWrapper extends HttpServletRequestWrapper {

	public ParamVaildRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 过滤无效空参数，例如title=&ids=
	 *
	 * @param name
	 * @return
	 */
	@Override
	@SneakyThrows
	public String[] getParameterValues(String name) {
		String[] arr = super.getParameterValues(name);
		if (arr != null) {
			List<String> values = CollectionUtil.array2List(arr);
			List<String> res = new ArrayList<>();
			for (String value : values) {
				if (StringUtil.isNotBlank(value)) {
					res.add(value);
				}
			}
			if (CollectionUtil.isEmpty(res)) {
				return null;
			} else {
				return CollectionUtil.list2Array(res, String.class);
			}
		} else {
			return null;
		}
	}
}
