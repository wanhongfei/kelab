package org.kelab.util;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kelab.util.constant.SeparatorConstant;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author wwhhf
 * @comment 类相关工具
 * @since 2016年6月13日
 */
@Slf4j
public class BeanUtil {

	public static final String CLASS_TYPE = "CLASS_TYPE";

	/**
	 * @param clazzName
	 * @param methodName
	 * @param args
	 * @author wwhhf
	 * @comment 拼装字符串
	 * @since 2016年6月1日
	 */
	public static String methodInfo(@NonNull String clazzName,
	                                @NonNull String methodName,
	                                @NonNull Object[] args) {
		StringBuffer sb = new StringBuffer(clazzName).append(".")
				.append(methodName).append("(");
		boolean isFirst = true;
		for (Object arg : args) {
			if (isFirst == true) {
				sb.append(arg);
				isFirst = false;
			} else {
				sb.append(", ").append(arg);
			}
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * 检查对象的属性是否相同
	 * propertyName 小写字母开头
	 *
	 * @param object1
	 * @param object2
	 * @param clazz
	 * @param propertyName
	 * @param <T>
	 * @return
	 */
	public static <T> boolean isPropertyEqual(@NonNull T object1, @NonNull T object2, @NonNull Class<T> clazz, @NonNull String propertyName) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(propertyName, clazz);
			Method method = pd.getReadMethod();
			Object res1 = method.invoke(object1);
			Object res2 = method.invoke(object2);
			if (res1 == null && res2 == null) {
				return true;
			} else if (res1 != null && res2 != null && res1.equals(res2)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// 属性不相等
			return false;
		}
	}

	/**
	 * List -> map
	 *
	 * @param list
	 * @param fieldName
	 * @param clazz
	 * @param <T>
	 * @param <K>
	 * @return
	 */
	public static <T, K> Map<T, K> list2MapByFieldName(@NonNull List<K> list,
	                                                   @NonNull String fieldName, @NonNull Class<T> clazz) {
		Map<T, K> ans = new HashMap<>();
		for (K k : list) {
			T t = getFieldValue(k, fieldName, clazz);
			ans.put(t, k);
		}
		return ans;
	}

	/**
	 * 深拷贝
	 *
	 * @param srcObject
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T deepCopy(@NonNull T srcObject, @NonNull Class<T> clazz) {
		T destObject = null;
		try {
			destObject = clazz.newInstance();
			copyProperties(srcObject, destObject);
			return destObject;
		} catch (InstantiationException e) {
			log.error("deepCopy InstantiationException:{}", FastJsonUtil.object2Json(srcObject));
			return null;
		} catch (IllegalAccessException e) {
			log.error("deepCopy IllegalAccessException:{}", FastJsonUtil.object2Json(srcObject));
			return null;
		}
	}

	/**
	 * 利用反射设置属性值
	 * （1）getDeclaredFields()返回Class中所有的字段，包括私有字段；
	 * （2）getFields  只返回公共字段，即有public修饰的字段
	 * Object value 可为空
	 *
	 * @param obj
	 * @param fieldName
	 * @param value
	 */
	@SneakyThrows
	public static void setFieldValue(@NonNull Object obj, @NonNull String fieldName,
	                                 Object value) {
		Class baseClazz = obj.getClass();
		for (; !baseClazz.equals(Object.class); baseClazz = baseClazz.getSuperclass()) {
			try {
				PropertyDescriptor pd = new PropertyDescriptor(fieldName, baseClazz);
				Method method = pd.getWriteMethod();
				if (!Modifier.isPublic(method.getModifiers())) {   //设置非共有类属性权限
					method.setAccessible(true);
				}
				method.invoke(obj, value);
			} catch (Exception e) {
				Field targetField = baseClazz.getDeclaredField(fieldName);
				if (!Modifier.isPublic(targetField.getModifiers())) {   //设置非共有类属性权限
					targetField.setAccessible(true);
				}
				targetField.set(obj, value); //设置类属性值
				// 异常直接抛出
			}
		}
	}

	/**
	 * 利用反射获取属性值
	 *
	 * @param obj
	 * @param fieldName
	 */
	@SneakyThrows
	public static <T> T getFieldValue(@NonNull Object obj, @NonNull String fieldName,
	                                  @NonNull Class<T> clazz) {
		T res = null;
		Class baseClazz = obj.getClass();
		for (; res == null && !baseClazz.equals(Object.class); baseClazz = baseClazz.getSuperclass()) {
			try {
				PropertyDescriptor pd = new PropertyDescriptor(fieldName, baseClazz);
				Method method = pd.getReadMethod();
				if (!Modifier.isPublic(method.getModifiers())) {   //设置非共有类属性权限
					method.setAccessible(true);
				}
				res = (T) method.invoke(obj);
			} catch (Exception e) {
				Field targetField = null;
				targetField = baseClazz.getDeclaredField(fieldName);
				if (!Modifier.isPublic(targetField.getModifiers())) {   //设置非共有类属性权限
					targetField.setAccessible(true);
				}
				res = (T) targetField.get(obj); //设置类属性值
				// 在此抛出异常会直接抛出
			}
		}
		return res;
	}

	/**
	 * 强转对象
	 *
	 * @param obj
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T cast(Object obj, Class<T> clazz) {
		return (T) obj;
	}

	/**
	 * 获取多级属性，通过"."进行分割:
	 * 如：a.b.c
	 * Map<String,T> key必须是String，否则为null
	 *
	 * @param obj
	 * @param fieldNamesStr
	 * @return
	 */
	@SneakyThrows
	public static Object getFieldValueByFieldNamesStr(@NonNull Object obj,
	                                                  @NonNull String fieldNamesStr) {
		if (StringUtil.isBlank(fieldNamesStr)) {
			return obj;
		}
		int pos = fieldNamesStr.indexOf(SeparatorConstant.PERIOD);
		String propertyName = null;
		String nextfieldNamesStr = null;
		if (pos == -1) {
			propertyName = fieldNamesStr;
			nextfieldNamesStr = "";
		} else {
			propertyName = fieldNamesStr.substring(0, pos);
			nextfieldNamesStr = fieldNamesStr.substring(pos + 1);
		}
		Map<String, Object> map = null;
		if (obj instanceof Map) {
			map = (Map<String, Object>) obj;
		} else {
			map = bean2MapNoRecursion(obj);
		}
		Object res = map.get(propertyName);
		if (res == null) {
			if (StringUtil.isBlank(nextfieldNamesStr)) {
				return null;
			} else {
				throw new Exception(propertyName + " is null");
			}
		} else {
			return getFieldValueByFieldNamesStr(res, nextfieldNamesStr);
		}
	}

	/**
	 * 从包package中获取所有的Class
	 *
	 * @param packageName
	 * @return
	 */
	public static List<Class<?>> scan(@NonNull String packageName) {
		//第一个class类的集合
		List<Class<?>> classes = new ArrayList<Class<?>>();
		//是否循环迭代
		boolean recursive = true;
		//获取包的名字 并进行替换
		String packageDirName = packageName.replace('.', '/');
		//定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			//循环迭代下去
			while (dirs.hasMoreElements()) {
				//获取下一个元素
				URL url = dirs.nextElement();
				//得到协议的名称
				String protocol = url.getProtocol();
				//如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					//获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					//以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else if ("jar".equals(protocol)) {
					//如果是jar包文件
					//定义一个JarFile
					JarFile jar;
					try {
						//获取jar
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						//从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						//同样的进行循环迭代
						while (entries.hasMoreElements()) {
							//获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							//如果是以/开头的
							if (name.charAt(0) == '/') {
								//获取后面的字符串
								name = name.substring(1);
							}
							//如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								//如果以"/"结尾 是一个包
								if (idx != -1) {
									//获取包名 把"/"替换成"."
									packageName = name.substring(0, idx).replace('/', '.');
								}
								//如果可以迭代下去 并且是一个包
								if ((idx != -1) || recursive) {
									//如果是一个.class文件 而且不是目录
									if (name.endsWith(".class") && !entry.isDirectory()) {
										//去掉后面的".class" 获取真正的类名
										String className = name.substring(packageName.length() + 1, name.length() - 6);
										try {
											//添加到classes
											classes.add(Class.forName(packageName + '.' + className));
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classes;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 *
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	private static void findAndAddClassesInPackageByFile(String packageName, String packagePath,
	                                                     final boolean recursive, List<Class<?>> classes) {
		//获取此包的目录 建立一个File
		File dir = new File(packagePath);
		//如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		//如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			//自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		if (dirfiles == null || dirfiles.length == 0) {
			return;
		}
		//循环所有文件
		for (File file : dirfiles) {
			//如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
						file.getAbsolutePath(),
						recursive,
						classes);
			} else {
				//如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					//添加到集合中去
					classes.add(Class.forName(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 拷贝属性
	 *
	 * @param src
	 * @param dest
	 */
	public static void copyProperties(@NonNull Object src, @NonNull Object dest) {
		org.springframework.beans.BeanUtils.copyProperties(src, dest);
	}

	/**
	 * 将bean转化为map
	 * 注意：
	 * 1. Bean必须实现getter和setter方法
	 * 2. 请避开CLASS_TYPE属性命名
	 *
	 * @param obj
	 * @return
	 */
	@SneakyThrows
	public static Map<String, Object> bean2Map(@NonNull Object obj) {
		Map<String, Object> res = new HashMap<>();
		res.put(CLASS_TYPE, obj.getClass());
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String propertyName = property.getName();
			if (!"class".equals(propertyName)) {
				Method getter = property.getReadMethod();
				getter.setAccessible(true);
				Object propertyValue = getter.invoke(obj);
				if (propertyValue == null || isWrapClass(propertyValue)
						|| propertyValue instanceof String
						|| propertyValue instanceof Number
						|| propertyValue instanceof Boolean
						|| propertyValue instanceof Character) {
					res.put(propertyName, propertyValue);
				} else {
					res.put(propertyName, bean2Map(propertyValue));
				}
			}
		}
		return res;
	}

	/**
	 * 将bean转化为map
	 * 注意：
	 * 1. Bean必须实现getter和setter方法
	 * 3. 不递归展开
	 *
	 * @param obj
	 * @return
	 */
	@SneakyThrows
	public static Map<String, Object> bean2MapNoRecursion(@NonNull Object obj) {
		Map<String, Object> res = new HashMap<>();
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String propertyName = property.getName();
			if (!"class".equals(propertyName)) {
				Method getter = property.getReadMethod();
				getter.setAccessible(true);
				Object propertyValue = getter.invoke(obj);
				res.put(propertyName, propertyValue);
			}
		}
		return res;
	}

	/**
	 * 将map转化为bean
	 * 注意：
	 * 1. 实现getter和setter方法
	 * 2. 多级结构保证CLASS_TYPE字段，以及其无参构造函数
	 *
	 * @param map
	 * @return
	 */
	@SneakyThrows
	public static Object map2Bean(@NonNull Map<String, Object> map) {
		if (map.containsKey(CLASS_TYPE)) {
			Class type = (Class) map.get(CLASS_TYPE);
			Object obj = type.newInstance();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String propertyName = entry.getKey();
				if (!CLASS_TYPE.equals(propertyName)) {
					Object propertyValue = entry.getValue();
					if (propertyValue == null) {
						setFieldValue(obj, propertyName, propertyValue);
					} else if (propertyValue instanceof Map) {
						setFieldValue(obj, propertyName,
								map2Bean((Map<String, Object>) propertyValue));
					} else {
						setFieldValue(obj, propertyName, propertyValue);
					}
				}
			}
			return obj;
		}
		return map;
	}

	/**
	 * 判断是否基本类型:不包括String
	 *
	 * @param obj
	 * @return
	 */
	public static boolean isWrapClass(@NonNull Object obj) {
		try {
			return ((Class) obj.getClass().getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * bean数组转为fields数组
	 *
	 * @param objs
	 * @param fieldsName
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	@SneakyThrows
	public static <T> List<T> beans2Fields(@NonNull List objs, @NonNull String fieldsName,
	                                       @NonNull Class<T> clazz) {
		List<T> fields = new ArrayList<>();
		for (Object object : objs) {
			fields.add(getFieldValue(object, fieldsName, clazz));
		}
		return fields;
	}

	/**
	 * map转为key数组
	 *
	 * @param map
	 * @param <T>
	 * @return
	 */
	@SneakyThrows
	public static <T, K> List<T> map2KeyList(@NonNull Map<T, K> map) {
		return new ArrayList<T>(map.keySet());
	}

	/**
	 * map转为value数组
	 *
	 * @param map
	 * @param <T>
	 * @return
	 */
	@SneakyThrows
	public static <T, K> List<K> map2ValueList(@NonNull Map<T, K> map) {
		return new ArrayList<K>(map.values());
	}

	/**
	 * bean数组转为fields数组
	 * 允许多级属性，用"."分割
	 *
	 * @param objs
	 * @param fieldNamesStr
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	@SneakyThrows
	public static <T> List<T> beans2FieldsByFieldNamesStr(@NonNull List objs,
	                                                      @NonNull String fieldNamesStr,
	                                                      @NonNull Class<T> clazz) {
		List<T> fields = new ArrayList<>();
		for (Object object : objs) {
			fields.add((T) getFieldValueByFieldNamesStr(object, fieldNamesStr));
		}
		return fields;
	}

	/**
	 * bean数组转为fields数组
	 *
	 * @param objs
	 * @param fieldsName
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> beans2Fields(@NonNull Object[] objs, @NonNull String fieldsName,
	                                       @NonNull Class<T> clazz) {
		List<T> fields = new ArrayList<>();
		for (Object object : objs) {
			fields.add(getFieldValue(object, fieldsName, clazz));
		}
		return fields;
	}

	/**
	 * bean数组转为fields数组
	 *
	 * @param objs
	 * @param fieldsName
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T[] beans2FieldsArray(@NonNull List objs, @NonNull String fieldsName,
	                                        @NonNull Class<T> clazz) {
		List<T> fields = new ArrayList<>();
		for (Object object : objs) {
			fields.add(getFieldValue(object, fieldsName, clazz));
		}
		return CollectionUtil.list2Array(fields, clazz);
	}

	/**
	 * bean数组转为fields数组
	 *
	 * @param objs
	 * @param fieldsName
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T[] beans2FieldsArray(@NonNull Object[] objs, @NonNull String fieldsName,
	                                        @NonNull Class<T> clazz) {
		List<T> fields = new ArrayList<>();
		for (Object object : objs) {
			fields.add(getFieldValue(object, fieldsName, clazz));
		}
		return CollectionUtil.list2Array(fields, clazz);
	}

}