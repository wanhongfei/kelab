package org.kelab.vf.generator;

import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.kelab.util.BeanUtil;
import org.kelab.util.CollectionUtil;
import org.kelab.util.StringUtil;
import org.kelab.util.model.Pair;
import org.kelab.vf.dao.impl.BaseDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by hongfei.whf on 2016/12/31.
 */
public class MybatisMapping {

	// only base
	public final static Map<Class, String/*tableName*/> class2TableName = new HashMap<>();
	// only base
	public final static Map<Class, Map<String/*fieldName*/, String/*column*/>> classAndField2Column = new HashMap<>();
	// only include WithBLOBs
	public final static Map<Class/*entity*/, Class/*query*/> entity2QueryType = new HashMap<>();
	// base dao and entity
	public final static Map<Class/*entity*/, Class/*query*/> dao2Entitys = new HashMap<>();
	// include base and WithBLOBs
	public final static Map<String, Class<?>> entitysMap = new HashMap<>();
	// only base
	public final static Map<String, Boolean> isEntityInherit = new HashMap<>();
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Setter
	private String daoPackageDir = "org.kelab.util.dao";

	@Setter
	private String entityPackageDir = "org.kelab.util.entity";

	/**
	 * 初始化操作
	 */
	@SneakyThrows
	protected void init() {
		// class2TableName classAndField2Column init
		Collection<ResultMap> resultMaps = this.sqlSessionFactory.getConfiguration().getResultMaps();
		List<ResultMap> resultMapList = new ArrayList<>(resultMaps);
		for (int i = 0, len = resultMapList.size(); i < len; i++) {
			if (resultMapList.get(i) instanceof ResultMap) {
				this.addResultMap(resultMapList.get(i));
			}
		}
		// entity2QueryType init
		List<Class<?>> classes = BeanUtil.scan(daoPackageDir);
		for (Class clazz : classes) {
			Pair<Class, Class> entityAndQuery = getEntityAndQuery(clazz);
			if (entityAndQuery.getValue1() != null
					&& !entity2QueryType.containsKey(entityAndQuery.getValue1())) {
//				System.out.println("===========");
//				System.out.println("dao:" + clazz);
//				System.out.println("entity:" + entityAndQuery.getValue1());
//				System.out.println("query:" + entityAndQuery.getValue2());
//				System.out.println("===========");
				// dao -> entity
				dao2Entitys.put(clazz, entityAndQuery.getValue1());
				// entity -> query
				entity2QueryType.put(entityAndQuery.getValue1(), entityAndQuery.getValue2());
			}
		}
		// entitys init
		List<Class<?>> entitys = BeanUtil.scan(entityPackageDir);
		entitys.addAll(BeanUtil.scan(entityPackageDir));
		for (Class clazz : entitys) {
			entitysMap.put(clazz.getCanonicalName(), clazz);
			if (clazz.getCanonicalName().endsWith("WithBLOBs")) {
				isEntityInherit.put(clazz.getCanonicalName().replace("WithBLOBs", ""), true);
			} else {
				isEntityInherit.put(clazz.getCanonicalName(), false);
			}
		}
	}

	/**
	 * 获取BaseDao的<T,Q>
	 *
	 * @param clazz
	 * @return
	 */
	@SneakyThrows
	private Pair<Class, Class> getEntityAndQuery(@NonNull Class clazz) {
		if (dao2Entitys.containsKey(clazz)) {
			Class entityType = dao2Entitys.get(clazz);
			return new Pair<>(entityType, entity2QueryType.get(entityType));
		}
		ResolvableType type = ResolvableType.forClass(clazz);
		Class entityType = type.as(BaseDaoImpl.class).getGeneric(0).resolve();
		Class queryType = type.as(BaseDaoImpl.class).getGeneric(1).resolve();
		return new Pair<>(entityType, queryType);
	}

	/**
	 * 外部调用，解析dao->entity->query
	 *
	 * @param target
	 * @return
	 */
	@SneakyThrows
	public Pair<Class, Class> getEntityAndQueryByDao(@NonNull Object target) {
		if (target instanceof BaseDaoImpl) {
			Class entityType = dao2Entitys.get(target.getClass());
			Class queryType = entity2QueryType.get(entityType);
			return new Pair<>(entityType, queryType);
		} else {
			throw new Exception("getEntityAndQuery error :" + target.getClass());
		}
	}

	/**
	 * class->table
	 *
	 * @param clazz
	 * @param tableName
	 */
	private void addClass(@NonNull Class clazz, @NonNull String tableName) {
		if (!class2TableName.containsKey(clazz)) {
			class2TableName.put(clazz, tableName);
			classAndField2Column.put(clazz, new HashMap<String, String>());
		}
	}

	/**
	 * field->column
	 *
	 * @param clazz
	 * @param field
	 * @param column
	 */
	private void addFields(@NonNull Class clazz, @NonNull String field, @NonNull String column) {
		if (!class2TableName.containsKey(clazz)) {
			classAndField2Column.put(clazz, new HashMap<String, String>());
		}
		Map<String, String/*column*/> map = classAndField2Column.get(clazz);
		map.put(field, column);
	}

