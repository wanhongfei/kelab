package org.kelab.test.util;

import org.kelab.util.MailUtil;

public class MailTest {

	public static void main(String[] args) throws Exception {
//        Mail mail = new Mail();
//        mail.setAddress(new String[]{"1272700372@qq.com", "wwhhff11@aliyun.com"});
//        mail.setSubject("test subject");
//        mail.setContent("test content");
//        mail.send();
		MailUtil.sendSimpleEmail(new String[]{"1272700372@qq.com"}, "testsubject", "testcontent");
	}
}
