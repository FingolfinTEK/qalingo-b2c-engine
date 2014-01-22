/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.web.mvc.factory.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.hoteia.qalingo.core.Constants;
import org.hoteia.qalingo.core.RequestConstants;
import org.hoteia.qalingo.core.domain.Asset;
import org.hoteia.qalingo.core.domain.CatalogCategoryMaster;
import org.hoteia.qalingo.core.domain.CatalogCategoryVirtual;
import org.hoteia.qalingo.core.domain.CatalogVirtual;
import org.hoteia.qalingo.core.domain.Localization;
import org.hoteia.qalingo.core.domain.MarketArea;
import org.hoteia.qalingo.core.domain.ProductBrand;
import org.hoteia.qalingo.core.domain.ProductMarketing;
import org.hoteia.qalingo.core.domain.ProductSku;
import org.hoteia.qalingo.core.domain.enumtype.FoUrls;
import org.hoteia.qalingo.core.domain.enumtype.ImageSize;
import org.hoteia.qalingo.core.i18n.enumtype.ScopeWebMessage;
import org.hoteia.qalingo.core.pojo.RequestData;
import org.hoteia.qalingo.core.solr.bean.ProductMarketingSolr;
import org.hoteia.qalingo.core.solr.response.ProductMarketingResponseBean;
import org.hoteia.qalingo.core.web.mvc.factory.FrontofficeViewBeanFactory;
import org.hoteia.qalingo.core.web.mvc.viewbean.CatalogBreadcrumbViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.CatalogCategoryViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.MenuViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.ProductBrandViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.ProductMarketingViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.RecentProductViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.SearchFacetViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.SearchProductItemViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.SearchViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.StoreLocatorCityFilterBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.StoreLocatorCountryFilterBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.StoreLocatorFilterBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.StoreLocatorViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.StoreViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.ValueBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 */
@Service("frontofficeViewBeanFactory")
@Transactional
public class FrontofficeViewBeanFactoryImpl extends ViewBeanFactoryImpl implements FrontofficeViewBeanFactory {

    /**
     * 
     */
    @Override
    public List<MenuViewBean> buildMenuViewBeans(final RequestData requestData) throws Exception {
        final HttpServletRequest request = requestData.getRequest();
        final MarketArea marketArea = requestData.getMarketArea();
        final Localization localization = requestData.getMarketAreaLocalization();

        final Locale locale = localization.getLocale();
        final String localeCode = localization.getCode();

        List<MenuViewBean> menuViewBeans = new ArrayList<MenuViewBean>();

        MenuViewBean menu = new MenuViewBean();
        menu.setName(getSpecificMessage(ScopeWebMessage.HEADER_MENU, "home", locale));
        menu.setUrl(urlService.generateUrl(FoUrls.HOME, requestData));
        menuViewBeans.add(menu);

        CatalogVirtual catalogVirtual = catalogService.getVirtualCatalogbyMarketAreaId(marketArea.getId());
        if (catalogVirtual != null) {
            final List<CatalogCategoryVirtual> catalogCategories = catalogVirtual.getCatalogCategories(marketArea.getId());
            if (catalogCategories != null) {
                for (Iterator<CatalogCategoryVirtual> iteratorCatalogCategory = catalogCategories.iterator(); iteratorCatalogCategory.hasNext();) {
                    final CatalogCategoryVirtual catalogCategory = (CatalogCategoryVirtual) iteratorCatalogCategory.next();
                    final CatalogCategoryVirtual catalogCategoryReloaded = catalogCategoryService.getVirtualCatalogCategoryByCode(catalogCategory.getCode());
                    
                    menu = new MenuViewBean();
                    final String seoCatalogCategoryName = catalogCategoryReloaded.getI18nName(localeCode);
                    menu.setName(seoCatalogCategoryName);
                    menu.setUrl(urlService.generateUrl(FoUrls.CATEGORY_AS_AXE, requestData, catalogCategoryReloaded));

                    List<CatalogCategoryVirtual> subCatalogCategories = catalogCategoryReloaded.getCatalogCategories(marketArea.getId());
                    if (subCatalogCategories != null) {
                        List<MenuViewBean> subMenus = new ArrayList<MenuViewBean>();
                        for (Iterator<CatalogCategoryVirtual> iteratorSubCatalogCategory = subCatalogCategories.iterator(); iteratorSubCatalogCategory.hasNext();) {
                            final CatalogCategoryVirtual subCatalogCategory = (CatalogCategoryVirtual) iteratorSubCatalogCategory.next();
                            final CatalogCategoryVirtual subCatalogCategoryReloaded = catalogCategoryService.getVirtualCatalogCategoryByCode(subCatalogCategory.getCode());
                            final MenuViewBean subMenu = new MenuViewBean();
                            final String seoSubCatalogCategoryName = catalogCategoryReloaded.getI18nName(localeCode) + " " + subCatalogCategoryReloaded.getI18nName(localeCode);
                            subMenu.setName(seoSubCatalogCategoryName);
                            subMenu.setUrl(urlService.generateUrl(FoUrls.CATEGORY_AS_LINE, requestData, subCatalogCategory));
                            subMenus.add(subMenu);
                        }
                        menu.setSubMenus(subMenus);
                    }
                    menuViewBeans.add(menu);
                }
            }
        }

        menu = new MenuViewBean();
        menu.setName(getSpecificMessage(ScopeWebMessage.HEADER_MENU, "our_company", locale));
        menu.setUrl(urlService.generateUrl(FoUrls.OUR_COMPANY, requestData));
        menuViewBeans.add(menu);

        // Set active menu
        String currentUrl = requestUtil.getLastRequestUrl(request);
        for (Iterator<MenuViewBean> iteratorMenu = menuViewBeans.iterator(); iteratorMenu.hasNext();) {
            MenuViewBean menuCheck = (MenuViewBean) iteratorMenu.next();
            menuCheck.setActive(false);
            if (currentUrl != null && currentUrl.contains(menuCheck.getUrl())) {
                menuCheck.setActive(true);
                for (Iterator<MenuViewBean> iteratorSubMenu = menuCheck.getSubMenus().iterator(); iteratorSubMenu.hasNext();) {
                    MenuViewBean subMenu = (MenuViewBean) iteratorSubMenu.next();
                    subMenu.setActive(false);
                    if (currentUrl != null && currentUrl.contains(subMenu.getUrl())) {
                        subMenu.setActive(true);
                    }
                }
            }
        }

        return menuViewBeans;
    }
    
