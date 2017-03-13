package com.wake.dao.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import com.wake.dao.base.helper.MySqlPageBeanHelper;
import com.wake.dao.base.helper.PageBean;

/**
 * @param <T>
 *            实体entity
 * @param <PK>
 *            主键类型
 */
@Transactional("TransactionManager")
public class BaseDao<T, PK extends Serializable> {

	@Resource(name = "SessionFactory")
	private SessionFactory sessionFactory;

	private Class<T> entityClass;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Class getEntityClass() {
		if (entityClass == null) {
			entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		}
		return entityClass;
	}

	private void querySetParameter(Query query, Object... params) {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
	}

	private void querySetParameter(Query query, Map<String, Object> condition) {
		if (condition != null) {
			for (Map.Entry<String, Object> entry : condition.entrySet()) {
				if (entry.getValue() instanceof Collection<?>) {
					query.setParameterList(entry.getKey(), (Collection<?>) entry.getValue());
				} else if (entry.getValue() instanceof Object[]) {
					query.setParameterList(entry.getKey(), (Object[]) entry.getValue());
				} else {
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}
	}
	

	/**
	 * 新增实体持久化
	 * 
	 * @param entity
	 *            实体
	 */
	public void save(T entity) {
		sessionFactory.getCurrentSession().save(entity);
	}

	/**
	 * 新增或更新实体
	 * 
	 * @param entity
	 *            实体
	 */
	public void saveOrUpdate(T entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}

	/**
	 * 删除实体
	 * 
	 * @param entity
	 *            实体
	 */
	public void delete(T entity) {
		sessionFactory.getCurrentSession().delete(entity);
	}

	/**
	 * 根据主键删除实体
	 * 
	 * @param pk
	 *            主键
	 * @return true 删除成功
	 */
	public boolean deleteByPrimaryKey(PK pk) {
		T entity = getByPrimaryKey(pk);
		if (entity != null) {
			delete(entity);
		}
		return true;
	}

	/**
	 * 更新实体
	 * 
	 * @param entity
	 */
	public void update(T entity) {
		sessionFactory.getCurrentSession().update(entity);
	}

	/**
	 * 加载实体（延迟加载）
	 * 
	 * @param pk
	 *            主键
	 * @return 实体entity
	 */
	@SuppressWarnings("unchecked")
	protected T load(PK pk) {
		return (T) sessionFactory.getCurrentSession().load(getEntityClass(), pk);
	}

	/**
	 * 获取实体
	 * 
	 * @param pk
	 * @return 实体entity
	 */
	@SuppressWarnings("unchecked")
	public T getByPrimaryKey(PK pk) {
		return (T) sessionFactory.getCurrentSession().get(getEntityClass(), pk);
	}

	/**
	 * 检查这个对象实例是否与当前的Session关联（即是否为Persistent状态）
	 * 
	 * @param entity
	 *            实体
	 * @return true 为Persistent状态
	 */
	protected boolean contains(T entity) {
		return sessionFactory.getCurrentSession().contains(entity);
	}

	/**
	 * refresh
	 * 
	 * @param t
	 */
	protected void refresh(T entity) {
		sessionFactory.getCurrentSession().refresh(entity);
	}

	/**
	 * HQL查找唯一实体
	 * 
	 * @param hqlString
	 *            HQL语句
	 * @param params
	 *            不定参数数组（setParameter）
	 * @return 实体entity
	 */
	@SuppressWarnings("unchecked")
	protected T getUniqueByHQL(String hqlString, Object... params) {
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		querySetParameter(query, params);
		return (T) query.uniqueResult();
	}

	/**
	 * HQL查找唯一实体
	 * 
	 * @param hqlString
	 * @param condition
	 *            参数map
	 * @return 实体entity
	 */
	@SuppressWarnings("unchecked")
	protected T getUniqueByHQL(String hqlString, Map<String, Object> condition) {
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		querySetParameter(query, condition);
		return (T) query.uniqueResult();
	}

	/**
	 * HQL查找唯一结果
	 * 
	 * @param hqlString
	 * @param params
	 * @return
	 */
	protected Object getUniqueResultByHQL(String hqlString, Object... params) {
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		querySetParameter(query, params);
		return query.uniqueResult();
	}

	/**
	 * HQL查找唯一结果
	 * 
	 * @param hqlString
	 * @param params
	 * @return
	 */
	protected Object getUniqueResultByHQL(String hqlString, Map<String, Object> condition) {
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		querySetParameter(query, condition);
		return query.uniqueResult();
	}

	/**
	 * SQL查找唯一结果
	 * 
	 * @param sqlString
	 * @param params
	 * @return
	 */
	protected Object getUniqueResultBySQL(String sqlString, Object... params) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlString);
		querySetParameter(query, params);
		return query.uniqueResult();
	}

