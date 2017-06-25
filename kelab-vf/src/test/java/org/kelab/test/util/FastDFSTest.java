package org.kelab.test.util;

import org.junit.Test;
import org.kelab.vf.fdfs.IFastDFSTemplate;
import org.kelab.vf.junit.JunitBaseServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.io.File;

/**
 * Created by hongfei.whf on 2017/2/27.
 */
@ContextConfiguration(locations = {"classpath*:dispatcher-servlet.xml"})
@TransactionConfiguration(defaultRollback = true)
public class FastDFSTest extends JunitBaseServiceDao {

	@Autowired
	private IFastDFSTemplate iFastDFSTemplate;

	@Test
	public void testupload() {
		File file = new File("E:\\tmp\\client1.conf");
		String[] url = iFastDFSTemplate.upload("group1", file);
		for (int i = 0; i < url.length; i++) {
			System.out.println(url[i]);
		}
	}

	@Test
	public void download() {
		iFastDFSTemplate.download("group1",
				"M01/00/00/wKgAG1i05luAe_9hAAAFtF0CZM484.conf",
				"E:\\tmp\\client1.conf213");
	}

	@Test
	public void delete() {
		System.out.println(iFastDFSTemplate.delete("group1",
				"M01/00/00/wKgAG1i05luAe_9hAAAFtF0CZM484.conf"));
	}

	@Test
	public void info() {
		System.out.println(iFastDFSTemplate.fileMeta("group1",
				"M01/00/00/wKgAG1i05luAe_9hAAAFtF0CZM484.conf"));
		System.out.println(iFastDFSTemplate.fileInfo("group1",
				"M01/00/00/wKgAG1i05luAe_9hAAAFtF0CZM484.conf"));
	}

}
