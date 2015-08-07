package com.cfe.auction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.CategoryDao;
import com.cfe.auction.model.persist.Category;
import com.cfe.auction.service.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl extends
		CRUDServiceImpl<Integer, Category, CategoryDao> implements
		CategoryService {
	@Autowired
	public CategoryServiceImpl(CategoryDao dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

}
