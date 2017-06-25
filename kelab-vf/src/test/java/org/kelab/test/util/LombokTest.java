package org.kelab.test.util;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hongfei.whf on 2016/8/31.
 */
public class LombokTest {

	@SneakyThrows
	public static void test() {
		@Cleanup InputStream in = null;
		in = new InputStream() {

			@Override
			public void close() throws IOException {
				super.close();
				System.out.println("this is close");
			}

			@Override
			public int read() throws IOException {
				return 0;
			}
		};
		System.out.println("haha1");
		if (1 == 1) {
			Integer.valueOf("s");
		}
		System.out.println("haha2");
	}

	public static void main(String args[]) {
		try {
			test();
			System.out.println("hshs3");
		} catch (Exception e) {
			System.out.println("haha3");
		}
	}
}