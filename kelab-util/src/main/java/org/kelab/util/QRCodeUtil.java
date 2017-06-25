package org.kelab.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.kelab.util.constant.CharsetConstant;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 利用zxing生成二维码
 * Created by hongfei.whf on 2017/5/15.
 */
public class QRCodeUtil {

	public static final String PNG = "png";
	public static final String JPGE = "jpge";
	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xFFFFFFFF;

	/**
	 * 生成二维码，写入本地文件中
	 *
	 * @param map
	 * @param width
	 * @param height
	 */
	@SneakyThrows
	public static void encode(@NonNull Map<String, Object> map, int width, int height,
	                          @NonNull String suffix, @NonNull String dir, @NonNull String filename) {
		HashMap<EncodeHintType, String> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, CharsetConstant.UTF_8);
		String json = FastJsonUtil.object2Json(map);
		BitMatrix matrix = new MultiFormatWriter().encode(json, BarcodeFormat.QR_CODE,
				width, height);
		BufferedImage image = bitMatrix2BufferedImage(matrix);
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		ImageIO.write(image, suffix, new File(dir + File.separator + filename));
	}

	/**
	 * 生成二维码，写入输出流中
	 *
	 * @param map
	 * @param width
	 * @param height
	 */
	@SneakyThrows
	public static void encode(@NonNull Map<String, Object> map, int width, int height,
	                          @NonNull String suffix, @NonNull OutputStream os) {
		HashMap<EncodeHintType, String> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, CharsetConstant.UTF_8);
		String json = FastJsonUtil.object2Json(map);
		BitMatrix matrix = new MultiFormatWriter().encode(json, BarcodeFormat.QR_CODE,
				width, height);
		BufferedImage image = bitMatrix2BufferedImage(matrix);
		ImageIO.write(image, suffix, os);
	}

	/**
	 * 二维码解析
	 */
	@SneakyThrows
	public static Map<String, Object> decode(@NonNull File file) {
		HashMap<DecodeHintType, String> hints = new HashMap<>();
		hints.put(DecodeHintType.CHARACTER_SET, CharsetConstant.UTF_8);
		BufferedImage image = ImageIO.read(file);
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		Binarizer binarizer = new HybridBinarizer(source);
		BinaryBitmap bitmap = new BinaryBitmap(binarizer);
		Result result = new MultiFormatReader().decode(bitmap, hints);
		String json = result.getText();
		return FastJsonUtil.json2Map(json, String.class, Object.class);
	}

	/**
	 * 将BitMatrix->BufferedImage
	 *
	 * @param matrix
	 * @return
	 */
	private static BufferedImage bitMatrix2BufferedImage(@NonNull BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
}
