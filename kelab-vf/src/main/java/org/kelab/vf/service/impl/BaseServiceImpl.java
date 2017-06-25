package org.kelab.vf.service.impl;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.kelab.vf.dao.BaseDao;
import org.kelab.vf.query.BaseQuery;
import org.kelab.vf.result.PaginationResult;
import org.kelab.vf.result.SingleResult;
import org.kelab.vf.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hongfei.whf on 2016/11/26.
 */
public class BaseServiceImpl<T, Q extends BaseQuery> implements BaseService<T, Q> {

	@Autowired
	protected BaseDao<T, Q> baseDao;

	@Resource
	protected TransactionTemplate transactionTemplate;

	// ==================== query =======================

	@Override
	public T queryById(Q q) {
		return this.baseDao.queryById(q);
	}

	@Override
	public T queryById(int id) {
		return this.baseDao.queryById(id);
	}

	@Override
	public SingleResult<T> querySingleResById(int id) {
		return new SingleResult<T>(this.baseDao.queryById(id));
	}

	@Override
	public SingleResult<T> querySingleResById(Q q) {
		return new SingleResult<T>(this.baseDao.queryById(q));
	}

	@Override
	public PaginationResult<T> queryList(@NonNull Q query) {
		List<T> data = this.baseDao.queryList(query);
		return new PaginationResult<T>(data, data.size());
	}

	@Override
	@SneakyThrows
	public PaginationResult<T> queryPage(@NonNull Q query) {
		return this.baseDao.queryPage(query);
	}

	@Override
	public int count(@NonNull Q query) {
		return this.baseDao.count(query);
	}

	// ==================== save =======================

	@Override
	public void save(@NonNull T t) {
		this.baseDao.save(t);
	}

	@Override
	public void batchSave(@NonNull List<T> list) {
		this.baseDao.batchSave(list);
	}

	@Override
	public int saveAndRetId(@NonNull T t) {
		return this.baseDao.saveAndRetId(t);
	}

	// ==================== update =======================

	@Override
	public void update(@NonNull T t) {
		this.baseDao.update(t);
	}

	@Override
	public void dynamicUpdate(@NonNull T t) {
		this.baseDao.dynamicUpdate(t);
	}

	// ==================== delete =======================

	@Override
	public void deleteByIds(@NonNull List<Integer> ids) {
		this.baseDao.deleteByIds(ids);
	}

	@Override
	public void deleteById(int id) {
		this.baseDao.deleteById(id);
	}
}
