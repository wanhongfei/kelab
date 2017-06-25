package org.kelab.vf.service;

import org.kelab.vf.query.BaseQuery;
import org.kelab.vf.result.PaginationResult;
import org.kelab.vf.result.SingleResult;

import java.util.List;

/**
 * Created by hongfei.whf on 2016/11/26.
 */
public interface BaseService<T, Q extends BaseQuery> {

	// ================== query =====================

	/**
	 * 查询指定id
	 *
	 * @param q
	 * @return
	 */
	T queryById(Q q);

	/**
	 * 查询指定id
	 *
	 * @param id
	 * @return
	 */
	T queryById(int id);

	/**
	 * 查询指定id
	 * 返回单结果封装类
	 *
	 * @param id
	 * @return
	 */
	SingleResult<T> querySingleResById(int id);

	SingleResult<T> querySingleResById(Q q);

	/**
	 * 全部条件查询
	 *
	 * @param query
	 * @return
	 */
	PaginationResult<T> queryList(Q query);

	/**
	 * 分页条件查询
	 *
	 * @param query
	 * @return
	 */
	PaginationResult<T> queryPage(Q query);

	/**
	 * 查询数量
	 *
	 * @param query
	 * @return
	 */
	int count(Q query);

	// ================== update =====================

	/**
	 * 插入操作
	 *
	 * @param t
	 * @return
	 */
	void save(T t);

	/**
	 * 批量插入
	 *
	 * @param list
	 * @return
	 */
	void batchSave(List<T> list);

	/**
	 * 插入并返回主键
	 *
	 * @param t
	 * @return
	 */
	int saveAndRetId(T t);

	// ================== update =====================

	/**
	 * 更新操作
	 *
	 * @param t
	 * @return
	 */
	void update(T t);

	/**
	 * 动态更新
	 *
	 * @param t
	 */
	void dynamicUpdate(T t);

	// ================== delete =====================

	/**
	 * 删除操作
	 *
	 * @param ids
	 * @return
	 */
	void deleteByIds(List<Integer> ids);

	/**
	 * 根据id进行删除
	 *
	 * @param id
	 * @return
	 */
	void deleteById(int id);
}