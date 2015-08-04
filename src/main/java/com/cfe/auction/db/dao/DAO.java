package com.cfe.auction.db.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.cfe.auction.model.persist.PO;

public interface DAO<I extends Serializable, T extends PO<I>> {
	public T get(I id);
	
	public <T> List<T> findAll(Class<T> persistentClass);
	public T create(T po);
	
//	public T createOrUpdate(T po);
	
	public T update(T po);
	
	public int countbyobject(I id,String fieldName) ;
	public void delete(T po);
	
	public int deleteById(I id);
	
	public <C extends Collection<T>> C create(C pos);
	
public <C extends Collection<T>> C createOrUpdate(C pos);
	
	public <C extends Collection<T>> C update(C pos);
	
	public <C extends Collection<T>> void delete(C pos);
	
	public <C extends Collection<I>> void deleteByIds(C ids);
	
	public List<T> listAll();
}
