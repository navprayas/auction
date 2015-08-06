package com.cfe.auction.dao.impl;

import org.springframework.stereotype.Service;

import com.cfe.auction.dao.CategoryDao;
import com.cfe.auction.model.persist.Category;
@Service
public class CategoryDaoImpl extends DAOImpl<Integer, Category> implements
		CategoryDao {

}
