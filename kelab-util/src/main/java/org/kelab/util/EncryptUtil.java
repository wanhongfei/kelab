package org.kelab.util;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.kelab.util.constant.CharsetConstant;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wwhhf
 * @comment 加密解密
 * @since 2016年6月8日
 */
public class EncryptUtil {

	// 算法名称
	public static final String MD5 = "MD5";
	public static final String SHA1 = "sha1";
	public static final String PBKDF2_SHA256 = "pbkdf2_sha256";

	/**
	 * base 64 加密
	 *
	 * @param message
	 * @return
	 */
	@SneakyThrows
	public static String base64encode(@NonNull String message) {
		return new String(Base64.encodeBase64(message.getBytes(CharsetConstant.UTF_8)),
				CharsetConstant.UTF_8);
	}

	/**
	 * base 64 解密
	 *
	 * @param message
	 * @return
	 */
	@SneakyThrows
	public static String base64decode(@NonNull String message) {
		return new String(Base64.decodeBase64(message.getBytes(CharsetConstant.UTF_8)),
				CharsetConstant.UTF_8);
	}

	/**
	 * @param message
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @author wwhhf
	 * @comment md5加密
	 * @since 2016年6月13日
	 */
	@SneakyThrows
	public static String md5encode(@NonNull String message) {
		return DigestUtils.md5Hex(message);
	}

	/**
	 * @param message
	 * @param salt
	 * @param iterations
	 * @return
	 * @throws UnsupportedEncodingException
	 * @author wwhhf
	 * @comment pbkdf2_sha256 加密
	 * @since 2016年6月8日
	 */
	@SneakyThrows
	public static String pbkdf2_sha256(@NonNull String message, @NonNull String salt, int iterations, @NonNull String sep) {
		PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
		gen.init(message.getBytes(CharsetConstant.UTF_8), salt.getBytes(CharsetConstant.UTF_8), iterations);
		byte[] dk = ((KeyParameter) gen.generateDerivedParameters(256)).getKey();
		byte[] hashBase64 = Base64.encodeBase64(dk);
		String hash = new String(hashBase64, CharsetConstant.UTF_8);
		StringBuffer sb = new StringBuffer(PBKDF2_SHA256).append(sep)
				.append(iterations).append(sep)
				.append(salt).append(sep)
				.append(hash);
		return sb.toString();
	}

	/**
	 * @param message
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @author wwhhf
	 * @comment sha1加密
	 * @since 2016年6月8日
	 */
	@SneakyThrows
	public static String sha1(@NonNull String message, @NonNull String salt, @NonNull String sep) {
		StringBuffer sb = new StringBuffer(SHA1).append(sep).
				append(salt).append(sep);
		MessageDigest md = MessageDigest.getInstance(SHA1);
		md.update(salt.getBytes(CharsetConstant.UTF_8));
		byte[] bytes = md.digest(message.getBytes(CharsetConstant.UTF_8));
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}

	/**
	 * @throws Exception
	 * @author wwhhf
	 * @comment 生成盐值
	 * @since 2016年6月10日
	 */
	@SneakyThrows
	public static String salt(int len) {
		return RandomUtil.randomCertainLenString(len);
	}

}
