package org.kelab.util;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kelab.util.constant.CharsetConstant;

import java.io.*;

/**
 * 流操作
 *
 * @author wwhhff11
 * @since 2016/03/02
 */
@Slf4j
public class StreamUtil {

	/**
	 * 从输出流获取数据
	 *
	 * @param is
	 * @return
	 * @throws IOException
	 * @author wwhhff11
	 */
	@SneakyThrows
	public static String inputStream2String(@NonNull InputStream is) {
		@Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(is, CharsetConstant.UTF_8));
		StringBuffer sb = new StringBuffer();
		String s = null;
		try {
			while ((s = reader.readLine()) != null) {
				sb.append(s).append("\n");
			}
		} catch (IOException e) {
			log.error("InputStream IOException");
			throw e;
		}
		return sb.toString();
	}

	/**
	 * 向输入流输入数据
	 *
	 * @param os
	 * @param cmd
	 * @throws IOException
	 */
	@SneakyThrows
	public static void input(@NonNull OutputStream os, @NonNull String cmd) {
		@Cleanup BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(os, CharsetConstant.UTF_8));
			writer.write(cmd);
			writer.flush();
		} catch (IOException e) {
			log.error("OutputStream IOException");
			throw e;
		}
	}

}
