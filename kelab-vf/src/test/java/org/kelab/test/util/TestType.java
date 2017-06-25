package org.kelab.test.util;

import org.kelab.util.model.Pair;
import org.kelab.vf.dao.impl.BaseDaoImpl;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by hongfei.whf on 2017/3/3.
 */
public class TestType {

	public static void main(String[] args) throws Exception {
//		Field f = TestType.class.getDeclaredField("map");
//		System.out.println(f.getGenericType());                               // java.util.Map<java.lang.String, java.lang.String>
//		System.out.println(f.getGenericType() instanceof ParameterizedType);  // true
//		ParameterizedType pType = (ParameterizedType) f.getGenericType();
//		System.out.println(pType.getRawType());                               // interface java.util.Map
//		for (Type type : pType.getActualTypeArguments()) {
//			System.out.println(type);                                         // 打印两遍: class java.lang.String
//		}
//		System.out.println(pType.getOwnerType());                             // null
//		System.out.println(getEntityAndQuery(ProblemSubmitDaoImpl.class));
//		ResolvableType[] types = type.getGenerics();
//		for (ResolvableType type1 : types) {
//			Class clazz = type1.resolve();
//			System.out.println(clazz.getName());
//		}
	}

	/**
	 * 获取BaseDao的<T,Q>
	 *
	 * @param clazz
	 * @return
	 */
	private static Pair<Class, Class> getEntityAndQuery(Class<? extends BaseDaoImpl> clazz) {
		ResolvableType type = ResolvableType.forClass(clazz);
		Class entityType = type.as(BaseDaoImpl.class).getGeneric(0).resolve();
		Class queryType = type.as(BaseDaoImpl.class).getGeneric(1).resolve();
		return new Pair<>(entityType, queryType);
	}

	/**
	 * 获取BaseDao的<T,Q>
	 *
	 * @param method
	 * @return
	 */
	private static void getMethodEntityAndQuery(Method method) {
		System.out.println(method.getReturnType());
		Class returnType = method.getReturnType();
		ResolvableType type = ResolvableType.forMethodReturnType(method);
		if (returnType == List.class) {
			Class entityType = type.getGeneric(0).resolve();
			System.out.println(returnType.getName() + " " + entityType.getName());
		} else if (returnType == String.class) {
			System.out.println(String.class);
		} else if (returnType == Map.class) {
			Class key = type.getGeneric(0).resolve();
			Class value = type.getGeneric(0).resolve();
			System.out.println(returnType.getName() + " " + key.getName() + "-" + value.getName());
		} else {
			System.out.println(returnType.getClass());
		}
	}
}