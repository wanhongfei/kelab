package org.kelab.test.util;

import org.kelab.util.HttpClientUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongfei.whf on 2016/9/1.
 * http://localhost:8080/api/tags.do?page=1&rows=20
 */
public class HttpClientTest {

	public static void main(String args[]) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("name", "liudong");
		params.put("mobile", "123456");
//        http://java.sun.com/?name=liudong&mobile=123456
		System.out.println(HttpClientUtil.post("http://java.sun.com",
				params
		));
	}

}
