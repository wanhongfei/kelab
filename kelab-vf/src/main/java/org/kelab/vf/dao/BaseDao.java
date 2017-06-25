package org.kelab.vf.dao;

import org.kelab.vf.query.BaseQuery;
import org.kelab.vf.result.PaginationResult;

import java.util.List;
import java.util.Map;

/**
 * Created by hongfei.whf on 2016/8/27.
 */
public interface BaseDao<T, Q extends BaseQuery> {

	// =========================== delete =============================

	/**
	 * 删除指定id
	 *
	 * @param id
	 * @return
	 */
	void deleteById(int id);

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	void deleteByIds(List<Integer> ids);

	// =========================== save =============================

	/**
	 * 插入
	 *
	 * @param record
	 * @return
	 */
	void save(T record);

	/**
	 * 条件插入
	 *
	 * @param record
	 * @return
	 */
	void dynamicSave(T record);

	/**
	 * 批量插入
	 * 不存在执行失败，只要出现异常，立即全部回滚
	 * 大数据量分批插入提交 10000条插入8秒
	 *
	 * @param list
	 * @return
	 */
	void batchSave(List<T> list);

	/**
	 * 批量插入
	 *
	 * @param list
	 * @return
	 */
	List<T> batchSaveAndRetList(List<T> list);

	/**
	 * 批量插入
	 *
	 * @param list
	 * @return
	 */
	List<Integer> batchSaveAndRetIds(List<T> list);

	/**
	 * 批量插入
	 *
	 * @param list
	 * @return
	 */
	void batchSave(List<T> list, int offset);

	/**
	 * 批量插入
	 *
	 * @param list
	 * @return
	 */
	List<T> batchSaveAndRetList(List<T> list, int offset);

	/**
	 * 插入并返回主键
	 *
	 * @param t
	 * @return
	 */
	int saveAndRetId(T t);

	// =========================== update =============================

	/**
	 * 条件更新
	 *
	 * @param record
	 * @return
	 */
	void dynamicUpdate(T record);

	/**
	 * 更新
	 *
	 * @param record
	 * @return
	 */
	void update(T record);

	/**
	 * 批量动态更新
	 *
	 * @param record
	 * @param ids
	 */
	void batchDynamicUpdate(T record, List<Integer> ids);


	/**
	 * 批量动态更新
	 *
	 * @param records
	 */
	void batchDynamicUpdate(List<T> records);

	/**
	 * 批量更新
	 *
	 * @param records
	 */
	void batchUpdate(List<T> records);

	/**
	 * 批量动态更新
	 *
	 * @param records
	 */
	void batchDynamicUpdate(List<T> records, int offset);

	/**
	 * 批量更新
	 *
	 * @param records
	 */
	void batchUpdate(List<T> records, int offset);

	// =========================== query =============================

	/**
	 * 条件查询(不分页)
	 *
	 * @param query
	 * @return
	 */
	List<T> queryList(Q query);

	/**
	 * 分页查询
	 *
	 * @param query
	 * @return
	 */
	PaginationResult<T> queryPage(Q query);

	/**
	 * 条件查询
	 *
	 * @param query
	 * @return
	 */
	T[] queryArray(Q query, Class<T> clazz);

	/**
	 * id->entity
	 *
	 * @param query
	 * @return
	 */
	Map<Integer, T> queryMap(Q query);

	/**
	 * 查询指定id
	 *
	 * @param id
	 * @return
	 */
	T queryById(int id);

	/**
	 * 根据ids查询
	 *
	 * @param ids
	 * @return
	 */
	List<T> queryByIds(List<Integer> ids);

	/**
	 * 查询指定id
	 *
	 * @param query
	 * @return
	 */
	T queryById(Q query);

	/**
	 * 根据ids查询
	 *
	 * @param query
	 * @return
	 */
	List<T> queryByIds(Q query);

	/**
	 * 条件查询数量
	 *
	 * @param query
	 * @return
	 */
	int count(Q query);

}
