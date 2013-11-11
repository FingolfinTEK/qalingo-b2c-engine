/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package fr.hoteia.qalingo.core.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.hoteia.qalingo.core.RequestConstants;
import fr.hoteia.qalingo.core.domain.AbstractPaymentGateway;
import fr.hoteia.qalingo.core.domain.AbstractRuleReferential;
import fr.hoteia.qalingo.core.domain.Asset;
import fr.hoteia.qalingo.core.domain.CatalogCategoryMaster;
import fr.hoteia.qalingo.core.domain.CatalogCategoryVirtual;
import fr.hoteia.qalingo.core.domain.Customer;
import fr.hoteia.qalingo.core.domain.EngineSetting;
import fr.hoteia.qalingo.core.domain.EngineSettingValue;
import fr.hoteia.qalingo.core.domain.Localization;
import fr.hoteia.qalingo.core.domain.Market;
import fr.hoteia.qalingo.core.domain.MarketArea;
import fr.hoteia.qalingo.core.domain.MarketPlace;
import fr.hoteia.qalingo.core.domain.Order;
import fr.hoteia.qalingo.core.domain.ProductMarketing;
import fr.hoteia.qalingo.core.domain.ProductSku;
import fr.hoteia.qalingo.core.domain.Retailer;
import fr.hoteia.qalingo.core.domain.Shipping;
import fr.hoteia.qalingo.core.domain.User;
import fr.hoteia.qalingo.core.domain.enumtype.BoUrls;
import fr.hoteia.qalingo.core.pojo.RequestData;
import fr.hoteia.qalingo.core.web.mvc.service.impl.AbstractUrlServiceImpl;
import fr.hoteia.qalingo.core.web.service.BackofficeUrlService;

@Service("backofficeUrlService")
@Transactional
public class BackofficeUrlServiceImpl extends AbstractUrlServiceImpl implements BackofficeUrlService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public String buildChangeLanguageUrl(final RequestData requestData) throws Exception {
		final MarketPlace marketPlace = requestData.getMarketPlace();
		final Market market = requestData.getMarket();
		final MarketArea marketArea = requestData.getMarketArea();
		final Localization localization = requestData.getLocalization();
		final Retailer retailer = requestData.getRetailer();
		
