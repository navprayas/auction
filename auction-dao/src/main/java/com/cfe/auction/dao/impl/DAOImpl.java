package com.cfe.auction.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;

import com.cfe.auction.dao.DAO;
import com.cfe.auction.model.persist.PO;

public abstract class DAOImpl<I extends Serializable, T extends PO<I>>
		implements DAO<I, T>, InitializingBean {

	@PersistenceContext(unitName = "PersistenceUnitA")
	private EntityManager entityManagerA;

	@PersistenceContext(unitName = "PersistenceUnitB")
	private EntityManager entityManagerB;

	/*
	 * @Autowired
	 * 
	 * @Qualifier("sessionFactoryZin") private SessionFactory sessionFactory1;
	 */
	private Class<T> poClass;
	private String entityName;

	@Override
	public T get(I id) {
		return (T) getEntityManager().find(poClass, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> persistentClass) {
		Query query = getMasterEntityManager()
				.createQuery("from " + entityName);
		return (List<T>) query.getResultList();
	}

	/*
	 * @Override
	 * 
	 * @SuppressWarnings("unchecked") public <T> List<T> findAllById(Class<T>
	 * persistentClass,I id) { Criteria criteria =
	 * sessionFactory1.getCurrentSession().createCriteria(persistentClass,id);
	 * 
	 * List<T> res = criteria.list(); return res; }
	 */

	@Override
	public T create(T po) {
		getEntityManager().persist(po);
		return po;
	}

	@Override
	public T createOrUpdate(T po) {
		getEntityManager().merge(po);
		return po;
	}

	@Override
	public T update(T po) {
		getEntityManager().merge(po);
		return po;
	}

	@Override
	public void delete(T po) {
		getEntityManager().remove(po);

	}

	@Override
	public int deleteById(I id) {
		Query query = getEntityManager().createQuery(
				"delete " + entityName + " where id = :id");
		query.setParameter("id", id);
		return query.executeUpdate();

	}

	@Override
	public int countbyobject(I id, String fieldName) {
		Query query = getEntityManager().createQuery(
				"select count(*)from " + entityName + " as ob where ob."
						+ fieldName + "  = :id");
		query.setParameter("id", id);
		return ((Number) query.getSingleResult()).intValue();

	}

	@Override
	public <C extends Collection<T>> C create(C pos) {
		for (T po : pos)
			create(po);
		return pos;
	}

	@Override
	public <C extends Collection<T>> C createOrUpdate(C pos) {
		for (T po : pos)
			getEntityManager().merge(po);
		return pos;
	}

	@Override
	public <C extends Collection<T>> C update(C pos) {
		for (T po : pos)
			update(po);
		return pos;
	}

	@Override
	public <C extends Collection<T>> void delete(C pos) {
		for (T po : pos)
			delete(po);
	}

	@Override
	public <C extends Collection<I>> void deleteByIds(C ids) {
		for (I id : ids)
			deleteById(id);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		poClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
		entityName = poClass.getSimpleName();
	}

	/*
	 * protected SessionFactory getSessionFactory() { return sessionFactory1; }
	 */

	protected EntityManager getEntityManager() {
		return entityManagerB;
	}
	protected EntityManager getEntityManager(String schemaKey) {
		if (schemaKey.equals("PersistenceUnitB")) {
			return entityManagerB;
		}
		return null;
	}
	
	protected EntityManager getMasterEntityManager() {
		return entityManagerA;
	}

	public List<T> listAll() {
		Query query = getEntityManager().createQuery("from " + entityName);
		return (List<T>) query.getResultList();
	}

}
