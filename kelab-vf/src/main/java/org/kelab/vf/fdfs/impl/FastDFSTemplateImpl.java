package org.kelab.vf.fdfs.impl;

import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.kelab.util.FileUtil;
import org.kelab.util.constant.SeparatorConstant;
import org.kelab.vf.fdfs.IFastDFSTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongfei.whf on 2017/2/27.
 */
@Slf4j
public class FastDFSTemplateImpl implements IFastDFSTemplate {

	public static final String FDFS_CLIENT_CONF = "fdfs_client.conf";

	@Setter
	private String defaultGroupName = null;

	@Override
	@SneakyThrows
	public void init() {
		File conf = FileUtil.classpathFile(FDFS_CLIENT_CONF);
		ClientGlobal.init(conf.getCanonicalPath());
	}

	@Override
	public String uploadAndRetUrl(File file) {
		String[] arr = upload(file);
		return SeparatorConstant.BACK_SLANT + arr[0] + SeparatorConstant.BACK_SLANT + arr[1];
	}

	@Override
	@SneakyThrows
	public String[] upload(@NonNull File file) {
		return upload(defaultGroupName, file);
	}

	@Override
	@SneakyThrows
	public String[] upload(@NonNull String groupName, @NonNull File file) {
		long fileLength = file.length();
		String fileName = file.getName();
		int pos = fileName.lastIndexOf(SeparatorConstant.PERIOD);
		String extName = fileName.substring(pos + 1);
		byte[] buff = FileUtil.file2ByteArray(file);

		log.info("filename is :" + fileName);
		log.info("extName is :" + extName);
		log.info("fileLength is :" + fileLength);

		// 建立连接
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);

		// 设置元信息
		NameValuePair[] metaList = new NameValuePair[3];
		metaList[0] = new NameValuePair("fileName", fileName);
		metaList[1] = new NameValuePair("fileExtName", extName);
		metaList[2] = new NameValuePair("fileLength", String.valueOf(fileLength));

		// 上传文件
		String[] res = null;
		try {
			res = storageClient.upload_file(groupName, buff, extName, metaList);
		} finally {
			trackerServer.close();
		}
		return res;
	}

	@Override
	public List<String[]> upload(@NonNull File[] files) {
		List<String[]> res = new ArrayList<>();
		for (File file : files) {
			res.add(upload(file));
		}
		return res;
	}

	@Override
	public List<String[]> upload(@NonNull String groupName, @NonNull File[] files) {
		List<String[]> res = new ArrayList<>();
		for (File file : files) {
			res.add(upload(groupName, file));
		}
		return res;
	}

	@Override
	@SneakyThrows
	public void download(@NonNull String groupName, @NonNull String remote_filename, @NonNull String local_filename) {
		// 建立连接
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);

		try {
			byte[] buff = storageClient.download_file(groupName, remote_filename);
			System.out.println(buff);
			FileUtil.byteArray2File(buff, local_filename);
		} finally {
			trackerServer.close();
		}
	}

	@Override
	@SneakyThrows
	public boolean delete(@NonNull String groupName, @NonNull String remote_filename) {
		// 建立连接
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);

		try {
			// 删除文件
			int i = storageClient.delete_file(groupName, remote_filename);
			return i == 0 ? true : false;
		} finally {
			trackerServer.close();
		}
	}

	@Override
	@SneakyThrows
	public NameValuePair[] fileMeta(@NonNull String groupName, @NonNull String remote_filename) {
		// 建立连接
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);

		try {
			NameValuePair meta[] = storageClient.get_metadata(groupName, remote_filename);
			return meta;
		} finally {
			trackerServer.close();
		}
	}

	@Override
	@SneakyThrows
	public FileInfo fileInfo(@NonNull String groupName, @NonNull String remote_filename) {
		// 建立连接
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);

		try {
			FileInfo fi = storageClient.get_file_info(groupName, remote_filename);
			return fi;
		} finally {
			trackerServer.close();
		}
	}
}
