package org.kelab.util;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.kelab.util.constant.CharsetConstant;

import java.io.IOException;

/**
 * @author wwhhf
 * @comment 发送邮件客户端
 * @since 2016年6月12日
 */
public class MailUtil {

	/**
	 * @param addresses
	 * @param subject
	 * @throws IOException
	 * @author wwhhf
	 * @comment 发送邮件(纯文本)
	 * @since 2016年6月12日
	 */
	@SneakyThrows
	public static boolean sendSimpleEmail(@NonNull final String[] addresses, @NonNull final String subject,
	                                      @NonNull final String content) {
		String host = PropertiesUtil.getPropertyByName(MailAttrConstant.MAIL_SMTP_HOST);
		String username = PropertiesUtil.getPropertyByName(MailAttrConstant.MAIL_USER);
		String passwd = PropertiesUtil.getPropertyByName(MailAttrConstant.MAIL_PASSWORD);

		Email email = new SimpleEmail();
		email.setHostName(host);
		email.setAuthenticator(new DefaultAuthenticator(username, passwd));
		email.setSSLOnConnect(true);
		email.setFrom(username);
		email.setContent(content, "text/plain;charset=" + CharsetConstant.UTF_8);
		email.setSubject(subject);
		for (String address : addresses) {
			email.addTo(address);
		}
		email.send();
		return true;
	}

	/**
	 * @param addresses
	 * @param subject
	 * @throws IOException
	 * @author wwhhf
	 * @comment 发送邮件(html)
	 * @since 2016年6月12日
	 */
	@SneakyThrows
	public static boolean sendHtmlEmail(@NonNull final String[] addresses, @NonNull final String subject,
	                                    @NonNull final String content) {
		String host = PropertiesUtil.getPropertyByName(MailAttrConstant.MAIL_SMTP_HOST);
		String username = PropertiesUtil.getPropertyByName(MailAttrConstant.MAIL_USER);
		String passwd = PropertiesUtil.getPropertyByName(MailAttrConstant.MAIL_PASSWORD);


		HtmlEmail email = new HtmlEmail();
		email.setHostName(host);
		email.setAuthenticator(new DefaultAuthenticator(username, passwd));
		email.setSSLOnConnect(true);
		email.setCharset(CharsetConstant.UTF_8);
		email.setFrom(username);
		email.setSubject(subject);
		email.setHtmlMsg(content);
		for (String address : addresses) {
			email.addTo(address);
		}
		email.send();
		return true;
	}

	/**
	 * 邮箱信息
	 */
	public static class MailAttrConstant {

		public static final String MAIL_SMTP_HOST = "mail.smtp.host";
		public static final String MAIL_USER = "mail.user";
		public static final String MAIL_PASSWORD = "mail.password";

	}

}
