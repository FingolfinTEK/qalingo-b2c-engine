/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.hoteia.qalingo.core.dao.CatalogCategoryDao;
import org.hoteia.qalingo.core.domain.CatalogCategoryMaster;
import org.hoteia.qalingo.core.domain.CatalogCategoryVirtual;
import org.hoteia.qalingo.core.domain.ProductMarketing;
import org.hoteia.qalingo.core.service.CatalogCategoryService;

@Service("catalogCategoryService")
@Transactional
public class CatalogCategoryServiceImpl implements CatalogCategoryService {

	@Autowired
	private CatalogCategoryDao catalogCategoryDao;

	// MASTER
	public CatalogCategoryMaster getMasterCatalogCategoryById(final String rawCatalogCategoryId) {
		long catalogCategoryId = -1;
		try {
			catalogCategoryId = Long.parseLong(rawCatalogCategoryId);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e);
		}
		return catalogCategoryDao.getMasterCatalogCategoryById(catalogCategoryId);
	}
	
	public CatalogCategoryMaster getMasterCatalogCategoryByCode(final String catalogCategoryCode) {
		return catalogCategoryDao.getMasterCatalogCategoryByCode(catalogCategoryCode);
	}
	
	public CatalogCategoryMaster getMasterCatalogCategoryByCode(final Long marketAreaId, final String catalogCategoryCode) {
		return catalogCategoryDao.getMasterCatalogCategoryByCode(marketAreaId, catalogCategoryCode);
	}
	
	public List<CatalogCategoryMaster> findRootMasterCatalogCategories(final Long marketAreaId) {
		List<CatalogCategoryMaster> categories = catalogCategoryDao.findRootCatalogCategories();
		return orderCategoryMasterList(marketAreaId, categories);
	}
	
	public List<CatalogCategoryMaster> findMasterCategoriesByMarketIdAndRetailerId(final Long marketAreaId) {
		return catalogCategoryDao.findMasterCategoriesByMarketIdAndRetailerId(marketAreaId);
	}
	
	public List<CatalogCategoryMaster> orderCategoryMasterList(final Long marketAreaId, final List<CatalogCategoryMaster> categories){
		List<CatalogCategoryMaster> sortedObjects = new LinkedList<CatalogCategoryMaster>(categories);
		Collections.sort(sortedObjects, new Comparator<CatalogCategoryMaster>() {
			@Override
			public int compare(CatalogCategoryMaster o1, CatalogCategoryMaster o2) {
				if(o1 != null
						&& o2 != null){
					Integer order1 = o1.getOrder(marketAreaId);
					Integer order2 = o2.getOrder(marketAreaId);
					if(order1 != null
							&& order2 != null){
						return order1.compareTo(order2);				
					} else {
						return o1.getId().compareTo(o2.getId());	
					}
				}
				return 0;
			}
		});
		return sortedObjects;
	}
	
	public void saveOrUpdateCatalogCategory(CatalogCategoryMaster catalogCategory) {
		catalogCategoryDao.saveOrUpdateCatalogCategory(catalogCategory);
	}

	public void deleteCatalogCategory(CatalogCategoryMaster catalogCategory) {
		catalogCategoryDao.deleteCatalogCategory(catalogCategory);
	}
	
	// VIRTUAL
	public CatalogCategoryVirtual getVirtualCatalogCategoryById(final String rawCatalogCategoryId) {
		long catalogCategoryId = -1;
		try {
			catalogCategoryId = Long.parseLong(rawCatalogCategoryId);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e);
		}
		return catalogCategoryDao.getVirtualCatalogCategoryById(catalogCategoryId);
	}
	
	public CatalogCategoryVirtual getVirtualCatalogCategoryByCode(final String catalogCategoryCode) {
		return catalogCategoryDao.getVirtualCatalogCategoryByCode(catalogCategoryCode);
	}
	
	public CatalogCategoryVirtual getVirtualCatalogCategoryByCode(final Long marketAreaId, final String catalogCategoryCode) {
		return catalogCategoryDao.getVirtualCatalogCategoryByCode(marketAreaId, catalogCategoryCode);
	}

	public CatalogCategoryVirtual getDefaultVirtualCatalogCategoryByProductMarketing(final Long marketAreaId, final ProductMarketing productMarketing) {
		List<CatalogCategoryVirtual> categories = catalogCategoryDao.findCatalogCategoriesByProductMarketingId(marketAreaId, productMarketing.getId());
		CatalogCategoryVirtual catalogCategoryVirtual = null;
		if(categories != null){
			for (Iterator<CatalogCategoryVirtual> iterator = categories.iterator(); iterator.hasNext();) {
				CatalogCategoryVirtual catalogCategoryVirtualIterator = (CatalogCategoryVirtual) iterator.next();
				if(catalogCategoryVirtualIterator.isDefault()){
					catalogCategoryVirtual = catalogCategoryVirtualIterator;
				}
			}
			if(categories.size() > 0
					&& catalogCategoryVirtual == null){
				catalogCategoryVirtual = categories.iterator().next();
			}
		}
		return catalogCategoryVirtual;
	}
	
	public List<CatalogCategoryVirtual> findRootVirtualCatalogCategories(final Long marketAreaId) {
		List<CatalogCategoryVirtual> categories = catalogCategoryDao.findRootCatalogCategories(marketAreaId);
		return orderCategoryVirtualList(marketAreaId, categories);
	}
	
	public List<CatalogCategoryVirtual> findVirtualCategories(final Long marketAreaId) {
		List<CatalogCategoryVirtual> categories = catalogCategoryDao.findCatalogCategories(marketAreaId);
		return orderCategoryVirtualList(marketAreaId, categories);
	}

	public List<CatalogCategoryVirtual> findVirtualCategoriesByProductMarketingId(final Long marketAreaId, final Long productMarketingId) {
		return catalogCategoryDao.findCatalogCategoriesByProductMarketingId(marketAreaId, productMarketingId);
	}
	
	public List<CatalogCategoryVirtual> orderCategoryVirtualList(final Long marketAreaId, final List<CatalogCategoryVirtual> categories){
		List<CatalogCategoryVirtual> sortedObjects = new LinkedList<CatalogCategoryVirtual>(categories);
		Collections.sort(sortedObjects, new Comparator<CatalogCategoryVirtual>() {
			@Override
			public int compare(CatalogCategoryVirtual o1, CatalogCategoryVirtual o2) {
				if(o1 != null
						&& o2 != null){
					Integer order1 = o1.getOrder(marketAreaId);
					Integer order2 = o2.getOrder(marketAreaId);
					if(order1 != null
							&& order2 != null){
						return order1.compareTo(order2);				
					} else {
						return o1.getId().compareTo(o2.getId());	
					}
				}
				return 0;
			}
		});
		return sortedObjects;
	}
	
	public void saveOrUpdateCatalogCategory(CatalogCategoryVirtual catalogCategory) {
		catalogCategoryDao.saveOrUpdateCatalogCategory(catalogCategory);
	}

	public void deleteCatalogCategory(CatalogCategoryVirtual catalogCategory) {
		catalogCategoryDao.deleteCatalogCategory(catalogCategory);
	}

}