package org.kelab.vf.dao.impl;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.kelab.util.BeanUtil;
import org.kelab.util.CollectionUtil;
import org.kelab.vf.cache.CacheAccess;
import org.kelab.vf.cache.CacheFlush;
import org.kelab.vf.dao.BaseDao;
import org.kelab.vf.query.BaseQuery;
import org.kelab.vf.result.PaginationResult;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hongfei.whf on 2016/8/27.
 */
@Slf4j
public abstract class BaseDaoImpl<T, Q extends BaseQuery> extends SqlSessionDaoSupport implements BaseDao<T, Q> {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	/**
	 * 获取mapper的namespace
	 *
	 * @return
	 */
	public abstract String getNameSpace();

	// =========================== delete =============================

	@Override
	@CacheFlush
	public void deleteById(int id) {
		this.delete("deleteByPrimaryKey", id);
	}

	@Override
	@CacheFlush
	public void deleteByIds(@NonNull List<Integer> ids) {
		this.delete("deleteByIds", ids);
	}

	// =========================== update =============================

	@Override
	@CacheFlush
	public void dynamicUpdate(@NonNull T record) {
		this.update("updateByPrimaryKeySelective", record);
	}

	@Override
	@CacheFlush
	public void update(@NonNull T record) {
		String className = record.getClass().getName();
		String mapperMethodId = "updateByPrimaryKey";
		if (className.endsWith("WithBLOBs")) {
			mapperMethodId = "updateByPrimaryKeyWithBLOBs";
		}
		this.update(mapperMethodId, record);
	}

	@Override
	@CacheFlush
	public void batchDynamicUpdate(@NonNull T record, @NonNull List<Integer> ids) {
		Map<String, Object> param = new HashMap<>();
		param.put("data", record);
		param.put("ids", ids);
		this.update("batchDynamicUpdate", param);
	}

	@Override
	@CacheFlush
	public void batchDynamicUpdate(List<T> records) {
		batchDynamicUpdate(records, 1000);
	}

	@Override
	@CacheFlush
	public void batchUpdate(List<T> records) {
		batchUpdate(records, 1000);
	}

