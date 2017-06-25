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
public class DefaultReturnTypeHandler extends AbstractReturnTypeHandler {

	@Override
	public boolean isAccept(@NonNull Method method) {
		return true;
	}

	@Override
	public Pair<Class, Class[]> resolve(@NonNull Method method,
	                                    @NonNull Pair<Class, Class> entityAndQuery) {
		Class returnType = method.getReturnType();
		ResolvableType type = ResolvableType.forMethodReturnType(method);
		// T的类型，如果entityType==null,说明是无法解析，代表是basedao里面的方法
		Class entityType = type.resolve();
		if (entityType == null) {
			// 是泛型无法转换，即 T
			entityType = entityAndQuery.getValue1();
		}
		log.info("==> Object:" + entityType.getName());
		return new Pair<>(entityType, new Class[]{});
	}

	@Override
	public Object deserialize(String json, @NonNull Pair<Class, Class[]> args) {
		Class clazz = args.getValue1();
		return json == null ? null : FastJsonUtil.json2Object(json, clazz);
	}
}
