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

import org.hoteia.qalingo.core.dao.CartDao;
import org.hoteia.qalingo.core.domain.Cart;
import org.hoteia.qalingo.core.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("cartService")
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDao cartDao;

    public Cart addToCart(Cart cart) {
        saveOrUpdateCart(cart);
        return cart;
    }

    public Cart updateToCart(Cart cart) {
        saveOrUpdateCart(cart);
        return cart;
    }

    public Cart deleteToCart(Cart cart) {
        saveOrUpdateCart(cart);
        return cart;
    }

    public Cart getCartById(Long cartId) {
        return cartDao.getCartById(cartId);
    }

    public Cart getCartById(String rawCartId) {
        long cartId = -1;
        try {
            cartId = Long.parseLong(rawCartId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
        return getCartById(cartId);
    }

    public void saveOrUpdateCart(Cart cart) {
        cartDao.saveOrUpdateCart(cart);
    }

    public void deleteCart(Cart cart) {
        cartDao.deleteCart(cart);
    }

}