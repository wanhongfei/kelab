package org.kelab.vf.validator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.kelab.util.BeanUtil;
import org.kelab.util.StringUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
@Data
public class Validator<T> {

	/**
	 * 验证器handler处理器
	 */
	private final List<ValidatorHandler<T>> handlers = new LinkedList<>();

	/**
	 * 待校验的对象
	 */
	private Object obj;

	/**
	 * 属性名称
	 */
	private String fieldName;

	/**
	 * 属性类型
	 */
	private Class<T> clazz;

	/**
	 * 获取实例
	 *
	 * @return
	 */
	public static <T> Validator<T> getInstance(Object obj, String fieldName, Class<T> clazz) {
		Validator<T> validator = new Validator<T>(obj, fieldName, clazz);
		return validator;
	}

	/**
	 * 验证操作
	 */
	@SneakyThrows
	public void vaild() {
		T t = (T) BeanUtil.getFieldValueByFieldNamesStr(obj, fieldName);
		for (ValidatorHandler handler : handlers) {
			if (!handler.vaild(t)) {
				throw new Exception(StringUtil.format("%s in %s vaild fail : %s",
						fieldName,
						clazz.getCanonicalName(),
						handler.failMsg()));
			}
		}
	}

	/**
	 * 加入验证handler
	 *
	 * @param handler
	 * @return
	 */
	public Validator<T> next(@NonNull ValidatorHandler<T> handler) {
		handlers.add(handler);
		return this;
	}

}