    // SEARCH
    
    @Override
    public CatalogCategoryViewBean buildCatalogCategoryViewBean(RequestData requestData, CatalogCategoryVirtual catalogCategory) throws Exception {
        final HttpServletRequest request = requestData.getRequest();
        final CatalogCategoryViewBean catalogCategoryViewBean = super.buildCatalogCategoryViewBean(requestData, catalogCategory);
        
        final String sortBy =  request.getParameter("sortBy");
        final String orderBy =  request.getParameter("orderBy");
        
        final List<ProductMarketingViewBean> productMarketingViewBeans = catalogCategoryViewBean.getProductMarketings();
        
        Collections.sort(productMarketingViewBeans, new Comparator<ProductMarketingViewBean>() {
            @Override
            public int compare(ProductMarketingViewBean o1, ProductMarketingViewBean o2) {
                if ("price".equals(sortBy)) {
                    if ("desc".equals(orderBy)) {
                        if (o2.getPriceWithCurrencySign() != null && o1.getPriceWithCurrencySign() != null) {
                            return o2.getPriceWithCurrencySign().compareTo(o1.getPriceWithCurrencySign());
                        } else {
                            if (o1.getPriceWithCurrencySign() == null) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    } else {
                        if (o1.getPriceWithCurrencySign() != null && o2.getPriceWithCurrencySign() != null) {
                            return o1.getPriceWithCurrencySign().compareTo(o2.getPriceWithCurrencySign());
                        } else {
                            if (o1.getPriceWithCurrencySign() == null) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    }
                } else {
                    /**
                     * default sort by name and oderby asc
                     */
                    if ("desc".equals(orderBy)) {
                        return o2.getI18nName().compareTo(o1.getI18nName());
                    } else {
                        return o1.getI18nName().compareTo(o2.getI18nName());
                    }
                }
            }
        });
        catalogCategoryViewBean.setProductMarketings(productMarketingViewBeans);
        return super.buildCatalogCategoryViewBean(requestData, catalogCategory);
    }

    /**
     * 
     */
    public SearchViewBean buildSearchViewBean(final RequestData requestData) throws Exception {
        final Localization localization = requestData.getMarketAreaLocalization();
        final Locale locale = localization.getLocale();

        final SearchViewBean search = new SearchViewBean();
        search.setTextLabel(getSpecificMessage(ScopeWebMessage.SEARCH, "form.label.text", locale));

        return search;
    }

    /**
     * 
     */
    public List<SearchProductItemViewBean> buildSearchProductItemViewBeans(final RequestData requestData, final ProductMarketingResponseBean productMarketingResponseBean) throws Exception {
        final List<SearchProductItemViewBean> searchProductItems = new ArrayList<SearchProductItemViewBean>();
        List<ProductMarketingSolr> productMarketings = productMarketingResponseBean.getProductMarketingSolrList();
        for (Iterator<ProductMarketingSolr> iterator = productMarketings.iterator(); iterator.hasNext();) {
            ProductMarketingSolr productMarketingSolr = (ProductMarketingSolr) iterator.next();
            SearchProductItemViewBean searchProductItemViewBean = buildSearchProductItemViewBean(requestData, productMarketingSolr);
            if(searchProductItemViewBean != null){
            	searchProductItems.add(searchProductItemViewBean);
            }
        }
        return searchProductItems;
    }

    /**
     * 
     */
    public SearchProductItemViewBean buildSearchProductItemViewBean(final RequestData requestData, final ProductMarketingSolr productMarketingSolr) throws Exception {
        final MarketArea marketArea = requestData.getMarketArea();
        final Localization localization = requestData.getMarketAreaLocalization();
        final String localeCode = localization.getCode();

        final String productCode = productMarketingSolr.getCode();
        final ProductMarketing productMarketing = productService.getProductMarketingByCode(productCode);
        final ProductSku productSku = productService.getProductSkuByCode(productMarketing.getDefaultProductSku().getCode());
        final String productSkuName = productSku.getI18nName(localeCode);
        
        final CatalogCategoryVirtual catalogCategory = catalogCategoryService.getDefaultVirtualCatalogCategoryByProductMarketing(marketArea.getId(), productMarketing.getCode());

        final String productName = productMarketing.getCode();
        final String categoryName = catalogCategory.getI18nName(localeCode);

        final SearchProductItemViewBean searchProductItemViewBean = new SearchProductItemViewBean();
        searchProductItemViewBean.setName(categoryName + " " + productName + " " + productSkuName);
        searchProductItemViewBean.setDescription(productMarketing.getDescription());
        searchProductItemViewBean.setCode(productCode);
        searchProductItemViewBean.setCategoryName(categoryName);
        searchProductItemViewBean.setCategoryCode(catalogCategory.getCode());

        searchProductItemViewBean.setProductDetailsUrl(urlService.generateUrl(FoUrls.PRODUCT_DETAILS, requestData, catalogCategory, productMarketing, productSku));

        Map<String, String> getParams = new HashMap<String, String>();
        getParams.put(RequestConstants.REQUEST_PARAMETER_PRODUCT_SKU_CODE, productSku.getCode());

        searchProductItemViewBean.setAddToCartUrl(urlService.generateUrl(FoUrls.CART_ADD_ITEM, requestData, getParams));
        
        final Asset defaultBackgroundImage = productMarketing.getDefaultBackgroundImage();
        if (defaultBackgroundImage != null) {
            final String backgroundImage = requestUtil.getProductMarketingImageWebPath(requestData.getRequest(), defaultBackgroundImage);
            searchProductItemViewBean.setBackgroundImage(backgroundImage);
        } else {
        	searchProductItemViewBean.setBackgroundImage("");
        }
        
        final Asset defaultPaskshotImage = productMarketing.getDefaultPaskshotImage(ImageSize.SMALL.name());
        if (defaultPaskshotImage != null) {
            final String carouselImage = requestUtil.getProductMarketingImageWebPath(requestData.getRequest(), defaultPaskshotImage);
            searchProductItemViewBean.setCarouselImage(carouselImage);
        } else {
        	searchProductItemViewBean.setCarouselImage("");
        }
        
        final Asset defaultIconImage = productMarketing.getDefaultIconImage();
        if (defaultIconImage != null) {
            final String iconImage = requestUtil.getProductMarketingImageWebPath(requestData.getRequest(), defaultIconImage);
            searchProductItemViewBean.setIconImage(iconImage);
        } else {
        	searchProductItemViewBean.setIconImage("");
        }
        
        Set<ProductSku> skus = productMarketing.getProductSkus();
        if (skus != null) {
            for (Iterator<ProductSku> iterator = skus.iterator(); iterator.hasNext();) {
                final ProductSku productSkuTmp = (ProductSku) iterator.next();
                final ProductSku reloadedProductSku = productService.getProductSkuByCode(productSkuTmp.getCode());
                searchProductItemViewBean.getProductSkus().add(buildProductSkuViewBean(requestData, catalogCategory, productMarketing, reloadedProductSku));
            }
        }

        return searchProductItemViewBean;
    }

    /**
     * 
     */
    public List<SearchFacetViewBean> buildSearchFacetViewBeans(final RequestData requestData, final ProductMarketingResponseBean productMarketingResponseBean) throws Exception {
        final List<SearchFacetViewBean> searchFacetViewBeans = new ArrayList<SearchFacetViewBean>();
        List<FacetField> productFacetFields = productMarketingResponseBean.getProductMarketingSolrFacetFieldList();
        for (Iterator<FacetField> iterator = productFacetFields.iterator(); iterator.hasNext();) {
            FacetField facetField = (FacetField) iterator.next();
            searchFacetViewBeans.add(buildSearchFacetViewBean(requestData, facetField));
        }
        return searchFacetViewBeans;
    }

    /**
     * 
     */
    public SearchFacetViewBean buildSearchFacetViewBean(final RequestData requestData, final FacetField facetField) throws Exception {
        final SearchFacetViewBean searchFacetViewBean = new SearchFacetViewBean();
        final MarketArea marketArea = requestData.getMarketArea();
        final Localization localization = requestData.getMarketAreaLocalization();
        final String localeCode = localization.getCode();
        
        if(Constants.PRODUCT_MARKETING_DEFAULT_FACET_FIELD.equalsIgnoreCase(facetField.getName())){
        	searchFacetViewBean.setName(facetField.getName());
            List<ValueBean> values = new ArrayList<ValueBean>();
            for (Iterator<Count> iterator = facetField.getValues().iterator(); iterator.hasNext();) {
                Count count = (Count) iterator.next();
                final CatalogCategoryMaster catalogCategoryMaster = catalogCategoryService.getMasterCatalogCategoryByCode(marketArea.getId(), count.getName());
                ValueBean valueBean = new ValueBean(catalogCategoryMaster.getCode(), catalogCategoryMaster.getI18nName(localeCode) + "(" + count.getCount() + ")");                
                values.add(valueBean);
            }
            searchFacetViewBean.setValues(values);
        }
        
        
        return searchFacetViewBean;
    }
    
    public List<CatalogCategoryViewBean> buildListRootCatalogCategories(RequestData requestData, MarketArea marketArea) throws Exception {
    	final List<CatalogCategoryVirtual> categoryVirtuals = catalogCategoryService.findRootVirtualCatalogCategories(marketArea.getId());
    	
    	final List<CatalogCategoryViewBean> catalogCategoryViewBeans = new ArrayList<CatalogCategoryViewBean>();
    	
    	for (CatalogCategoryVirtual catalogCategoryVirtual : categoryVirtuals) {
    		CatalogCategoryViewBean catalogCategoryViewBean = buildCatalogCategoryViewBean(requestData, catalogCategoryVirtual);
    		catalogCategoryViewBeans.add(catalogCategoryViewBean);
		}
    	
    	return catalogCategoryViewBeans;
    }
    
    public List<ProductBrandViewBean> buildListProductBrands(final RequestData requestData, final CatalogCategoryVirtual catalogCategoryVirtual) throws Exception {
    	final List<ProductBrandViewBean> productBrandViewBeans = new ArrayList<ProductBrandViewBean>();
    	
    	List<ProductBrand> productBrands = productService.findProductBrandsByCatalogCategoryCode(catalogCategoryVirtual.getCode());
    	for (ProductBrand productBrand : productBrands) {
    		ProductBrandViewBean productBrandViewBean = buildProductBrandViewBean(requestData, productBrand);
    		productBrandViewBeans.add(productBrandViewBean);
		}
    	return productBrandViewBeans;
    }
    
    @Override
    public List<RecentProductViewBean> buildRecentProductViewBean(final RequestData requestData, final List<String> listCode) throws Exception {
    	final List<RecentProductViewBean> recentProductViewBeans = new ArrayList<RecentProductViewBean>(); 
    	final Localization localization = requestData.getMarketAreaLocalization();
        final String localeCode = localization.getCode();
    	final MarketArea marketArea = requestData.getMarketArea();
    	for (String value : listCode) {
    		RecentProductViewBean recentProductViewBean = new RecentProductViewBean();
    		ProductMarketing productMarketing = productService.getProductMarketingById(value);
    		CatalogCategoryVirtual catalogCategory = catalogCategoryService.getDefaultVirtualCatalogCategoryByProductMarketing(marketArea.getId(), productMarketing.getCode());
        	recentProductViewBean.setId(productMarketing.getId());
    		recentProductViewBean.setCode(value);
    		recentProductViewBean.setDetailsUrl(urlService.generateUrl(FoUrls.PRODUCT_DETAILS, requestData, catalogCategory, productMarketing, productMarketing.getDefaultProductSku()));	
        	recentProductViewBean.setI18nName(productMarketing.getI18nName(localeCode));
        	recentProductViewBeans.add(recentProductViewBean);
    	}
    	return recentProductViewBeans;
    }
    
    /**
     * 
     */
    public CatalogBreadcrumbViewBean buildCatalogBreadcrumbViewBean(final RequestData requestData, final CatalogCategoryVirtual catalogCategory) throws Exception {
    	 final Localization localization =  requestData.getMarketAreaLocalization();
    	 final String localizationCode = localization.getCode();
    	 final MarketArea currentMarketArea = requestData.getMarketArea();
    	 final CatalogBreadcrumbViewBean catalogBreadCumViewBean = new CatalogBreadcrumbViewBean();
    	 catalogBreadCumViewBean.setRoot(catalogCategory.isRoot());
    	 catalogBreadCumViewBean.setName(catalogCategory.getI18nName(localizationCode));
		
		 if (catalogCategory.isRoot()) {
			catalogBreadCumViewBean.setDetailsUrl(urlService.generateUrl(FoUrls.CATEGORY_AS_AXE, requestData, catalogCategory));
		 } else {
			catalogBreadCumViewBean.setDetailsUrl(urlService.generateUrl(FoUrls.CATEGORY_AS_LINE, requestData, catalogCategory));
		 }
		 final CatalogCategoryVirtual parentCatalogCategoryVirtual = catalogCategory.getDefaultParentCatalogCategory();
		 if(!catalogCategory.isRoot() && parentCatalogCategoryVirtual != null){
			 final CatalogCategoryVirtual pareCatalogCategoryVirtualReload = catalogCategoryService.getVirtualCatalogCategoryByCode(currentMarketArea.getId(), parentCatalogCategoryVirtual.getCode());
			catalogBreadCumViewBean.setDefaultParentCategory(buildCatalogBreadcrumbViewBean(requestData,pareCatalogCategoryVirtualReload));
		 }

    	return catalogBreadCumViewBean;
    }
    
    public StoreLocatorFilterBean buildStoreLocatorFilterBean(final StoreLocatorViewBean storeLocatorViewBean, final Locale locale) throws Exception {
        final List<StoreViewBean> stores = storeLocatorViewBean.getStores();
        final StoreLocatorFilterBean filter = new StoreLocatorFilterBean();

        final Map<String, StoreLocatorCountryFilterBean> countryFilterMap = new HashMap<String, StoreLocatorCountryFilterBean>();
        final Map<String, StoreLocatorCityFilterBean> cityFilterMap = new HashMap<String, StoreLocatorCityFilterBean>();

        for (StoreViewBean store : stores) {
            String country = store.getCountry();
            String city = store.getCity();
            StoreLocatorCountryFilterBean countryFilter;
            StoreLocatorCityFilterBean cityFilter;

            if (countryFilterMap.containsKey(country)) {
                countryFilter = countryFilterMap.get(country);
            } else {
                countryFilter = new StoreLocatorCountryFilterBean();
                countryFilter.setCode(country);
                String countryLabel = referentialDataService.getCountryByLocale(country, locale);
                countryFilter.setName(countryLabel);
                filter.addCountry(countryFilter);
                countryFilterMap.put(country, countryFilter);
            }

            if (cityFilterMap.containsKey(city)) {
                cityFilter = cityFilterMap.get(city);
            } else {
                cityFilter = new StoreLocatorCityFilterBean();
                cityFilter.setCode(handleCityCode(country));
                cityFilter.setName(city);
                countryFilter.addCity(cityFilter);
                cityFilterMap.put(city, cityFilter);
            }

            cityFilter.addStore(store);
        }
        return filter;
    }
    
    protected String handleCityCode(String cityName) throws Exception {
        if (StringUtils.isNotEmpty(cityName)) {
            cityName = cityName.replaceAll(" ", "-");
            cityName = cityName.replaceAll("_", "-");
            cityName = cityName.replaceAll("[àáâãäå]", "a");
            cityName = cityName.replaceAll("[ç]", "c");
            cityName = cityName.replaceAll("[èéêë]", "e");
            cityName = cityName.replaceAll("[ìíîï]", "i");
            cityName = cityName.replaceAll("[ðòóôõö]", "o");
            cityName = cityName.replaceAll("[ùúûü]", "u");
            cityName = cityName.replaceAll("[ýÿ]", "y");
            cityName = cityName.toLowerCase().trim();
        }
        return cityName;
    }

}