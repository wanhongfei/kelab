package org.kelab.util;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author wwhhf
 * @comment 数组工具
 * @since 2016年6月13日
 */
public class CollectionUtil {

	/**
	 * 特殊集合
	 *
	 * @return
	 */
	public static final Map emptyMap = Collections.EMPTY_MAP;
	public static final Set emptySet = Collections.EMPTY_SET;
	public static final List emptyList = Collections.EMPTY_LIST;

	/**
	 * 判断集合是否为空
	 *
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 集合求和
	 *
	 * @param list
	 * @return
	 */
	public static Integer sum(@NonNull List<Integer> list, int initValue) {
		Integer sum = initValue;
		for (Integer i : list) {
			sum += i;
		}
		return sum;
	}

	public static Long sum(@NonNull List<Long> list, long initValue) {
		Long sum = initValue;
		for (Long i : list) {
			sum += i;
		}
		return sum;
	}

	public static Float sum(@NonNull List<Float> list, float initValue) {
		Float sum = initValue;
		for (Float i : list) {
			sum += i;
		}
		return sum;
	}

	public static Double sum(@NonNull List<Double> list, double initValue) {
		Double sum = initValue;
		for (Double i : list) {
			sum += i;
		}
		return sum;
	}

	/**
	 * @param ids
	 * @param symbol
	 * @return
	 * @author wwhhf
	 * @comment 字符串转为整形list
	 * @since 2016年6月13日
	 */
	public static List<Integer> string2IntList(String ids, @NonNull String symbol) {
		if (StringUtil.isBlank(ids)) return emptyList;
		String s[] = ids.split(symbol);
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0, length = s.length; i < length; i++) {
			if (!StringUtil.isBlank(s[i])) {
				Integer x = Integer.valueOf(s[i]);
				if (!list.contains(x)) {
					list.add(x);
				}
			}
		}
		return list;
	}

	/**
	 * 将多个对象组成list
	 *
	 * @param args
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> args2List(@NonNull T... args) {
		return array2List(args);
	}

	/**
	 * @param str
	 * @param symbol
	 * @return
	 * @author wwhhf
	 * @comment 字符串转为字符串数组
	 * @since 2016年6月13日
	 */
	public static List<String> string2StrList(String str, @NonNull String symbol) {
		if (StringUtil.isBlank(str)) return emptyList;
		String s[] = str.split(symbol);
		List<String> list = new ArrayList<String>();
		for (int i = 0, length = s.length; i < length; i++) {
			if (!StringUtil.isBlank(s[i])) {
				list.add(s[i]);
			}
		}
		return list;
	}

	/**
	 * @param ids
	 * @param symbol
	 * @return
	 * @author wwhhf
	 * @comment 拼接字符串
	 * @since 2016年6月13日
	 */
	public static <T> String list2String(@NonNull List<T> ids,
	                                     @NonNull String symbol) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0, len = ids.size(); i < len; i++) {
			if (i == 0)
				sb.append(ids.get(i));
			else {
				sb.append(symbol).append(ids.get(i));
			}
		}
		return sb.toString();
	}

	/**
	 * 从list对象中取出指定属性当作key
	 *
	 * @param <T>
	 * @param <Q>
	 * @return
	 */
	@SneakyThrows
	public static <T, Q> Map<T, Q> list2Map(@NonNull List<Q> objs, @NonNull String fieldName,
	                                        @NonNull Class<T> clazz) {
		Map<T, Q> res = new HashMap<>();
		for (Q obj : objs) {
			res.put(BeanUtil.getFieldValue(obj, fieldName, clazz), obj);
		}
		return res;
	}

	/**
	 * 将list转为set
	 *
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T> Set<T> list2Set(@NonNull List<T> list) {
		Set<T> set = new HashSet<>();
		for (T t : list) {
			set.add(t);
		}
		return set;
	}

	/**
	 * list 去重
	 *
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> listDuplicate(@NonNull List<T> list) {
		List<T> newList = new ArrayList<>();
		newList.addAll(list2Set(list));
		return newList;
	}

	/**
	 * 求交集
	 *
	 * @param a
	 * @param b
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> listIntersect(@NonNull List<T> a, @NonNull List<T> b) {
		Set<T> setA = list2Set(a);
		Set<T> setB = list2Set(b);
		Set<T> res = new HashSet<>();
		for (T ta : setA) {
			if (setB.contains(ta)) {
				res.add(ta);
			}
		}
		for (T tb : setB) {
			if (setA.contains(tb)) {
				res.add(tb);
			}
		}
		return new ArrayList<>(res);
	}

	/**
	 * a排除b
	 *
	 * @param a
	 * @param b
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> listNotExistInA(@NonNull List<T> a, @NonNull List<T> b) {
		Set<T> set = new HashSet<>(b);
		List<T> res = new ArrayList<>();
		for (T t : a) {
			if (!set.contains(t)) {
				res.add(t);
			}
		}
		return res;
	}

	/**
	 * 求差集
	 *
	 * @param a
	 * @param b
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> listDifferent(@NonNull List<T> a, @NonNull List<T> b) {
		Set<T> setA = list2Set(a);
		Set<T> setB = list2Set(b);
		Set<T> res = new HashSet<>();
		for (T ta : setA) {
			if (!setB.contains(ta)) {
				res.add(ta);
			}
		}
		for (T tb : setB) {
			if (!setA.contains(tb)) {
				res.add(tb);
			}
		}
		return new ArrayList<>(res);
	}

	/**
	 * list 转为数组
	 *
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T> T[] list2Array(@NonNull List<T> list, Class<T> clazz) {
		T[] array = (T[]) Array.newInstance(clazz, list.size());
		return list.toArray(array);
	}

	/**
	 * 数组转为list
	 *
	 * @param array
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> array2List(@NonNull T[] array) {
		List<T> list = new ArrayList<>();
		list.addAll(Arrays.asList(array));
		return list;
	}

	/**
	 * 可变数组转为list
	 *
	 * @param objs
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> objects2List(@NonNull Class<T> clazz, @NonNull T... objs) {
		return array2List(objs);
	}

	/**
	 * map的key和value倒置
	 * 请注意value是否重复以及null
	 *
	 * @param map
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> Map<V, K> mapKeyValueInvert(@NonNull Map<K, V> map) {
		Map<V, K> res = new HashMap<>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			res.put(entry.getValue(), entry.getKey());
		}
		return res;
	}

	/**
	 * 二分查找
	 *
	 * @param arr
	 * @param item
	 * @return
	 */
	public static <T> int binarySearch(@NonNull T[] arr, @NonNull T item) {
		return Arrays.binarySearch(arr, item);
	}

	/**
	 * 二分查找
	 *
	 * @param list
	 * @param item
	 * @param clazz
	 * @return
	 */
	public static <T> int binarySearch(@NonNull List<T> list, @NonNull T item,
	                                   @NonNull Class<T> clazz) {
		T arr[] = CollectionUtil.list2Array(list, clazz);
		return Arrays.binarySearch(arr, item);
	}
}
