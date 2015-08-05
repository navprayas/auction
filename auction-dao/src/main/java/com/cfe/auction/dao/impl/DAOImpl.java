package com.cfe.auction.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.cfe.auction.dao.DAO;
import com.cfe.auction.dao.model.persist.PO;

public abstract class DAOImpl<I extends Serializable, T extends PO<I>> implements DAO<I, T>, InitializingBean {

	@Autowired
	private SessionFactory sessionFactory;
	private Class<T> poClass;
	private String entityName;
	
	
	
	@Override
	public T get(I id) {
		return (T)sessionFactory.getCurrentSession().get(poClass, id);
	}
	
	@Override
	 @SuppressWarnings("unchecked")
	    public <T> List<T> findAll(Class<T> persistentClass) {
	        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(persistentClass);
	       
	        List<T> res = criteria.list();
	        return res;
	    }
	
	
	/*@Override
	 @SuppressWarnings("unchecked")
	    public <T> List<T> findAllById(Class<T> persistentClass,I id) {
	        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(persistentClass,id);
	       
	        List<T> res = criteria.list();
	        return res;
	    }*/

	@Override
	public T create(T po) {
		sessionFactory.getCurrentSession().save(po);
		return po;
	}

	/*@Override
	public T createOrUpdate(T po) {
		sessionFactory.getCurrentSession().saveOrUpdate(po);
		return po;
	}
*/
	@Override
	public T update(T po) {
		sessionFactory.getCurrentSession().update(po);
		return po;
	}

	@Override
	public void delete(T po) {
		sessionFactory.getCurrentSession().delete(po);
		
	}

	@Override
	public int deleteById(I id) {
		Query query = sessionFactory.getCurrentSession().createQuery("delete "+entityName+" where id = :id");
		query.setParameter("id", id);
		return query.executeUpdate();
		
	}
	
	@Override
	public int countbyobject(I id,String fieldName) {
		Query query = sessionFactory.getCurrentSession().createQuery("select count(*)from "+entityName+" as ob where ob."+fieldName+"  = :id");
		query.setParameter("id", id);
		return ((Number) query.uniqueResult()).intValue();
		
	}

	@Override
	public <C extends Collection<T>> C create(C pos) {
		for(T po: pos)
			create(po);
		return pos;
	}
	
	

	@Override
public <C extends Collection<T>> C createOrUpdate(C pos) {
		for(T po: pos)
			sessionFactory.getCurrentSession().saveOrUpdate(po);
		return pos;
	}

	@Override
	public <C extends Collection<T>> C update(C pos) {
		for(T po: pos)
			update(po);
		return pos;
	}

	@Override
	public <C extends Collection<T>> void delete(C pos) {
		for(T po: pos)
			delete(po);
	}

	@Override
	public <C extends Collection<I>> void deleteByIds(C ids) {
		for(I id: ids)
			deleteById(id);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		poClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		entityName = poClass.getSimpleName();
	}
	
	protected SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
	public List<T> listAll(){
		Query query = sessionFactory.getCurrentSession().createQuery("from "+entityName);
		return (List<T>)query.list();
	}

}
