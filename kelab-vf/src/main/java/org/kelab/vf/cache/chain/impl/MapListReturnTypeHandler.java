package org.kelab.vf.cache.chain.impl;

import com.alibaba.fastjson.TypeReference;
import org.kelab.util.FastJsonUtil;
import org.kelab.util.model.Pair;
import org.kelab.vf.cache.chain.AbstractReturnTypeHandler;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by hongfei.whf on 2017/3/31.
 */
public class MapListReturnTypeHandler extends AbstractReturnTypeHandler {
	@Override
	public boolean isAccept(Method method) {
		Class returnType = method.getReturnType();
		if (!returnType.equals(Map.class)) {
			return false;
		}
		ResolvableType type = ResolvableType.forMethodReturnType(method);
		// Map<K,T> KT的类型，如果entityType==null,说明是无法解析，代表是basedao里面的方法
		Class valueType = type.getGeneric(1).getRawClass();
		return List.class.equals(valueType);
	}

	@Override
	public Pair<Class, Class[]> resolve(Method method, Pair<Class, Class> entityAndQuery) {
		Class returnType = method.getReturnType();
		ResolvableType type = ResolvableType.forMethodReturnType(method);
		// Map<K,List<T>> KT的类型，如果entityType==null,说明是无法解析，代表是basedao里面的方法
		Class keyType = type.getGeneric(0).resolve();
		Class valueType = type.getGeneric(1, 0).resolve();
		if (keyType == null) keyType = String.class;
		if (valueType == null) valueType = entityAndQuery.getValue1();
		return new Pair<>(returnType, new Class[]{keyType, valueType});
	}

	@Override
	public Object deserialize(String json, Pair<Class, Class[]> args) {
		Class keyClazz = args.getValue2()[0];// id
		Class valueClazz = args.getValue2()[1]; // entity
		return json == null ? null : FastJsonUtil.json2GenericType(json,
				getTypeReference(keyClazz, valueClazz));
	}

	/**
	 * 获取TypeReference
	 *
	 * @param keyClazz
	 * @param listClazz
	 * @param <T>
	 * @param <K>
	 * @return
	 */
	private <T, K> TypeReference getTypeReference(Class<K> keyClazz, Class<T> listClazz) {
		return new TypeReference<Map<K, List<T>>>(keyClazz, listClazz) {
		};
	}
}