	@Override
	@CacheFlush
	public void batchDynamicUpdate(List<T> records, int offset) {
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		int page = records.size() / offset + (records.size() % offset == 0 ? 0 : 1);
		long beginTime = System.currentTimeMillis();
		try {
			for (int i = 1; i <= page; i++) {
				int from = (i - 1) * offset;
				int to = Math.min(from + offset, records.size());
				for (int j = from; j < to; j++) {
					log.info("当前更新第{}条到第{}条数据", from, to);
					sqlSession.update(getNameSpace() + ".updateByPrimaryKeySelective", records.get(j));
				}
			}
			log.info("正在提交事务");
			sqlSession.commit();
			long endTime = System.currentTimeMillis();
			log.info("事务更新共{}条,分{}批更新，每批{}条，耗时：{}",
					records.size(), page, offset, new Time(endTime - beginTime).toString());
		} catch (Exception ex) {
			log.info("批量插入出错！");
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}

	@Override
	@CacheFlush
	public void batchUpdate(List<T> records, int offset) {
		String className = records.get(0).getClass().getName();
		String mapperMethodId = ".updateByPrimaryKey";
		if (className.endsWith("WithBLOBs")) {
			mapperMethodId = ".updateByPrimaryKeyWithBLOBs";
		}
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		int page = records.size() / offset + (records.size() % offset == 0 ? 0 : 1);
		long beginTime = System.currentTimeMillis();
		try {
			for (int i = 1; i <= page; i++) {
				int from = (i - 1) * offset;
				int to = Math.min(from + offset, records.size());
				for (int j = from; j < to; j++) {
					log.info("当前更新第{}条数据", j);
					sqlSession.update(getNameSpace() + mapperMethodId, records.get(j));
				}
			}
			log.info("正在提交事务");
			sqlSession.commit();
			long endTime = System.currentTimeMillis();
			log.info("事务更新共{}条,分{}批更新，每批{}条，耗时：{}",
					records.size(), page, offset, new Time(endTime - beginTime).toString());
		} catch (Exception ex) {
			log.info("批量插入出错！");
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}
	// =========================== save =============================

	@Override
	@CacheFlush
	public void save(@NonNull T record) {
		this.save("insert", record);
	}

	@Override
	@CacheFlush
	public void dynamicSave(@NonNull T record) {
		this.save("insertSelective", record);
	}

	@Override
	@CacheFlush
	@SneakyThrows
	public int saveAndRetId(@NonNull T t) {
		if (this.save("saveAndRetId", t) > 0) {
			return BeanUtil.getFieldValue(t, "id", Integer.class);
		} else {
			throw new Exception("save error");
		}
	}

	@Override
	@CacheFlush
	public void batchSave(@NonNull List<T> list) {
		// 防止因为批量插入数量导致无法插入
		batchSave(list, 10000);
	}

	@Override
	public List<T> batchSaveAndRetList(List<T> list) {
		return this.batchSaveAndRetList(list, 10000);
	}

	@Override
	@CacheFlush
	public void batchSave(@NonNull List<T> list, int offset) {
		// 批量操作模式 + 取消自动提交 + 异常全部数据回滚
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		int page = list.size() / offset + (list.size() % offset == 0 ? 0 : 1);
		long beginTime = System.currentTimeMillis();
		try {
			for (int i = 1; i <= page; i++) {
				int from = (i - 1) * offset;
				int to = Math.min(from + offset, list.size());
				log.info("当前插入第{}条到第{}条数据", from, to);
				sqlSession.insert(getNameSpace() + ".batchSave", list.subList(from, to));
			}
			log.info("正在提交事务");
			sqlSession.commit();
			long endTime = System.currentTimeMillis();
			log.info("事务插入共{}条,分{}批插入，每批{}条，耗时：{}",
					list.size(), page, offset, new Time(endTime - beginTime).toString());
		} catch (Exception ex) {
			log.info("批量插入出错！");
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<T> batchSaveAndRetList(List<T> list, int offset) {
		if (list.size() == 0) return CollectionUtil.emptyList;
		// 批量操作模式 + 取消自动提交 + 异常全部数据回滚
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		int page = list.size() / offset + (list.size() % offset == 0 ? 0 : 1);
		long beginTime = System.currentTimeMillis();
		try {
			for (int i = 1; i <= page; i++) {
				int from = (i - 1) * offset;
				int to = Math.min(from + offset, list.size());
				for (int j = from; j < to; j++) {
					log.info("当前插入第{}条数据", j);
					sqlSession.insert(getNameSpace() + ".saveAndRetId", list.get(j));
				}
			}
			log.info("正在提交事务");
			sqlSession.commit();
			long endTime = System.currentTimeMillis();
			log.info("插入共{}条，耗时：{}", list.size(), new Time(endTime - beginTime).toString());
			return list;
		} catch (Exception ex) {
			log.info("批量插入出错！");
			sqlSession.rollback();
			// 向外抛出异常
			throw ex;
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<Integer> batchSaveAndRetIds(List<T> list) {
		List<T> saveList = this.batchSaveAndRetList(list);
		return BeanUtil.beans2Fields(saveList, "id", Integer.class);
	}

	// =========================== query =============================

	@Override
	@CacheAccess
	public List<T> queryList(@NonNull Q query) {
		return this.queryList("queryList", query);
	}

	@Override
	@CacheAccess
	@SneakyThrows
	public PaginationResult<T> queryPage(@NonNull Q query) {
		if (query.getStart() == null || query.getRows() == null) {
			throw new Exception("query rows and start can't be null");
		}
		List<T> data = this.queryList("queryPage", query);
		int total = this.count(query);
		return new PaginationResult<T>(data, total);
	}

	@Override
	@CacheAccess
	public T[] queryArray(@NonNull Q query, @NonNull Class<T> clazz) {
		List<T> list = this.queryList(query);
		return CollectionUtil.list2Array(list, clazz);
	}

	@Override
	@CacheAccess
	public Map<Integer, T> queryMap(@NonNull Q query) {
		List<T> list = this.queryList(query);
		Map<Integer, T> map = new HashMap<>();
		for (T t : list) {
			int id = BeanUtil.getFieldValue(t, "id", Integer.class);
			map.put(id, t);
		}
		return map;
	}

	@Override
	@CacheAccess
	public T queryById(int id) {
		BaseQuery query = new BaseQuery();
		query.setId(id);
		return this.queryOne("queryById", query);
	}

	@Override
	@CacheAccess
	public List<T> queryByIds(@NonNull List<Integer> list) {
		BaseQuery query = new BaseQuery();
		query.setIds(list);
		return this.queryList("queryByIds", query);
	}

	@Override
	@CacheAccess
	@SneakyThrows
	public T queryById(@NonNull Q query) {
		if (query.getId() == null) {
			throw new Exception("query id is null");
		}
		return this.queryOne("queryById", query);
	}

	@Override
	@CacheAccess
	@SneakyThrows
	public List<T> queryByIds(@NonNull Q query) {
		if (CollectionUtil.isEmpty(query.getIds())) {
			throw new Exception("query ids is empty");
		}
		return this.queryList("queryByIds", query);
	}

	@Override
	@CacheAccess
	public int count(@NonNull Q query) {
		return this.queryOne("count", query);
	}

	// ----------------------- mybatis base method ---------------------

	@SneakyThrows
	protected <E> List<E> queryList(@NonNull String id, Object param) {
		return this.getSqlSession().selectList(getNameSpace() + "." + id, param);
	}

	@SneakyThrows
	protected <E> E queryOne(@NonNull String id, Object param) {
		return this.getSqlSession().selectOne(getNameSpace() + "." + id, param);
	}

	@SneakyThrows
	protected int save(@NonNull String id, Object param) {
		return this.getSqlSession().insert(getNameSpace() + "." + id, param);
	}

	@SneakyThrows
	protected int update(@NonNull String id, Object param) {
		return this.getSqlSession().update(getNameSpace() + "." + id, param);
	}

	@SneakyThrows
	protected int delete(@NonNull String id, Object param) {
		return this.getSqlSession().delete(getNameSpace() + "." + id, param);
	}

}
