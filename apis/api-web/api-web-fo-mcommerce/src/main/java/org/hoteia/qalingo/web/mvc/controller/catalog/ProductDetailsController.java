/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.web.mvc.controller.catalog;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hoteia.qalingo.core.Constants;
import org.hoteia.qalingo.core.ModelConstants;
import org.hoteia.qalingo.core.RequestConstants;
import org.hoteia.qalingo.core.domain.CatalogCategoryVirtual;
import org.hoteia.qalingo.core.domain.MarketArea;
import org.hoteia.qalingo.core.domain.ProductMarketing;
import org.hoteia.qalingo.core.domain.enumtype.FoUrls;
import org.hoteia.qalingo.core.pojo.RequestData;
import org.hoteia.qalingo.core.service.CatalogCategoryService;
import org.hoteia.qalingo.core.service.ProductService;
import org.hoteia.qalingo.core.web.mvc.viewbean.CatalogBreadcrumbViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.CatalogCategoryViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.ProductMarketingViewBean;
import org.hoteia.qalingo.core.web.servlet.ModelAndViewThemeDevice;
import org.hoteia.qalingo.web.mvc.controller.AbstractMCommerceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 */
@Controller("productDetailsController")
public class ProductDetailsController extends AbstractMCommerceController {

	@Autowired
	protected CatalogCategoryService catalogCategoryService;
	
	@Autowired
	protected ProductService productService;
	
	@RequestMapping(FoUrls.PRODUCT_DETAILS_URL)
	public ModelAndView productDetails(final HttpServletRequest request, final HttpServletResponse response ,final Model model, @PathVariable(RequestConstants.URL_PATTERN_CATEGORY_CODE) final String categoryCode,
			 						   @PathVariable(RequestConstants.URL_PATTERN_PRODUCT_MARKETING_CODE) final String productMarketingCode,
			 						   @PathVariable(RequestConstants.URL_PATTERN_PRODUCT_SKU_CODE) final String productSkuCode) throws Exception {
		ModelAndViewThemeDevice modelAndView = new ModelAndViewThemeDevice(getCurrentVelocityPath(request), FoUrls.PRODUCT_DETAILS.getVelocityPage());
        final RequestData requestData = requestUtil.getRequestData(request);
        final MarketArea currentMarketArea = requestData.getMarketArea();
        final Locale locale = requestData.getLocale();

		CatalogCategoryVirtual catalogCategory = catalogCategoryService.getVirtualCatalogCategoryByCode(currentMarketArea.getId(), categoryCode);
		ProductMarketing productMarketing = productService.getProductMarketingByCode(productMarketingCode);
		
		String seoPageMetaKeywords = coreMessageSource.getMessage("page.meta.keywords", locale);
        model.addAttribute("seoPageMetaKeywords", seoPageMetaKeywords);

		String seoPageMetaDescription = coreMessageSource.getMessage("page.meta.description", locale);
        model.addAttribute("seoPageMetaDescription", seoPageMetaDescription);

		String pageTitleKey = "header.title." + "";
		String seoPageTitle = coreMessageSource.getMessage("page.title.prefix", locale) + " - " + coreMessageSource.getMessage(pageTitleKey, locale);
        model.addAttribute("seoPageTitle", seoPageTitle);
        
		final CatalogCategoryViewBean catalogCategoryViewBean = frontofficeViewBeanFactory.buildCatalogCategoryViewBean(requestUtil.getRequestData(request), catalogCategory);
		model.addAttribute(ModelConstants.CATALOG_CATEGORY_VIEW_BEAN, catalogCategoryViewBean);

        final ProductMarketingViewBean productMarketingViewBean = frontofficeViewBeanFactory.buildProductMarketingViewBean(requestUtil.getRequestData(request), catalogCategory, productMarketing);
        model.addAttribute(ModelConstants.PRODUCT_MARKETING_VIEW_BEAN, productMarketingViewBean);
        
        final CatalogBreadcrumbViewBean catalogBreadcrumbViewBean = frontofficeViewBeanFactory.buildCatalogBreadcrumbViewBean(requestUtil.getRequestData(request) , catalogCategory);
		model.addAttribute(ModelConstants.CATALOG_BREADCRUMB_VIEW_BEAN, catalogBreadcrumbViewBean);

        //for now, get the featured products in same category
        //TODO: define related products
        List<ProductMarketingViewBean> relatedProducts = catalogCategoryViewBean.getFeaturedProductMarketings();
        model.addAttribute(ModelConstants.RELATED_PPRODUCT_MARKETING_VIEW_BEAN, relatedProducts);

        // TODO : Denis : move this part, Cookie, in RequestUtilImpl.java
        Cookie info=null;
        Cookie[] cookies = request.getCookies();
        Boolean found = false;
        if(cookies !=  null){
	        for(int i=0;i<cookies.length;i++)
	        {
	            info=cookies[i];
	            if(Constants.COOKIE_RECENT_PRODUCT_COOKIE_NAME.equals(info.getName()))
	            {
	                found = true;
	                break;
	            }
	        }
        }   
        if(found){
        	Boolean flag = false;
        	String[] splits = info.getValue().split(" ");
        	for(String value:splits){
        		if(value.equals(Long.toString(productMarketing.getId()))){
        			flag = true;
        		} 
        	}
        	if(!flag){
        		String values = info.getValue();
        		values += " "+ Long.toString(productMarketing.getId());
        		info.setValue(values);
        		info.setPath("/");
    			response.addCookie(info);
        	} 
        } else {
			info = new Cookie(Constants.COOKIE_RECENT_PRODUCT_COOKIE_NAME, Long.toString(productMarketing.getId()));
			info.setMaxAge(Constants.COOKIES_LENGTH);
			info.setPath("/");
			info.setDomain("fo-mcommerce.dev.qalingo.com");
			response.addCookie(info);
        }
        return modelAndView;
	}

}