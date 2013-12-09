/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.web.mvc.viewbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductSkuViewBean extends AbstractViewBean implements Serializable {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -5163066749293135126L;

    protected Long id;
    protected String code;
    protected String businessName;
    protected String description;
    protected String backgroundImage;
    protected String carouselImage;
    protected String iconImage;

    protected String i18nName;
    
    protected int positionItem;
    protected boolean isDefault;

    protected Map<String, String> globalAttributes = new HashMap<String, String>();
    protected Map<String, String> marketAreaAttributes = new HashMap<String, String>();
    
    protected ProductMarketingViewBean productMarketing;
    protected List<AssetViewBean> assets = new ArrayList<AssetViewBean>();
    
    protected String addToCartUrl;
    protected String removeFromCartUrl;
    protected String addToWishlistUrl;
    protected String removeFromWishlistUrl;

    protected String detailsUrl;
    protected String editUrl;
    
    protected String createdDate;
    protected String updatedDate;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }

    public String getBusinessName() {
        return businessName;
    }
    
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getCarouselImage() {
        return carouselImage;
    }

    public void setCarouselImage(String carouselImage) {
        this.carouselImage = carouselImage;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }
    
    public String getI18nName() {
        return i18nName;
    }
    
    public void setI18nName(String i18nName) {
        this.i18nName = i18nName;
    }

    public int getPositionItem() {
        return positionItem;
    }

    public void setPositionItem(int positionItem) {
        this.positionItem = positionItem;
    }
    
    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Map<String, String> getGlobalAttributes() {
        return globalAttributes;
    }
    
    public void setGlobalAttributes(Map<String, String> globalAttributes) {
        this.globalAttributes = globalAttributes;
    }
    
    public Map<String, String> getMarketAreaAttributes() {
        return marketAreaAttributes;
    }
    
    public void setMarketAreaAttributes(Map<String, String> marketAreaAttributes) {
        this.marketAreaAttributes = marketAreaAttributes;
    }
    
    public ProductMarketingViewBean getProductMarketing() {
        return productMarketing;
    }
    
    public void setProductMarketing(ProductMarketingViewBean productMarketing) {
        this.productMarketing = productMarketing;
    }
    
    public List<AssetViewBean> getAssets() {
        return assets;
    }
    
    public void setAssets(List<AssetViewBean> assets) {
        this.assets = assets;
    }
    
    public String getAddToCartUrl() {
        return addToCartUrl;
    }

    public void setAddToCartUrl(String addToCartUrl) {
        this.addToCartUrl = addToCartUrl;
    }

    public String getRemoveFromCartUrl() {
        return removeFromCartUrl;
    }

    public void setRemoveFromCartUrl(String removeFromCartUrl) {
        this.removeFromCartUrl = removeFromCartUrl;
    }

    public String getAddToWishlistUrl() {
        return addToWishlistUrl;
    }

    public void setAddToWishlistUrl(String addToWishlistUrl) {
        this.addToWishlistUrl = addToWishlistUrl;
    }

    public String getRemoveFromWishlistUrl() {
        return removeFromWishlistUrl;
    }

    public void setRemoveFromWishlistUrl(String removeFromWishlistUrl) {
        this.removeFromWishlistUrl = removeFromWishlistUrl;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public String getEditUrl() {
        return editUrl;
    }

    public void setEditUrl(String editUrl) {
        this.editUrl = editUrl;
    }
    
    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

}