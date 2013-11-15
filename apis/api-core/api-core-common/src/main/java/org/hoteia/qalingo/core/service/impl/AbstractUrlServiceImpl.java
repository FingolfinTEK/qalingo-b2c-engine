package org.hoteia.qalingo.core.service.impl;

import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import org.hoteia.qalingo.core.Constants;
import org.hoteia.qalingo.core.domain.Localization;
import org.hoteia.qalingo.core.domain.MarketArea;
import org.hoteia.qalingo.core.i18n.enumtype.I18nKeyValueUniverse;
import org.hoteia.qalingo.core.i18n.enumtype.ScopeWebMessage;
import org.hoteia.qalingo.core.i18n.message.CoreMessageSource;
import org.hoteia.qalingo.core.pojo.RequestData;
import org.hoteia.qalingo.core.service.EngineSettingService;

public abstract class AbstractUrlServiceImpl {

	@Autowired
	public CoreMessageSource coreMessageSource;

	@Autowired
	public EngineSettingService engineSettingService;
	
    protected String handleUrlParameters(String url, Map<String, String> urlParams, Map<String, String> getParams) {
    	if(StringUtils.isNotEmpty(url)){
            if (urlParams != null) {
                for (Entry<String, String> entry : urlParams.entrySet()) {
                    String key = String.format("\\{%s(:[^\\}]+)?\\}", entry.getKey());
                    if (StringUtils.equals(entry.getKey(), "slug")){
                    	key = "\\*\\*";
                    }
                    if (entry.getValue() != null){
                    	url = url.replaceAll(key, entry.getValue());
                    }
                }
            }

            String queryString = "";
            if (getParams != null) {
                for (Entry<String, String> entry : getParams.entrySet()) {
                    queryString += "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
            return url + queryString.replaceFirst("&", "?");
    	}
        return url;
    }
    
    public String buildAbsoluteUrl(final RequestData requestData, final String relativeUrl) throws Exception {
        String absoluteUrl = buildDomainePathUrl(requestData);
        if(!relativeUrl.startsWith("/")){
            absoluteUrl = absoluteUrl + "/" + relativeUrl;
        } else {
            absoluteUrl = absoluteUrl + relativeUrl;
        }
        if(!absoluteUrl.startsWith("http://")){
            absoluteUrl = "http://" + absoluteUrl;
        }
        return absoluteUrl;
    }
    
    public String buildDomainePathUrl(final RequestData requestData) throws Exception {
        final HttpServletRequest request = requestData.getRequest();
        final MarketArea marketArea = requestData.getMarketArea();
        final String contextNameValue = requestData.getContextNameValue();
        
        String domainePathUrl = "";
        if(marketArea != null){
            String domainName = marketArea.getDomainName(contextNameValue);
            if(StringUtils.isNotEmpty(domainName)){
                domainePathUrl = domainName;
            }
        }
        if(StringUtils.isEmpty(domainePathUrl)){
            String requestUrl = request.getRequestURL().toString();
            requestUrl = requestUrl.replace("http://", "");
            String[] urlBlock = requestUrl.split("/");
            domainePathUrl = urlBlock[0];
        }
        if(!domainePathUrl.startsWith("http")){
            String scheme = request.getScheme();
            domainePathUrl = scheme + "://" + domainePathUrl;
        }
        return domainePathUrl;
    }
    
	protected String encodeString(String url) throws Exception {
		if(StringUtils.isNotEmpty(url)){
			return URLEncoder.encode(url, Constants.UTF8);
		}
		return url;
	}

	protected String handleParamValue(String string) throws Exception {
		if(StringUtils.isNotEmpty(string)) {
			string = string.toLowerCase();
		}
		return string;
	}

	protected String handleString(String string) throws Exception {
		if(StringUtils.isNotEmpty(string)) {
			string = string.replaceAll(" ", "-");
			string = string.replaceAll("_", "-");
			String escapeAccent = engineSettingService.withEscapeAccent().getDefaultValue();
			if(BooleanUtils.toBoolean(escapeAccent)){
				string = string.replaceAll("[àáâãäå]", "a");
				string = string.replaceAll("[ç]", "c");
				string = string.replaceAll("[èéêë]", "e");
				string = string.replaceAll("[ìíîï]", "i");
				string = string.replaceAll("[ðòóôõö]", "o");
				string = string.replaceAll("[ùúûü]", "u");
				string = string.replaceAll("[ýÿ]", "y");
			}
			string = encodeString(string).toLowerCase().trim();
		 }
		return string;
	}
	
	protected String buildDefaultPrefix(final RequestData requestData){
		return buildContextPath(requestData) + Constants.SPRING_URL_PATH;
	}
	
	protected String buildContextPath(final RequestData requestData){
		return requestData.getContextPath();
	}
	
	protected String getMessage(final Localization localization, final String key) throws Exception {
		final Locale locale = localization.getLocale();
		return handleString(coreMessageSource.getSpecificMessage(I18nKeyValueUniverse.FO, ScopeWebMessage.SEO, key, locale));
	}

	protected MessageSource getMessageSource() throws Exception {
		return coreMessageSource;
	}
	
}