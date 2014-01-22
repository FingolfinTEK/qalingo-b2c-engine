/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.dao.impl;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hoteia.qalingo.core.dao.CartDao;
import org.hoteia.qalingo.core.domain.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("cartDao")
public class CartDaoImpl extends AbstractGenericDaoImpl implements CartDao {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public Cart getCartById(final Long cartId) {
        Criteria criteria = createDefaultCriteria(Cart.class);
        
        addDefaultFetch(criteria);
        
        criteria.add(Restrictions.eq("id", cartId));
        Cart cart = (Cart) criteria.uniqueResult();
        return cart;
	}

	public Cart saveOrUpdateCart(final Cart cart) {
		if(cart.getDateCreate() == null){
			cart.setDateCreate(new Date());
		}
		cart.setDateUpdate(new Date());
        if (cart.getId() != null) {
            if(em.contains(cart)){
                em.refresh(cart);
            }
            Cart mergedCart = em.merge(cart);
            em.flush();
            return mergedCart;
        } else {
            em.persist(cart);
            return cart;
        }
	}

	public void deleteCart(final Cart cart) {
	    if(em.contains(cart)){
	        cart.deleteAllCartItem();
	        em.remove(cart);
	    } else {
            cart.deleteAllCartItem();
	        em.remove(em.merge(cart));
	    }
	}
	   
    private void addDefaultFetch(Criteria criteria) {
        criteria.setFetchMode("session", FetchMode.JOIN);
        criteria.setFetchMode("cartItems", FetchMode.JOIN);

        criteria.createAlias("cartItems.productSku.productSkuAttributes", "productSkuAttributes", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("productSkuAttributes", FetchMode.JOIN);

        criteria.createAlias("cartItems.productSku.assets", "productSkuAssets", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("productSkuAssets", FetchMode.JOIN);

        criteria.createAlias("cartItems.productSku.prices", "productSkuPrices", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("productSkuPrices", FetchMode.JOIN);

        criteria.createAlias("cartItems.productSku.stocks", "productSkuStocks", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("productSkuStocks", FetchMode.JOIN);

        criteria.createAlias("cartItems.productMarketing", "productMarketing", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("productMarketing", FetchMode.JOIN);

        criteria.createAlias("cartItems.productMarketing.productMarketingAttributes", "productMarketingAttributes", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("productMarketingAttributes", FetchMode.JOIN);

        criteria.createAlias("cartItems.productMarketing.assets", "productMarketingAssets", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("productMarketingAssets", FetchMode.JOIN);

        criteria.createAlias("cartItems.catalogCategory.catalogCategoryAttributes", "catalogCategoryAttributes", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("catalogCategoryAttributes", FetchMode.JOIN);

        criteria.createAlias("cartItems.catalogCategory.assets", "catalogCategoryAssets", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("catalogCategoryAssets", FetchMode.JOIN);

        criteria.setFetchMode("shippings", FetchMode.JOIN);
    }
    
}