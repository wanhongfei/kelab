package org.kelab.vf.cache.chain;

import lombok.NonNull;
import org.kelab.util.FastJsonUtil;
import org.kelab.util.model.Pair;

import java.lang.reflect.Method;

/**
 * Created by hongfei.whf on 2017/3/9.
 */
public abstract class AbstractReturnTypeHandler {

	/**
	 * 是否符合处理的类型
	 *
	 * @param method
	 * @return
	 */
	public abstract boolean isAccept(Method method);

	/**
	 * 解析类型
	 * 1.ReturnType:
	 * List、Map、Array、PaginationResult等等
	 * 2.GenericTypes:
	 * ReturnType里的泛型参数
	 *
	 * @param method
	 * @param entityAndQuery
	 * @return
	 */
	public abstract Pair<Class, Class[]> resolve(Method method, Pair<Class, Class> entityAndQuery);

	/**
	 * 序列化成json格式
	 */
	public String serialize(@NonNull Object obj) {
		return FastJsonUtil.object2Json(obj);
	}

	/**
	 * 反序列化成对应类型
	 *
	 * @param json
	 * @param args
	 * @return
	 */
	public abstract Object deserialize(String json, Pair<Class, Class[]> args);
}
