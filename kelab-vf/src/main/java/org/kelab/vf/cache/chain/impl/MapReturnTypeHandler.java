package org.kelab.vf.cache.chain.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kelab.util.FastJsonUtil;
import org.kelab.util.model.Pair;
import org.kelab.vf.cache.chain.AbstractReturnTypeHandler;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by hongfei.whf on 2017/3/9.
 * 尚支持一级泛型，即Map<T,K>
 * 不支持Map<T,List<K>>
 */
@Slf4j
public class MapReturnTypeHandler extends AbstractReturnTypeHandler {

	@Override
	public boolean isAccept(@NonNull Method method) {
		Class returnType = method.getReturnType();
		return Map.class.equals(returnType);
	}

	@Override
	public Pair<Class, Class[]> resolve(@NonNull Method method,
	                                    @NonNull Pair<Class, Class> entityAndQuery) {
		Class returnType = method.getReturnType();
		ResolvableType type = ResolvableType.forMethodReturnType(method);
		// Map<K,T> KT的类型，如果entityType==null,说明是无法解析，代表是basedao里面的方法
		Class keyType = type.getGeneric(0).resolve();
		Class valueType = type.getGeneric(1).resolve();
		if (keyType == null) keyType = Integer.class;
		if (valueType == null) valueType = entityAndQuery.getValue1();
		log.info("==> Map:" + keyType.getName() + " - " + valueType.getName());
		return new Pair<>(returnType, new Class[]{keyType, valueType});
	}

	@Override
	public Object deserialize(String json, @NonNull Pair<Class, Class[]> args) {
		Class keyClazz = args.getValue2()[0];// id
		Class valueClazz = args.getValue2()[1]; // entity
		return json == null ? null : FastJsonUtil.json2Map(json, keyClazz, valueClazz);
	}

}