	/**
	 * entity 是否存在继承关系
	 *
	 * @param clazz
	 * @return
	 */
	public boolean isInherit(@NonNull Class clazz) {
		return isEntityInherit.get(clazz.getCanonicalName());
	}

	/**
	 * entity 是否存在继承关系
	 *
	 * @param clazz
	 * @return
	 */
	public Class inherit(@NonNull Class clazz) {
		return entitysMap.get(clazz.getCanonicalName() + "WithBLOBs");
	}

	/**
	 * entity without "WithBLOBs"
	 *
	 * @return
	 */
	public List<Class> getBaseEntitys() {
		List<Class> res = new ArrayList<>();
		for (Class clazz : entitysMap.values()) {
			if (!clazz.getName().endsWith("WithBLOBs")) {
				res.add(clazz);
			}
		}
		return res;
	}

	/**
	 * 获取tableName
	 *
	 * @param clazz
	 * @return
	 */
	public String getTableName(@NonNull Class clazz) {
		if (class2TableName.containsKey(clazz)) {
			return class2TableName.get(clazz);
		}
		return StringUtil.emptyString;
	}

	/**
	 * 获取tableName
	 *
	 * @param clazz
	 * @return
	 */
	public Class getQueryType(@NonNull Class clazz) {
		if (isEntityInherit.get(clazz.getCanonicalName())) {
			clazz = entitysMap.get(clazz.getCanonicalName() + "WithBLOBs");
		}
		if (entity2QueryType.containsKey(clazz)) {
			return entity2QueryType.get(clazz);
		}
		return null;
	}

	/**
	 * 获取tableName
	 *
	 * @param clazz
	 * @return
	 */
	public String getMapperNameSpace(@NonNull Class clazz) {
		String className = clazz.getName();
		if (className.endsWith("WithBLOBs")) {
			className = className.replace("WithBLOBs", "");
		}
		return className.replace("entity", "dao") + "Mapper";
	}

	/**
	 * 获取属性和字段
	 *
	 * @param clazz
	 * @return
	 */
	public Map<String, String> getField2Column(@NonNull Class clazz) {
		return classAndField2Column.get(clazz);
	}

	/**
	 * 获取字段
	 *
	 * @param clazz
	 * @return
	 */
	public String getColumn(@NonNull Class clazz, @NonNull String fieldName) {
		if (classAndField2Column.containsKey(clazz)) {
			return classAndField2Column.get(clazz).get(fieldName);
		}
		return StringUtil.emptyString;
	}

	/**
	 * 获取字段
	 *
	 * @param clazz
	 * @return
	 */
	public List<String> getColumns(@NonNull Class clazz) {
		if (classAndField2Column.containsKey(clazz)) {
			Map<String, String> map = classAndField2Column.get(clazz);
			return new ArrayList<>(map.values());
		}
		return CollectionUtil.emptyList;
	}

	/**
	 * 添加Mybatis的ResultMap关系
	 *
	 * @param resultMap
	 * @throws NoSuchFieldException
	 */
	private void addResultMap(@NonNull ResultMap resultMap) throws NoSuchFieldException {
		// class -> table
		Class clazz = resultMap.getType();
		if (hasSuper(clazz.getName())) {
			clazz = clazz.getSuperclass();
		}

		Table tableAnn = (Table) clazz.getAnnotation(Table.class);
		if (tableAnn == null) {
			// 自定义数据类型
			return;
		}
		addClass(clazz, tableAnn.name());

		List<ResultMapping> mappings = resultMap.getResultMappings();
		for (ResultMapping mapping : mappings) {
			// class + Field -> table
			addFields(clazz, mapping.getProperty(), mapping.getColumn());
		}
	}

	/**
	 * 是否继承
	 *
	 * @param className
	 * @return
	 */
	private boolean hasSuper(@NonNull String className) {
		return className.endsWith("WithBLOBs");
	}

	/**
	 * 是否继承
	 *
	 * @param clazz
	 * @return
	 */
	private boolean hasSubClass(@NonNull Class clazz) {
		return !(clazz.getDeclaredFields().length == getColumns(clazz).size());
	}

	/**
	 * 获取子类
	 *
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SneakyThrows
	public Class subClass(@NonNull Class clazz) {
		return entitysMap.get(clazz.getCanonicalName() + "WithBLOBs");
	}

	/**
	 * 如果是存在关系，获取子类和父类的所有属性
	 * 否则，值获取父类所有属性
	 *
	 * @param clazz
	 * @return
	 */
	@SneakyThrows
	public List<Field> classAndSubClassFields(@NonNull Class clazz) {
		List<Field> res = new ArrayList<>();
		res.addAll(CollectionUtil.array2List(clazz.getDeclaredFields()));
		if (hasSubClass(clazz)) {
			res.addAll(CollectionUtil.array2List(subClass(clazz).getDeclaredFields()));
		}
		return res;
	}

}
