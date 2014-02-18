package org.hoteia.qalingo.core.web.mvc.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hoteia.qalingo.core.Constants;
import org.hoteia.qalingo.core.ModelConstants;
import org.hoteia.qalingo.core.domain.enumtype.BoUrls;
import org.hoteia.qalingo.core.domain.enumtype.EngineSettingWebAppContext;
import org.hoteia.qalingo.core.i18n.BoMessageKey;
import org.hoteia.qalingo.core.i18n.enumtype.I18nKeyValueUniverse;
import org.hoteia.qalingo.core.i18n.enumtype.ScopeCommonMessage;
import org.hoteia.qalingo.core.i18n.enumtype.ScopeWebMessage;
import org.hoteia.qalingo.core.pojo.RequestData;
import org.hoteia.qalingo.core.service.BackofficeUrlService;
import org.hoteia.qalingo.core.service.EngineSettingService;
import org.hoteia.qalingo.core.service.LocalizationService;
import org.hoteia.qalingo.core.service.UserService;
import org.hoteia.qalingo.core.service.WebBackofficeService;
import org.hoteia.qalingo.core.web.mvc.factory.BackofficeFormFactory;
import org.hoteia.qalingo.core.web.mvc.factory.BackofficeViewBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * <p>
 * <a href="AbstractBackofficeQalingoController.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author Denis Gosset <a href="http://www.hoteia.com"><i>Hoteia.com</i></a>
 * 
 */
public abstract class AbstractBackofficeQalingoController extends AbstractQalingoController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    protected BackofficeUrlService backofficeUrlService;

	@Autowired
	protected UserService userService;
	
	@Autowired
	protected LocalizationService localizationService;
	
	@Autowired
	protected EngineSettingService engineSettingService;
	
	@Autowired
    protected BackofficeViewBeanFactory backofficeViewBeanFactory;
	
    @Autowired
    protected BackofficeFormFactory backofficeFormFactory;
    
    @Autowired
    protected WebBackofficeService webBackofficeService;

	/**
	 * 
	 */
	@ModelAttribute
	protected void initApp(final HttpServletRequest request, final Model model) throws Exception {
        final RequestData requestData = requestUtil.getRequestData(request);
        final Locale locale = requestData.getLocale();
        
		// APP NAME
		model.addAttribute(Constants.APP_NAME, getAppName(request));
		Object[] params = {StringUtils.capitalize(requestUtil.getApplicationName())};
		model.addAttribute(Constants.APP_NAME_HTML, getCommonMessage(ScopeCommonMessage.APP, "name_html", params, locale));
	}
	
	/**
	 * 
	 */
	@ModelAttribute
	protected void initConfig(final HttpServletRequest request, final Model model) throws Exception {
        final RequestData requestData = requestUtil.getRequestData(request);
        final Locale locale = requestData.getLocale();
        
		model.addAttribute(ModelConstants.LOCALE_LANGUAGE_CODE, locale.getLanguage());
		model.addAttribute(ModelConstants.CONTEXT_PATH, request.getContextPath());
		model.addAttribute(ModelConstants.THEME, requestUtil.getCurrentTheme(request));
		Object[] params = {StringUtils.capitalize(requestUtil.getEnvironmentName())};
		model.addAttribute(ModelConstants.ENV_NAME, getSpecificMessage(ScopeWebMessage.COMMON, "header.env.name", params, locale));
	}
	
	/**
	 * 
	 */
	@ModelAttribute
	protected void initSeo(final HttpServletRequest request, final Model model) throws Exception {
        final RequestData requestData = requestUtil.getRequestData(request);
        final Locale locale = requestData.getLocale();

		String seoPageMetaAuthor = getCommonMessage(ScopeCommonMessage.SEO, BoMessageKey.SEO_META_AUTHOR, locale);
        model.addAttribute(ModelConstants.SEO_PAGE_META_AUTHOR, seoPageMetaAuthor);

		String seoPageMetaKeywords = getCommonMessage(ScopeCommonMessage.SEO, BoMessageKey.SEO_META_KEYWORDS, locale);
        model.addAttribute(ModelConstants.SEO_PAGE_META_KEYWORDS, seoPageMetaKeywords);

		String seoPageMetaDescription = getCommonMessage(ScopeCommonMessage.SEO, BoMessageKey.SEO_META_DESCRIPTION, locale);
        model.addAttribute(ModelConstants.SEO_PAGE_META_DESCIPRTION, seoPageMetaDescription);

        // DEFAULT EMPTY VALUE
        model.addAttribute(ModelConstants.SEO_PAGE_META_TITLE, getAppName(request));
	}
	
	/**
	 * 
	 */
	@ModelAttribute(ModelConstants.URL_SUBMIT_QUICK_SEARCH)
	protected String initQuickSearch(final HttpServletRequest request, final Model model) throws Exception {
		return backofficeUrlService.generateUrl(BoUrls.GLOBAL_SEARCH, requestUtil.getRequestData(request));
	}
	
	/**
	 * 
	 */
	protected void overrideMainContentTitle(final HttpServletRequest request, final ModelAndView modelAndView, final String title) throws Exception {
//		final Locale locale  = requestUtil.getCurrentLocale(request);
//		String mainContentTitleKey = BoMessageKey.MAIN_CONTENT_TITLE_PREFIX + pageKey;
//		try {
//			String mainContentTitle = getSpecificMessage(ScopeWebMessage.BREADCRUMB_AND_HEADER, mainContentTitleKey, locale);
//	        modelAndView.addObject(ModelConstants.MAIN_CONTENT_TITLE, mainContentTitle);
//		} catch (Exception e) {
//			// DOESN'T NEED WARNING OR ERROR LOG
//			logger.debug("mainContentTitle with key header.title doesn't exist for:" + pageKey);
//		}
		if(StringUtils.isNotEmpty(title)){
			 modelAndView.addObject(ModelConstants.MAIN_CONTENT_TITLE, title);
		}
	}
	
	/**
	 * 
	 */
	@ModelAttribute
	public void initWording(final HttpServletRequest request, final Model model) throws Exception {
        final RequestData requestData = requestUtil.getRequestData(request);
        final Locale locale = requestData.getLocale();
		String contextName = requestUtil.getContextName();
		try {
			EngineSettingWebAppContext contextValue = EngineSettingWebAppContext.valueOf(contextName);
			model.addAttribute(ModelConstants.WORDING, coreMessageSource.loadWording(contextValue, locale));
	        
        } catch (Exception e) {
        	logger.error("Context name, " + contextName + " can't be resolve by EngineSettingWebAppContext class.", e);
        }
	}
	
	/**
	 * 
	 */
	protected void overrideSeoTitle(final HttpServletRequest request, final ModelAndView modelAndView, final String title) throws Exception {
		final String fullTitle = getAppName(request) + " - " + title;
		if(StringUtils.isNotEmpty(fullTitle)){
	        modelAndView.addObject(ModelConstants.SEO_PAGE_META_TITLE, fullTitle);
		}
	}
	
	/**
	 * 
	 */
	protected String getMessageTitleKey(final String pageKey) throws Exception {
		return "page_title_" + pageKey;
	}
	
	/**
	 * 
	 */
	@ModelAttribute
	protected void initBreadcrumAndHeaderContent(final HttpServletRequest request, final Model model) throws Exception {
        // DEFAULT EMPTY VALUE
        model.addAttribute(ModelConstants.MAIN_CONTENT_TITLE, "");
	}
	
