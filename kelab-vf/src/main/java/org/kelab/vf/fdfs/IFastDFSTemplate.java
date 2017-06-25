package org.kelab.vf.fdfs;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;

import java.io.File;
import java.util.List;

/**
 * Created by hongfei.whf on 2017/2/27.
 */
public interface IFastDFSTemplate {

	/**
	 * 初始化
	 */
	void init();

	/**
	 * 上传单文件
	 *
	 * @param file
	 * @return
	 */
	String uploadAndRetUrl(File file);

	/**
	 * 上传单文件
	 *
	 * @param file
	 * @return
	 */
	String[] upload(File file);

	/**
	 * 上传单文件
	 *
	 * @param file
	 * @return
	 */
	String[] upload(String groupName, File file);

	/**
	 * 上传单文件
	 *
	 * @param files
	 * @return
	 */
	List<String[]> upload(File[] files);

	/**
	 * 上传单文件
	 *
	 * @param files
	 * @return
	 */
	List<String[]> upload(String groupName, File[] files);

	/**
	 * 下载单文件
	 *
	 * @param groupName
	 * @param remote_filename
	 * @param local_filename
	 */
	void download(String groupName, String remote_filename, String local_filename);

	/**
	 * 删除单文件
	 *
	 * @param groupName
	 * @param remote_filename
	 */
	boolean delete(String groupName, String remote_filename);

	/**
	 * 获取元信息
	 *
	 * @param groupName
	 * @param remote_filename
	 * @return
	 */
	NameValuePair[] fileMeta(String groupName, String remote_filename);

	/**
	 * 文件信息
	 *
	 * @param groupName
	 * @param remote_filename
	 * @return
	 */
	FileInfo fileInfo(String groupName, String remote_filename);
}
