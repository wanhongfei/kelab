package org.kelab.vf.cache.chain.impl;

import com.alibaba.fastjson.TypeReference;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kelab.util.FastJsonUtil;
import org.kelab.util.model.Pair;
import org.kelab.vf.cache.chain.AbstractReturnTypeHandler;
import org.kelab.vf.result.PaginationResult;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Method;

/**
 * Created by hongfei.whf on 2017/3/9.
 */
@Slf4j
public class PRReturnTypeHandler extends AbstractReturnTypeHandler {

	@Override
	public boolean isAccept(@NonNull Method method) {
		Class returnType = method.getReturnType();
		return returnType.equals(PaginationResult.class);
	}

	@Override
	public Pair<Class, Class[]> resolve(@NonNull Method method,
	                                    @NonNull Pair<Class, Class> entityAndQuery) {
		Class returnType = method.getReturnType();
		ResolvableType type = ResolvableType.forMethodReturnType(method);
		// List<T> T的类型，如果entityType==null,说明是无法解析，代表是basedao里面的方法
		Class entityType = type.getGeneric(0).resolve();
		if (entityType == null) entityType = entityAndQuery.getValue1();
		log.info("==> PaginationResult:" + entityType.getName());
		return new Pair<>(returnType, new Class[]{entityType});
	}

	@Override
	public Object deserialize(String json, @NonNull Pair<Class, Class[]> args) {
		// PaginationResult
		Class clazz = args.getValue2()[0];
		return getPaginationResult(json, clazz);
	}

	/**
	 * 自定义泛型实体类获取
	 *
	 * @param json
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	private <T> PaginationResult<T> getPaginationResult(String json, @NonNull Class<T> clazz) {
		return json == null ? null : FastJsonUtil.json2GenericType(json,
				new TypeReference<PaginationResult<T>>(clazz) {
				});
	}
}
