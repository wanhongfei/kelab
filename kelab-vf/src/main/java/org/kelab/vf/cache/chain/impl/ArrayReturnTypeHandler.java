package org.kelab.vf.cache.chain.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kelab.util.FastJsonUtil;
import org.kelab.util.model.Pair;
import org.kelab.vf.cache.chain.AbstractReturnTypeHandler;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Method;

/**
 * Created by hongfei.whf on 2017/3/9.
 */
@Slf4j
public class ArrayReturnTypeHandler extends AbstractReturnTypeHandler {

	@Override
	public boolean isAccept(@NonNull Method method) {
		Class returnType = method.getReturnType();
		return returnType.isArray();
	}

	@Override
	public Pair<Class, Class[]> resolve(@NonNull Method method,
	                                    @NonNull Pair<Class, Class> entityAndQuery) {
		Class returnType = method.getReturnType();
		ResolvableType type = ResolvableType.forMethodReturnType(method);
		// T[] T的类型，如果entityType==null,说明是无法解析，代表是basedao里面的方法
		Class entityType = type.getComponentType().resolve();
		if (entityType == null) entityType = entityAndQuery.getValue1();
		log.info("==> Array:" + entityType.getName());
		return new Pair<>(returnType, new Class[]{entityType});
	}

	@Override
	public Object deserialize(String json, @NonNull Pair<Class, Class[]> args) {
		Class clazz = args.getValue2()[0];
		return json == null ? null : FastJsonUtil.json2Array(json, clazz);
	}
}
