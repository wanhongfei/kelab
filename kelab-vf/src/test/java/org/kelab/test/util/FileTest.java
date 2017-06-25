package org.kelab.test.util;

import org.junit.Test;
import org.kelab.util.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by hongfei.whf on 2016/11/13.
 */
public class FileTest {

	@Test
	public void testWriteAndRead() throws IOException {
		FileUtil.writeFile("helloworld", "C:\\Users\\hongfei.whf\\Desktop\\test.txt");
		System.out.println(FileUtil.readFile("C:\\Users\\hongfei.whf\\Desktop\\test.txt"));
	}

	@Test
	public void testByteArrayAndFile() throws IOException {
		byte[] bytes = FileUtil.file2ByteArray(new File("C:\\Users\\hongfei.whf\\Desktop\\test.txt"));
		FileUtil.byteArray2File(bytes, "C:\\Users\\hongfei.whf\\Desktop\\test1.txt");
		System.out.println(FileUtil.readFile("C:\\Users\\hongfei.whf\\Desktop\\test1.txt"));
		FileUtil.deleteFile("C:\\Users\\hongfei.whf\\Desktop\\test1.txt");
	}
}
