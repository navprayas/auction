package com.cfe.auction.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.DAO;
import com.cfe.auction.dao.model.persist.PO;
import com.cfe.auction.service.CRUDService;

public class CRUDServiceImpl<I extends Serializable, T extends PO<I>, D extends DAO<I, T>> implements CRUDService<I, T>{
	protected D dao;
	
	public CRUDServiceImpl(D dao){
		this.dao = dao;
	}

	@Override
	@Transactional
	public T create(T po) {
		return dao.create(po);
	}
	
	@Override
	@Transactional
	public List<T> findAll(Class<T> persistentClass){
		return dao.findAll(persistentClass);
	}
	
	

	@Override
	@Transactional
	public T read(I id) {
		return dao.get(id);
	}

	@Override @Transactional
	public T update(T po) {
		return dao.update(po);
	}

	@Override @Transactional
	public void delete(T po) {
		dao.delete(po);
	}
	
	@Override @Transactional
	public int countbyobject(I id,String fieldName){
		
		return dao.countbyobject(id, fieldName);
	}

	@Override @Transactional
	public int deleteById(I id) {
		return dao.deleteById(id);
	}

	@Override @Transactional
	public <C extends Collection<T>> C create(C pos) {
		return dao.create(pos);
	}

	@Override @Transactional
	public <C extends Collection<T>> C update(C pos) {
		return dao.update(pos);
	}

	@Override @Transactional
	public <C extends Collection<T>> void delete(C pos) {
		dao.delete(pos);
	}

	@Override @Transactional
	public <C extends Collection<I>> void deleteByIds(C ids) {
		dao.deleteByIds(ids);
	}
	
	@Override @Transactional
	public List<T> listAll(){
		return dao.listAll();
	}

}
