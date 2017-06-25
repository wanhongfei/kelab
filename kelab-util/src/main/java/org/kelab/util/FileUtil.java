package org.kelab.util;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author wwhhf
 * @comment 文件工具类
 * @since 2016年6月13日
 */
public class FileUtil {

	/**
	 * 从classpath获取file(jar内资源文件无法读取)
	 *
	 * @param filePath
	 */
	@SneakyThrows
	public static File classpathFile(@NonNull String filePath) {
		ClassPathResource resource = new ClassPathResource(filePath);
		return resource.getFile();
	}

	/**
	 * 从classpath获取file的内容，包括Jar的资源文件
	 *
	 * @param filePath
	 */
	@SneakyThrows
	public static String readResource(@NonNull String filePath) {
		ClassPathResource resource = new ClassPathResource(filePath);
		@Cleanup InputStream is = resource.getInputStream();
		return inputStream2String(is);
	}

	/**
	 * 从本机获取文件
	 *
	 * @param path
	 * @return
	 */
	public static File getFile(@NonNull String path) {
		return FileUtils.getFile(path);
	}

	/**
	 * 从classpath获取file的输入流
	 *
	 * @param filePath
	 */
	@SneakyThrows
	public static InputStream classpathFileAsInputStream(@NonNull String filePath) {
		ClassPathResource resource = new ClassPathResource(filePath);
		return resource.getInputStream();
	}

	/**
	 * @param bytes
	 * @param filePath
	 * @throws IOException
	 * @author wwhhf
	 * @comment 比特数组转为文件
	 * @since 2016年6月13日
	 */
	@SneakyThrows
	public static void byteArray2File(@NonNull byte[] bytes, @NonNull String filePath) {
		File file = new File(filePath);
		@Cleanup InputStream is = new ByteArrayInputStream(bytes);
		FileUtils.copyInputStreamToFile(is, file);
	}

	/**
	 * 将MultipartFile->file
	 *
	 * @param file
	 * @return
	 */
	@SneakyThrows
	public static File convert(@NonNull MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		@Cleanup FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		return convFile;
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 * @author wwhhf
	 * @comment 文件转为byte数组
	 * @since 2016年6月13日
	 */
	@SneakyThrows
	public static byte[] file2ByteArray(@NonNull File file) {
		return FileUtils.readFileToByteArray(file);
	}

	/**
	 * 写入文本文件
	 *
	 * @param content
	 * @param filePath
	 * @param isAppend
	 */
	@SneakyThrows
	public static void writeFile(@NonNull String content, @NonNull String filePath,
	                             boolean isAppend) {
		File file = new File(filePath);
		FileUtils.write(file, content, isAppend);
	}

	/**
	 * inputStream->String
	 *
	 * @param is
	 * @return
	 */
	@SneakyThrows
	public static String inputStream2String(InputStream is) {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line).append("\n");
		}
		return buffer.toString();
	}

	/**
	 * 写入文本文件
	 *
	 * @param content
	 * @param filePath
	 */
	@SneakyThrows
	public static void writeFile(@NonNull String content, @NonNull String filePath) {
		writeFile(content, filePath, true);
	}

	/**
	 * 删除文件
	 *
	 * @param filePath
	 */
	public static void deleteFile(@NonNull String filePath) {
		File file = new File(filePath);
		FileUtils.deleteQuietly(file);
	}

	/**
	 * 读取文本文件内容
	 *
	 * @param filePath
	 * @return
	 */
	@SneakyThrows
	public static String readFile(@NonNull String filePath) {
		return FileUtils.readFileToString(new File(filePath));
	}

	/**
	 * 读取文本文件内容
	 *
	 * @param file
	 * @return
	 */
	@SneakyThrows
	public static String readFile(@NonNull File file) {
		return FileUtils.readFileToString(file);
	}

	/**
	 * file->inputStream
	 *
	 * @param file
	 * @return
	 */
	@SneakyThrows
	public static InputStream file2InputStream(@NonNull File file) {
		return FileUtils.openInputStream(file);
	}

	/**
	 * file->outputStream
	 *
	 * @param file
	 * @return
	 */
	@SneakyThrows
	public static OutputStream file2OutputStream(@NonNull File file) {
		return FileUtils.openOutputStream(file);
	}

}