		String url = buildDefaultPrefix(requestData) + BoUrls.CHANGE_LANGUAGE_URL + "?";
		url = url + RequestConstants.REQUEST_PARAMETER_MARKET_PLACE_CODE + "=" + handleString(marketPlace.getCode());
		url = url + "&" + RequestConstants.REQUEST_PARAMETER_MARKET_CODE + "=" + handleString(market.getCode());
		url = url + "&" + RequestConstants.REQUEST_PARAMETER_MARKET_AREA_CODE + "=" + handleString(marketArea.getCode());
		url = url + "&" + RequestConstants.REQUEST_PARAMETER_MARKET_LANGUAGE + "=" + handleString(localization.getCode());
		url = url + "&" + RequestConstants.REQUEST_PARAMETER_RETAILER_CODE + "=" + handleString(retailer.getCode());
		return url;
	}
	
	public String buildChangeContextUrl(final RequestData requestData) throws Exception {
		final MarketPlace marketPlace = requestData.getMarketPlace();
		final Market market = requestData.getMarket();
		final MarketArea marketArea = requestData.getMarketArea();
		final Localization localization = requestData.getLocalization();
		final Retailer retailer = requestData.getRetailer();
		
		String url = buildDefaultPrefix(requestData) + BoUrls.CHANGE_CONTEXT_URL + "?";
		url = url + RequestConstants.REQUEST_PARAMETER_MARKET_PLACE_CODE + "=" + handleString(marketPlace.getCode());
		url = url + "&" + RequestConstants.REQUEST_PARAMETER_MARKET_CODE + "=" + handleString(market.getCode());
		url = url + "&" + RequestConstants.REQUEST_PARAMETER_MARKET_AREA_CODE + "=" + handleString(marketArea.getCode());
		url = url + "&" + RequestConstants.REQUEST_PARAMETER_MARKET_LANGUAGE + "=" + handleString(localization.getCode());
		url = url + "&" + RequestConstants.REQUEST_PARAMETER_RETAILER_CODE + "=" + handleString(retailer.getCode());
		return url;
	}
	
    public String generateUrl(final BoUrls url, final RequestData requestData) {
    	return generateUrl(url, requestData, null);
    }
    
    @SuppressWarnings("unchecked")
    public String generateUrl(final BoUrls url, final RequestData requestData, Object... params) {
    	String urlStr = null;
    	Map<String, String> getParams = new HashMap<String, String>();
    	Map<String, String> urlParams = new HashMap<String, String>();
    	try {
        	if(params != null){
                for (Object param : params) {
                    if (param == null) continue;
                    if (param instanceof CatalogCategoryMaster) {
                        CatalogCategoryMaster catalogCategoryMaster = (CatalogCategoryMaster) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_PRODUCT_CATEGORY_CODE, handleParamValue(catalogCategoryMaster.getCode()));
                        break;
                    } else if (param instanceof CatalogCategoryVirtual) {
                        CatalogCategoryVirtual catalogCategoryVirtual = (CatalogCategoryVirtual) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_PRODUCT_CATEGORY_CODE, handleParamValue(catalogCategoryVirtual.getId().toString()));
                        break;
                    } else if (param instanceof ProductMarketing) {
                        ProductMarketing productMarketing = (ProductMarketing) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_PRODUCT_MARKETING_CODE, handleParamValue(productMarketing.getId().toString()));
                        break;
                    } else if (param instanceof ProductSku) {
                        ProductSku productSku = (ProductSku) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_PRODUCT_SKU_CODE, handleParamValue(productSku.getId().toString()));
                        break;
                    } else if (param instanceof Asset) {
                        Asset asset = (Asset) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_ASSET_CODE, handleParamValue(asset.getId().toString()));
                        break;
                    } else if (param instanceof AbstractRuleReferential) {
                        AbstractRuleReferential rule = (AbstractRuleReferential) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_RULE_CODE, handleParamValue(rule.getId().toString()));
                        break;
                    } else if (param instanceof Shipping) {
                        Shipping shipping = (Shipping) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_SHIPPING_CODE, handleParamValue(shipping.getId().toString()));
                        break;
                    } else if (param instanceof Order) {
                        Order order = (Order) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_ORDER_CODE, handleParamValue(order.getId().toString()));
                        break;
                    } else if (param instanceof Customer) {
                        Customer customer = (Customer) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_CUSTOMER_CODE, handleParamValue(customer.getId().toString()));
                        break;
                    } else if (param instanceof EngineSetting) {
                        EngineSetting engineSetting = (EngineSetting) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_ENGINE_SETTING_ID, handleParamValue(engineSetting.getId().toString()));
                        break;
                    } else if (param instanceof EngineSettingValue) {
                        EngineSettingValue engineSettingValue = (EngineSettingValue) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_ENGINE_SETTING_VALUE_ID, handleParamValue(engineSettingValue.getId().toString()));
                        break;
                    } else if (param instanceof AbstractPaymentGateway) {
                        AbstractPaymentGateway paymentGateway = (AbstractPaymentGateway) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_PAYMENT_GATEWAY_ID, handleParamValue(paymentGateway.getId().toString()));
                        break;
                    } else if (param instanceof User) {
                        User user = (User) param;
                        getParams.put(RequestConstants.REQUEST_PARAMETER_USER_ID, handleParamValue(user.getId().toString()));
                        break;
                    } else if (param instanceof Map) {
                        getParams = (Map<String, String>) param;
                        break;
                    } else {
                        logger.warn("Unknowned url parameter : [{}]", param);
                    }
                }    		
        	}
        	
        	if(StringUtils.isEmpty(urlStr)){
        		urlStr = buildDefaultPrefix(requestData);
        	}
        	
        	urlStr = urlStr + url.getUrl();
	        
        } catch (Exception e) {
        	logger.error("Can't build Url!", e);
        }
    	return handleUrlParameters(urlStr, urlParams, getParams);
    }
    
	public String buildSpringSecurityCheckUrl(final RequestData requestData) throws Exception {
		return buildContextPath(requestData) + BoUrls.SPRING_SECURITY_URL;
	}
	
}