	/**
	 * SQL查找唯一结果
	 * 
	 * @param sqlString
	 * @param params
	 * @return
	 */
	protected Object getUniqueResultBySQL(String sqlString, Map<String, Object> condition) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlString);
		querySetParameter(query, condition);
		return query.uniqueResult();
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> listAll() {
		String hql = "from " + getEntityClass().getSimpleName();
		return getListByHQL(hql);
	}

	/**
	 * HQL查询list
	 * 
	 * @param hqlString
	 * @param params
	 *            不定参数数组
	 * @return 结果集
	 */
	@SuppressWarnings("rawtypes")
	protected List getListByHQL(String hqlString, Object... params) {
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		querySetParameter(query, params);
		return query.list();
	}

	/**
	 * HQL查询list
	 * 
	 * @param hqlString
	 * @param condition
	 *            参数map
	 * @return 结果集
	 */
	@SuppressWarnings("rawtypes")
	protected List getListByHQL(String hqlString, Map<String, Object> condition) {
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		querySetParameter(query, condition);
		return query.list();
	}

	/**
	 * SQL查询list
	 * 
	 * @param sqlString
	 * @param params
	 *            不定参数数组
	 * @return 结果集
	 */
	@SuppressWarnings("rawtypes")
	protected List getListBySQL(String sqlString, Object... params) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlString);
		querySetParameter(query, params);
		return query.list();
	}

	/**
	 * SQL查询list
	 * 
	 * @param sqlString
	 * @param condition
	 *            参数map
	 * @return 结果集
	 */
	@SuppressWarnings("rawtypes")
	protected List getListBySQL(String sqlString, Map<String, Object> condition) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlString);
		querySetParameter(query, condition);
		return query.list();
	}

	/**
	 * SQL查询list
	 * 
	 * @param sqlString
	 * @param params
	 *            不定参数数组
	 * @return 结果集(entity)
	 */
	@SuppressWarnings("rawtypes")
	protected List getListBySQL(String sqlString, Class clazz, Object... params) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlString).addEntity(clazz);
		querySetParameter(query, params);
		return query.list();
	}

	/**
	 * SQL查询list
	 * 
	 * @param sqlString
	 * @param condition
	 *            参数map
	 * @return 结果集(entity)
	 */
	@SuppressWarnings("rawtypes")
	protected List getListBySQL(String sqlString, Class clazz, Map<String, Object> condition) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlString).addEntity(clazz);
		querySetParameter(query, condition);
		return query.list();
	}

	/**
	 * HQL获取结果count
	 * 
	 * @param hqlString
	 * @param params
	 *            不定参数数组
	 * @return count
	 */
	protected int getCountByHQL(String hqlString, Object... params) {
		hqlString = MySqlPageBeanHelper.formatQueryString(hqlString);
		if (hqlString.toLowerCase().indexOf("select count") == -1) {
			hqlString = MySqlPageBeanHelper.getCountString(hqlString);
		}
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		querySetParameter(query, params);
		Object result = query.uniqueResult();
		return result == null ? 0 : ((Long) result).intValue();
	}

	/**
	 * HQL获取结果count
	 * 
	 * @param hqlString
	 * @param params
	 *            不定参数数组
	 * @return count
	 */
	protected int getCountByHQL(String hqlString, Map<String, Object> condition) {
		hqlString = MySqlPageBeanHelper.formatQueryString(hqlString);
		if (hqlString.toLowerCase().indexOf("select count") == -1) {
			hqlString = MySqlPageBeanHelper.getCountString(hqlString);
		}
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		querySetParameter(query, condition);
		Object result = query.uniqueResult();
		return result == null ? 0 : ((Long) result).intValue();
	}

	/**
	 * SQL获取结果count
	 * 
	 * @param sqlString
	 * @param params
	 *            不定参数数组
	 * @return count
	 */
	protected int getCountBySQL(String sqlString, Object... params) {
		sqlString = MySqlPageBeanHelper.formatQueryString(sqlString);
		if (sqlString.toLowerCase().indexOf("select count") == -1) {
			sqlString = MySqlPageBeanHelper.getCountString(sqlString);
		}
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlString);
		querySetParameter(query, params);
		Object result = query.uniqueResult();
		return result == null ? 0 : ((BigInteger) result).intValue();
	}

	/**
	 * SQL获取结果count
	 * 
	 * @param sqlString
	 * @param condition
	 *            参数map
	 * @return count
	 */
	protected int getCountBySQL(String sqlString, Map<String, Object> condition) {
		sqlString = MySqlPageBeanHelper.formatQueryString(sqlString);
		if (sqlString.toLowerCase().indexOf("select count") == -1) {
			sqlString = MySqlPageBeanHelper.getCountString(sqlString);
		}
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlString);
		querySetParameter(query, condition);
		Object result = query.uniqueResult();
		return result == null ? 0 : ((BigInteger) result).intValue();
	}

	/**
	 * hql分页查询
	 * 
	 * @param pageBean
	 * @param hqlString
	 * @param params
	 *            不定参数数组
	 * @return 结果集
	 */
	@SuppressWarnings("rawtypes")
	protected List queryPageListByHQL(PageBean pageBean, String hqlString, Object... params) {
		if (pageBean == null) {
			return getListByHQL(hqlString, params);
		} else {
			Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
			querySetParameter(query, params);
			pageBean.setTotalCount(getCountByHQL(hqlString, params));
			query.setFirstResult(pageBean.getOffset());
			query.setMaxResults(pageBean.getPageSize());
			return query.list();
		}
	}

	/**
	 * HQL分页查询
	 * 
	 * @param pageBean
	 * @param hqlString
	 * @param condition
	 *            参数map
	 * @return 结果集
	 */
	@SuppressWarnings("rawtypes")
	protected List queryPageListByHQL(PageBean pageBean, String hqlString, Map<String, Object> condition) {
		if (pageBean == null) {
			return getListByHQL(hqlString, condition);
		} else {
			Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
			querySetParameter(query, condition);
			pageBean.setTotalCount(getCountByHQL(hqlString, condition));
			query.setFirstResult(pageBean.getOffset());
			query.setMaxResults(pageBean.getPageSize());
			return query.list();
		}
	}

	/**
	 * SQL分页查询
	 * 
	 * @param pageBean
	 * @param sqlString
	 * @param params
	 *            不定参数数组
	 * @return 结果集
	 */
	@SuppressWarnings("rawtypes")
	protected List queryPageListBySQL(PageBean pageBean, String sqlString, Object... params) {
		if (pageBean == null) {
			return getListBySQL(sqlString, params);
		} else {
			Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlString);
			querySetParameter(query, params);
			pageBean.setTotalCount(getCountBySQL(sqlString, params));
			query.setFirstResult(pageBean.getOffset());
			query.setMaxResults(pageBean.getPageSize());
			return query.list();
		}
	}

	/**
	 * SQL分页查询
	 * 
	 * @param pageBean
	 * @param sqlString
	 * @param condition
	 *            参数map
	 * @return 结果集
	 */
	@SuppressWarnings("rawtypes")
	protected List queryPageListBySQL(PageBean pageBean, String sqlString, Map<String, Object> condition) {
		if (pageBean == null) {
			return getListBySQL(sqlString, condition);
		} else {
			Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlString);
			querySetParameter(query, condition);
			pageBean.setTotalCount(getCountBySQL(sqlString, condition));
			query.setFirstResult(pageBean.getOffset());
			query.setMaxResults(pageBean.getPageSize());
			return query.list();
		}
	}

	/**
	 * sql查询 返回List<Map<String,Object>>
	 * 
	 * @param pageBean
	 *            分页，如不需要分页，传null
	 * @param sql
	 *            sql语句
	 * @param params
	 *            不定参数数组
	 * @return 结果集 List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> getResultToListMap(PageBean pageBean, String sql, Object... params) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		querySetParameter(query, params);
		if (pageBean != null) {
			pageBean.setTotalCount(getCountBySQL(sql, params));
			query.setFirstResult(pageBean.getOffset());
			query.setMaxResults(pageBean.getPageSize());
		}
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	/**
	 * sql查询 返回List<Map<String,Object>>
	 * 
	 * @param pageBean
	 *            分页，如不需要分页，传null
	 * @param sql
	 *            sql语句
	 * @param condition
	 *            参数map
	 * @return 结果集 List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> getResultToListMap(PageBean pageBean, String sql,
			Map<String, Object> condition) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		querySetParameter(query, condition);
		if (pageBean != null) {
			pageBean.setTotalCount(getCountBySQL(sql, condition));
			query.setFirstResult(pageBean.getOffset());
			query.setMaxResults(pageBean.getPageSize());
		}
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

}