//	/**
//	 * 
//	 */
//	@ModelAttribute(ModelConstants.USER_VIEW_BEAN)
//	protected User initUser(final HttpServletRequest request, final Model model) throws Exception {
//		final User user = requestUtil.getCurrentUser(request);
//		return user;
//	}
//	
//	/**
//	 * @throws Exception  
//	 * 
//	 */
//	protected String getUserId(final HttpServletRequest request) throws Exception {
//		User user = requestUtil.getCurrentUser(request);
//		if(user != null){
//			Long userId = user.getId();
//			if(userId != null){
//				return userId.toString();
//			} else {
//				return null;
//			}
//		} else {
//			return null;
//		}
//	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	protected String getAppName(HttpServletRequest request) throws Exception {
        final RequestData requestData = requestUtil.getRequestData(request);
        final Locale locale = requestData.getLocale();
		Object[] params = {StringUtils.capitalize(requestUtil.getApplicationName())};
		String appName = getCommonMessage(ScopeCommonMessage.APP, "name_text", params, locale);
		return appName;
	}
	
    /**
     * @throws Exception
     * 
     */
    protected String getCurrentVelocityPath(HttpServletRequest request) throws Exception {
        final RequestData requestData = requestUtil.getRequestData(request);
        return requestUtil.getCurrentVelocityWebPrefix(requestData);
    }
    
	/**
	 * 
	 */
	protected String getSpecificMessage(ScopeWebMessage scope, String key, Locale locale){
		return coreMessageSource.getSpecificMessage(I18nKeyValueUniverse.BO, scope, key, locale);
	}
	
	/**
	 * 
	 */
	protected String getSpecificMessage(ScopeWebMessage scope, String key, Object[] params, Locale locale){
		return coreMessageSource.getSpecificMessage(I18nKeyValueUniverse.BO, scope, key, params, locale);
	}
}