package org.kelab.test.util;

import org.junit.Test;
import org.kelab.util.QRCodeUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongfei.whf on 2017/5/15.
 */
public class QRCodeTest {
	@Test
	public void test() {
		Map<String, Object> map = new HashMap<>();
		map.put("username", "1");
		map.put("passwd", 19940921);
		QRCodeUtil.encode(map, 50, 50, QRCodeUtil.PNG,
				"C:\\tmp", "test.png");
		Map<String, Object> map2 = QRCodeUtil.decode(new File("C:\\tmp\\test.png"));
		System.out.println(map2.get("username"));
	}

}
