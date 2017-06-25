package org.kelab.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Json 序列化和反序列化工具包
 * https://github.com/alibaba/fastjson/wiki/TypeReference
 */
public class FastJsonUtil {

	/**
	 * 选项:
	 * QuoteFieldNames———-输出key时是否使用双引号,默认为true
	 * WriteMapNullValue——–是否输出值为null的字段,默认为false
	 * WriteNullNumberAsZero—-数值字段如果为null,输出为0,而非null
	 * WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null
	 * WriteNullStringAsEmpty—字符类型字段如果为null,输出为"",而非null
	 * WriteNullBooleanAsFalse–Boolean字段如果为null,输出为false,而非null
	 * PrettyFormat - 代码整理
	 */
	private static final SerializerFeature features[] = {
			SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteDateUseDateFormat
	};
	private static final SerializerFeature file_features[] = {
			SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteDateUseDateFormat,
			SerializerFeature.PrettyFormat
	};

	/**
	 * Fastjson支持继承关系的序列和反序列
	 * 但是对于反序列对象类型为Object.class，其返回的是JsonObject，无法直接强转至其他对象，
	 * 所以，我们应当避免使用Object作为FastJson反序列的类型
	 * json转为非Object对象
	 * 涉及要注意泛型
	 *
	 * @param alibabaJson
	 * @param clazz
	 * @return
	 */
	public static <T> T json2Object(@NonNull String alibabaJson, @NonNull Class<T> clazz) {
		return JSON.parseObject(alibabaJson, clazz);
	}

	/**
	 * json -> array
	 *
	 * @param alibabaJson
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T[] json2Array(@NonNull String alibabaJson, @NonNull Class<T> clazz) {
		List<T> list = json2List(alibabaJson, clazz);
		return CollectionUtil.list2Array(list, clazz);
	}

	/**
	 * json to map
	 *
	 * @param alibabaJson
	 * @return
	 */
	public static <K, V> Map<K, V> json2Map(@NonNull String alibabaJson,
	                                        @NonNull Class<K> keyClz, @NonNull Class<V> valueClz) {
		Map<K, V> map = JSON.parseObject(alibabaJson, new TypeReference<Map<K, V>>(keyClz, valueClz) {
		});
		return map;
	}

	/**
	 * @param alibabaJson
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> json2List(@NonNull String alibabaJson, @NonNull Class<T> clazz) {
		List<T> list = JSON.parseObject(alibabaJson, new TypeReference<List<T>>(clazz) {
		});
		return list;
	}

	/**
	 * 自定义泛型实体类转换
	 *
	 * @param alibabaJson
	 * @param typeReference
	 * @return
	 */
	public static <T> T json2GenericType(@NonNull String alibabaJson,
	                                     @NonNull TypeReference<T> typeReference) {
		T t = JSON.parseObject(alibabaJson, typeReference);
		return t;
	}

	/**
	 * @param obj
	 * @return
	 * @author wwhhf
	 * @comment object转为json
	 * @since 2016年6月13日
	 */
	public static String object2Json(@NonNull Object obj) {
		return JSON.toJSONString(obj);
	}

	/**
	 * @param obj
	 * @param isNullShow
	 * @return
	 * @author wwhhf
	 * @comment object转为json
	 * @since 2016年6月13日
	 */
	public static String object2Json(@NonNull Object obj, boolean isNullShow) {
		if (isNullShow == false) {
			return JSON.toJSONString(obj);
		} else {
			return JSON.toJSONString(obj, features);
		}
	}

	/**
	 * list->JsonList
	 *
	 * @param list
	 * @return
	 */
	public static List<String> list2JsonList(@NonNull List<Object> list) {
		return list2JsonList(list, false);
	}

	/**
	 * list->JsonList
	 *
	 * @param list
	 * @param isNullShow
	 * @return
	 */
	public static List<String> list2JsonList(@NonNull List<Object> list, boolean isNullShow) {
		List<String> res = new ArrayList<>();
		for (Object obj : list) {
			res.add(object2Json(obj, isNullShow));
		}
		return res;
	}

	/**
	 * map->JsonMap
	 *
	 * @param map
	 * @param isNullShow
	 * @return
	 */
	public static <T> Map<T, String> map2JsonMap(@NonNull Map<T, Object> map, boolean isNullShow) {
		Map<T, String> res = new HashMap<>();
		for (Map.Entry<T, Object> entry : map.entrySet()) {
			res.put(entry.getKey(), object2Json(entry.getValue(), isNullShow));
		}
		return res;
	}

	/**
	 * map->JsonMap
	 *
	 * @param map
	 * @return
	 */
	public static <T> Map<T, String> map2JsonMap(@NonNull Map<T, Object> map) {
		return map2JsonMap(map, false);
	}

	/**
	 * obj->json and write into os
	 *
	 * @param obj
	 * @param os
	 */
	@SneakyThrows
	public static void jsonObject2OutputStream(@NonNull Object obj, @NonNull OutputStream os) {
		JSON.writeJSONString(os, obj, file_features);
	}

	/**
	 * obj->json and write into file
	 *
	 * @param obj
	 * @param file
	 */
	@SneakyThrows
	public static void jsonObject2File(@NonNull Object obj, @NonNull File file) {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(FileUtil.file2OutputStream(file));
			JSON.writeJSONString(bos, obj, file_features);
			bos.flush();
		} finally {
			bos.close();
		}
	}

	/**
	 * obj->json and write into file
	 *
	 * @param obj
	 * @param filePath
	 */
	@SneakyThrows
	public static void jsonObject2File(@NonNull Object obj, @NonNull String filePath) {
		jsonObject2File(obj, new File(filePath));
	}
}
