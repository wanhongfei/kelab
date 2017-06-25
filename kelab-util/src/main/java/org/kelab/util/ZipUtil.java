package org.kelab.util;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wwhhf
 * @comment zip压缩工具
 * @since 2016年6月13日
 */
@Slf4j
public class ZipUtil {

	/**
	 * zip压缩文件
	 *
	 * @param dir
	 * @param zippath
	 */
	public static void zip(@NonNull String dir, @NonNull String zippath) {
		File file = new File(dir);
		if (file.isDirectory()) {
			List<String> paths = getFiles(dir);
			compressFilesZip(paths.toArray(new String[paths.size()]), zippath, dir);
		} else {
			compressFilesZip(new File[]{file}, zippath);
		}
	}

	/**
	 * zip压缩文件
	 *
	 * @param filepaths
	 * @param zippath
	 */
	public static void zip(@NonNull List<File> filepaths, @NonNull String zippath) {
		File[] files = new File[filepaths.size()];
		filepaths.toArray(files);
		compressFilesZip(files, zippath);
	}

	/**
	 * zip压缩文件
	 *
	 * @param files
	 * @param zippath
	 */
	public static void zip(@NonNull String zippath, File... files) {
		compressFilesZip(files, zippath);
	}

	/**
	 * 递归取到当前目录所有文件
	 *
	 * @param dir
	 * @return
	 */
	private static List<String> getFiles(String dir) {
		List<String> lstFiles = null;
		if (lstFiles == null) {
			lstFiles = new ArrayList<String>();
		}
		File file = new File(dir);
		File[] files = file.listFiles();
		if (files == null || files.length == 0) {
			return CollectionUtil.emptyList;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				lstFiles.add(f.getAbsolutePath());
				lstFiles.addAll(getFiles(f.getAbsolutePath()));
			} else {
				String str = f.getAbsolutePath();
				lstFiles.add(str);
			}
		}
		return lstFiles;
	}

	/**
	 * 文件名处理
	 *
	 * @param dir
	 * @param path
	 * @return
	 */
	private static String getFilePathName(String dir, String path) {
		String p = path.replace(dir + File.separator, "");
		p = p.replace("\\", "/");
		return p;
	}

	/**
	 * 把文件压缩成zip格式
	 *
	 * @param files       需要压缩的文件
	 * @param zipFilePath 压缩后的zip文件路径   ,如"D:/test/aa.zip";
	 */
	private static void compressFilesZip(String[] files, String zipFilePath, String dir) {
		if (files == null || files.length <= 0) {
			return;
		}
		ZipArchiveOutputStream zaos = null;
		try {
			File zipFile = new File(zipFilePath);
			zaos = new ZipArchiveOutputStream(zipFile);
			zaos.setUseZip64(Zip64Mode.AsNeeded);
			//将每个文件用ZipArchiveEntry封装
			//再用ZipArchiveOutputStream写到压缩文件中
			for (String strfile : files) {
				File file = new File(strfile);
				if (file != null) {
					String name = getFilePathName(dir, strfile);
					ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, name);
					zaos.putArchiveEntry(zipArchiveEntry);
					if (file.isDirectory()) {
						continue;
					}
					InputStream is = null;
					try {
						is = new BufferedInputStream(new FileInputStream(file));
						byte[] buffer = new byte[1024];
						int len = -1;
						while ((len = is.read(buffer)) != -1) {
							//把缓冲区的字节写入到ZipArchiveEntry
							zaos.write(buffer, 0, len);
						}
						zaos.closeArchiveEntry();
					} catch (Exception e) {
						log.error("compressFilesZip Exception 1:{}", zipFilePath);
						throw new RuntimeException(e);
					} finally {
						IOUtils.closeQuietly(is);
					}
				}
			}
			zaos.finish();
		} catch (Exception e) {
			log.error("compressFilesZip Exception 2:{}", zipFilePath);
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(zaos);
		}
	}

	/**
	 * 把zip文件解压到指定的文件夹
	 *
	 * @param zipFilePath zip文件路径, 如 "D:/test/aa.zip"
	 * @param saveFileDir 解压后的文件存放路径, 如"D:/test/" ()
	 */
	@SneakyThrows
	public static void unzip(@NonNull String zipFilePath, @NonNull String saveFileDir) {
		if (!saveFileDir.endsWith("\\") && !saveFileDir.endsWith("/")) {
			saveFileDir += File.separator;
		}
		File dir = new File(saveFileDir);
		if (!dir.exists()) {
			boolean isSucc = dir.mkdirs();
			if (!isSucc) throw new Exception("mkdir dir unsuccessfully.");
		}
		File file = new File(zipFilePath);
		if (file.exists()) {
			InputStream is = null;
			ZipArchiveInputStream zais = null;
			try {
				is = new FileInputStream(file);
				zais = new ZipArchiveInputStream(is);
				ArchiveEntry archiveEntry = null;
				while ((archiveEntry = zais.getNextEntry()) != null) {
					// 获取文件名
					String entryFileName = archiveEntry.getName();
					// 构造解压出来的文件存放路径
					String entryFilePath = saveFileDir + entryFileName;
					OutputStream os = null;
					try {
						// 把解压出来的文件写到指定路径
						File entryFile = new File(entryFilePath);
						if (entryFileName.endsWith("/")) {
							boolean isSucc = entryFile.mkdirs();
							if (!isSucc) throw new Exception("mkdir dir error.");
						} else {
							os = new BufferedOutputStream(new FileOutputStream(
									entryFile));
							byte[] buffer = new byte[1024];
							int len = -1;
							while ((len = zais.read(buffer)) != -1) {
								os.write(buffer, 0, len);
							}
						}
					} catch (IOException e) {
						log.error("unzip IOException:{},{}", zipFilePath, saveFileDir);
						throw new IOException(e);
					} finally {
						IOUtils.closeQuietly(os);
					}
				}
			} catch (Exception e) {
				log.error("unzip Exception:{},{}", zipFilePath, saveFileDir);
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(zais);
				IOUtils.closeQuietly(is);
			}
		}
	}

	/**
	 * 把文件压缩成zip格式
	 *
	 * @param files       需要压缩的文件
	 * @param zipFilePath 压缩后的zip文件路径   ,如"D:/test/aa.zip";
	 */
	private static void compressFilesZip(File[] files, String zipFilePath) {
		if (files != null && files.length > 0) {
			if (isEndsWithZip(zipFilePath)) {
				ZipArchiveOutputStream zaos = null;
				try {
					File zipFile = new File(zipFilePath);
					zaos = new ZipArchiveOutputStream(zipFile);
					//Use Zip64 extensions for all entries where they are required
					zaos.setUseZip64(Zip64Mode.AsNeeded);
					//将每个文件用ZipArchiveEntry封装
					//再用ZipArchiveOutputStream写到压缩文件中
					for (File file : files) {
						if (file != null) {
							ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
							zaos.putArchiveEntry(zipArchiveEntry);
							InputStream is = null;
							try {
								is = new BufferedInputStream(new FileInputStream(file));
								byte[] buffer = new byte[1024 * 5];
								int len = -1;
								while ((len = is.read(buffer)) != -1) {
									//把缓冲区的字节写入到ZipArchiveEntry
									zaos.write(buffer, 0, len);
								}
								//Writes all necessary data for this entry.
								zaos.closeArchiveEntry();
							} catch (Exception e) {
								log.error("compressFilesZip Error 1:{}", file.getCanonicalPath());
								throw new RuntimeException(e);
							} finally {
								IOUtils.closeQuietly(is);
								if (is != null)
									is.close();
							}
						}
					}
					zaos.finish();
				} catch (Exception e) {
					log.error("compressFilesZip Error 2:{}", zipFilePath);
					throw new RuntimeException(e);
				} finally {
					IOUtils.closeQuietly(zaos);
				}
			}
		}
	}

	/**
	 * 判断文件名是否以.zip为后缀
	 *
	 * @param fileName 需要判断的文件名
	 * @return 是zip文件返回true, 否则返回false
	 */
	private static boolean isEndsWithZip(String fileName) {
		boolean flag = false;
		if (fileName != null && !"".equals(fileName.trim())) {
			if (fileName.endsWith(".ZIP") || fileName.endsWith(".zip")) {
				flag = true;
			}
		}
		return flag;
	}

}