/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *		  http://www.apache.org/licenses/LICENSE-2.0
 *
 *				     Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.web.util.impl;

import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.hoteia.qalingo.core.Constants;
import org.hoteia.qalingo.core.RequestConstants;
import org.hoteia.qalingo.core.domain.Asset;
import org.hoteia.qalingo.core.domain.Cart;
import org.hoteia.qalingo.core.domain.CartItem;
import org.hoteia.qalingo.core.domain.Company;
import org.hoteia.qalingo.core.domain.Customer;
import org.hoteia.qalingo.core.domain.EngineBoSession;
import org.hoteia.qalingo.core.domain.EngineEcoSession;
import org.hoteia.qalingo.core.domain.EngineSetting;
import org.hoteia.qalingo.core.domain.EngineSettingValue;
import org.hoteia.qalingo.core.domain.Localization;
import org.hoteia.qalingo.core.domain.Market;
import org.hoteia.qalingo.core.domain.MarketArea;
import org.hoteia.qalingo.core.domain.MarketPlace;
import org.hoteia.qalingo.core.domain.Order;
import org.hoteia.qalingo.core.domain.ProductSku;
import org.hoteia.qalingo.core.domain.Retailer;
import org.hoteia.qalingo.core.domain.User;
import org.hoteia.qalingo.core.domain.enumtype.EngineSettingWebAppContext;
import org.hoteia.qalingo.core.domain.enumtype.EnvironmentType;
import org.hoteia.qalingo.core.pojo.RequestData;
import org.hoteia.qalingo.core.service.CustomerService;
import org.hoteia.qalingo.core.service.EngineSettingService;
import org.hoteia.qalingo.core.service.LocalizationService;
import org.hoteia.qalingo.core.service.MarketPlaceService;
import org.hoteia.qalingo.core.service.MarketService;
import org.hoteia.qalingo.core.service.ProductSkuService;
import org.hoteia.qalingo.core.web.clickstream.ClickstreamSession;
import org.hoteia.qalingo.core.web.clickstream.ClickstreamRequest;
import org.hoteia.qalingo.core.web.util.RequestUtil;

/**
 * <p>
 * <a href="RequestUtilImpl.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author Denis Gosset <a href="http://www.hoteia.com"><i>Hoteia.com</i></a>
 * 
 */
@Service("requestUtil")
@Transactional
public class RequestUtilImpl implements RequestUtil {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected EngineSettingService engineSettingService;

    @Value("${env.name}")
    protected String environmentName;

    @Value("${app.name}")
    protected String applicationName;

    @Value("${context.name}")
    protected String contextName;

    @Autowired
    protected MarketPlaceService marketPlaceService;

    @Autowired
    protected MarketService marketService;

    @Autowired
    protected ProductSkuService productSkuService;

    @Autowired
    protected CustomerService customerService;

    @Autowired
    protected LocalizationService localizationService;

    /**
	 *
	 */
    public boolean isLocalHostMode(final HttpServletRequest request) throws Exception {
        if (StringUtils.isNotEmpty(getHost(request)) && (getHost(request).contains("localhost") || getHost(request).equalsIgnoreCase("127.0.0.1"))) {
            return true;
        }
        return false;
    }

    /**
	 *
	 */
    public String getHost(final HttpServletRequest request) throws Exception {
        return (String) request.getHeader(Constants.HOST);
    }

    /**
	 *
	 */
    public String getEnvironmentName() throws Exception {
        return environmentName;
    }

    /**
	 *
	 */
    public String getApplicationName() throws Exception {
        return applicationName;
    }

    /**
	 *
	 */
    public String getContextName() throws Exception {
        return contextName;
    }

    /**
	 *
	 */
    public DateFormat getFormatDate(final RequestData requestData, final int dateStyle, final int timeStyle) throws Exception {
        final Localization localization = requestData.getLocalization();
        final Locale locale = localization.getLocale();
        DateFormat formatter = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        return formatter;
    }

    /**
	 *
	 */
    public SimpleDateFormat getRssFormatDate(final RequestData requestData) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        return formatter;
    }

    /**
	 *
	 */
    public SimpleDateFormat getDataVocabularyFormatDate(final RequestData requestData) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter;
    }

    /**
	 *
	 */
    public SimpleDateFormat getAtomFormatDate(final RequestData requestData) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        return formatter;
    }

    /**
	 *
	 */
    public NumberFormat getCartItemPriceNumberFormat(final RequestData requestData, final String currencyCode) throws Exception {
        return getNumberFormat(requestData, currencyCode, RoundingMode.HALF_EVEN, 2, 2, 1000000, 1);
    }

    /**
	 *
	 */
    public NumberFormat getNumberFormat(final RequestData requestData, final String currencyCode, final RoundingMode roundingMode, final int maximumFractionDigits, final int minimumFractionDigits,
            final int maximumIntegerDigits, final int minimumIntegerDigits) throws Exception {
        final Localization localization = requestData.getLocalization();
        final Locale locale = localization.getLocale();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        formatter.setGroupingUsed(true);
        formatter.setParseIntegerOnly(false);
        formatter.setRoundingMode(roundingMode);
        Currency currency = Currency.getInstance(currencyCode);
        formatter.setCurrency(currency);

        formatter.setMaximumFractionDigits(maximumFractionDigits);
        formatter.setMinimumFractionDigits(minimumFractionDigits);

        formatter.setMaximumIntegerDigits(maximumIntegerDigits);
        formatter.setMinimumIntegerDigits(minimumIntegerDigits);

        return formatter;
    }

    /**
    * 
    */
    public ClickstreamSession getClickstreamSession(final HttpServletRequest request) throws Exception {
        ClickstreamSession clickstream = (ClickstreamSession) request.getSession().getAttribute(Constants.ENGINE_CLICKSTREAM);
        if(clickstream == null){
            clickstream = new ClickstreamSession();
            clickstream.setInitialReferrer(request.getHeader("REFERER"));
        }
        return clickstream;
    }
    
    /**
    * 
    */
    public void addClickstream(final HttpServletRequest request) throws Exception {
        ClickstreamSession clickstream = getClickstreamSession(request);
        Date lastRequest = new Date();
        clickstream.setLastRequest(lastRequest);
        clickstream.setHostname(request.getRemoteHost());
        
        clickstream.getRequests().add(new ClickstreamRequest(request, lastRequest));

        request.getSession().setAttribute(Constants.ENGINE_CLICKSTREAM, clickstream);
    }
    
    /**
    * 
    */
    public String getLastRequestUrlNotSecurity(final HttpServletRequest request) throws Exception {
        final List<String> excludedPatterns = new ArrayList<String>();
        excludedPatterns.add("login");
        excludedPatterns.add("auth");
        excludedPatterns.add("logout");
        excludedPatterns.add("timeout");
        excludedPatterns.add("forbidden");
        return getRequestUrl(request, excludedPatterns, 1);
    }

    /**
    * 
    */
    public String getCurrentRequestUrl(final HttpServletRequest request, final List<String> excludedPatterns) throws Exception {
        return getRequestUrl(request, excludedPatterns, 0);
    }

    /**
    * 
    */
    public String getCurrentRequestUrl(final HttpServletRequest request) throws Exception {
        return getRequestUrl(request, new ArrayList<String>(), 0);
    }

    /**
    * 
    */
    public String getCurrentRequestUrlNotSecurity(final HttpServletRequest request) throws Exception {
        final List<String> excludedPatterns = new ArrayList<String>();
        excludedPatterns.add("login");
        excludedPatterns.add("auth");
        excludedPatterns.add("logout");
        excludedPatterns.add("timeout");
        excludedPatterns.add("forbidden");
        return getRequestUrl(request, excludedPatterns, 0);
    }

    /**
    * 
    */
    public String getLastRequestUrl(final HttpServletRequest request, final List<String> excludedPatterns, String fallbackUrl) throws Exception {
        String url = getRequestUrl(request, excludedPatterns, 1);
        if (StringUtils.isEmpty(url)) {
            return fallbackUrl;
        }
        return url;
    }

    /**
    * 
    */
    public String getLastRequestUrl(final HttpServletRequest request, final List<String> excludedPatterns) throws Exception {
        return getRequestUrl(request, excludedPatterns, 1);
    }

    /**
    * 
    */
    public String getLastRequestUrl(final HttpServletRequest request) throws Exception {
        return getRequestUrl(request, new ArrayList<String>(), 1);
    }

    /**
    * 
    */
    public String getRequestUrl(final HttpServletRequest request, final List<String> excludedPatterns, int position) throws Exception {
        String url = Constants.EMPTY;
        ClickstreamSession clickstreamSession = getClickstreamSession(request);
        
        final List<ClickstreamRequest> clickstreams = clickstreamSession.getRequests();

        if (clickstreams != null && !clickstreams.isEmpty()) {
            if (clickstreams != null && !clickstreams.isEmpty()) {
                // Clean not html values or exluded patterns
                List<ClickstreamRequest> cleanClickstreams = new ArrayList<ClickstreamRequest>();
                Iterator<ClickstreamRequest> it = clickstreams.iterator();
                while (it.hasNext()) {
                    ClickstreamRequest clickstream = (ClickstreamRequest) it.next();
                    String uri = clickstream.getRequestURI();
                    if (uri.endsWith(".html")) {
                        // TEST IF THE URL IS EXCLUDE
                        CharSequence[] excludedPatternsCharSequence = excludedPatterns.toArray(new CharSequence[excludedPatterns.size()]);
                        boolean isExclude = false;
                        for (int i = 0; i < excludedPatternsCharSequence.length; i++) {
                            CharSequence string = excludedPatternsCharSequence[i];
                            if (uri.contains(string)) {
                                isExclude = true;
                            }
                        }
                        if (BooleanUtils.negate(isExclude)) {
                            cleanClickstreams.add(clickstream);
                        }
                    }
                }

                if (cleanClickstreams.size() == 1) {
                    Iterator<ClickstreamRequest> itCleanClickstreams = cleanClickstreams.iterator();
                    while (itCleanClickstreams.hasNext()) {
                        ClickstreamRequest clickstream = (ClickstreamRequest) itCleanClickstreams.next();
                        String uri = clickstream.getRequestURI();
                        url = uri;
                    }
                } else {
                    Iterator<ClickstreamRequest> itCleanClickstreams = cleanClickstreams.iterator();
                    int countCleanClickstream = 1;
                    while (itCleanClickstreams.hasNext()) {
                        ClickstreamRequest clickstream = (ClickstreamRequest) itCleanClickstreams.next();
                        String uri = clickstream.getRequestURI();
                        // The last url is the current URI, so we need to get the url previous the last
                        if (countCleanClickstream == (cleanClickstreams.size() - position)) {
                            url = uri;
                        }
                        countCleanClickstream++;
                    }
                }
            }
        }

        // CLEAN CONTEXT FROM URL
        if (StringUtils.isNotEmpty(url) && !isLocalHostMode(request) && url.contains(request.getContextPath())) {
            url = url.replace(request.getContextPath(), "");
        }
        return handleUrl(url);
    }

    /**
     * 
     */
    public String getRootAssetFilePath(final HttpServletRequest request) throws Exception {
        EngineSetting engineSetting = engineSettingService.getAssetFileRootPath();
        String prefixPath = "";
        if (engineSetting != null) {
            prefixPath = engineSetting.getDefaultValue();
        }
        if (prefixPath.endsWith("/")) {
            prefixPath = prefixPath.substring(0, prefixPath.length() - 1);
        }
        return prefixPath;
    }

    /**
     * 
     */
    public String getRootAssetWebPath(final HttpServletRequest request) throws Exception {
        EngineSetting engineSetting = engineSettingService.getAssetWebRootPath();
        String prefixPath = "";
        if (engineSetting != null) {
            prefixPath = engineSetting.getDefaultValue();
        }
        if (prefixPath.endsWith("/")) {
            prefixPath = prefixPath.substring(0, prefixPath.length() - 1);
        }
        return prefixPath;
    }

    /**
     * 
     */
    public String getCatalogImageWebPath(final HttpServletRequest request, final Asset asset) throws Exception {
        EngineSetting engineSetting = engineSettingService.getAssetCatalogFilePath();
        String prefixPath = "";
        if (engineSetting != null) {
            prefixPath = engineSetting.getDefaultValue();
        }
        String catalogImageWebPath = getRootAssetWebPath(request) + prefixPath + "/" + asset.getType().getPropertyKey().toLowerCase() + "/" + asset.getPath();
        if (catalogImageWebPath.endsWith("/")) {
            catalogImageWebPath = catalogImageWebPath.substring(0, catalogImageWebPath.length() - 1);
        }
        return catalogImageWebPath;
    }

    /**
     * 
     */
    public String getProductMarketingImageWebPath(final HttpServletRequest request, final Asset asset) throws Exception {
        EngineSetting engineSetting = engineSettingService.getAssetProductMarketingFilePath();
        String prefixPath = "";
        if (engineSetting != null) {
            prefixPath = engineSetting.getDefaultValue();
        }
        String productMarketingImageWebPath = getRootAssetWebPath(request) + prefixPath + "/" + asset.getType().getPropertyKey().toLowerCase() + "/" + asset.getPath();
        if (productMarketingImageWebPath.endsWith("/")) {
            productMarketingImageWebPath = productMarketingImageWebPath.substring(0, productMarketingImageWebPath.length() - 1);
        }
        return productMarketingImageWebPath;
    }

    /**
     * 
     */
    public String getProductSkuImageWebPath(final HttpServletRequest request, final Asset asset) throws Exception {
        EngineSetting engineSetting = engineSettingService.getAssetPoductSkuFilePath();
        String prefixPath = "";
        if (engineSetting != null) {
            prefixPath = engineSetting.getDefaultValue();
        }
        String productSkuImageWebPath = getRootAssetWebPath(request) + prefixPath + "/" + asset.getType().getPropertyKey().toLowerCase() + "/" + asset.getPath();
        if (productSkuImageWebPath.endsWith("/")) {
            productSkuImageWebPath = productSkuImageWebPath.substring(0, productSkuImageWebPath.length() - 1);
        }
        return productSkuImageWebPath;
    }

    /**
     * 
     */
    public String getCurrentThemeResourcePrefixPath(final HttpServletRequest request) throws Exception {
        EngineSetting engineSetting = engineSettingService.getThemeResourcePrefixPath();
        try {
            String contextValue = getCurrentContextNameValue(request);
            EngineSettingValue engineSettingValue = engineSetting.getEngineSettingValue(contextValue);
            String prefixPath = engineSetting.getDefaultValue();
            if (engineSettingValue != null) {
                prefixPath = engineSettingValue.getValue();
            } else {
                logger.warn("This engine setting is request, but doesn't exist: " + engineSetting.getCode() + "/" + contextValue);
            }
            String currentThemeResourcePrefixPath = prefixPath + getCurrentTheme(request);
            if (currentThemeResourcePrefixPath.endsWith("/")) {
                currentThemeResourcePrefixPath = currentThemeResourcePrefixPath.substring(0, currentThemeResourcePrefixPath.length() - 1);
            }
            return currentThemeResourcePrefixPath;

        } catch (Exception e) {
            logger.error("Context name, " + getContextName() + " can't be resolve by EngineSettingWebAppContext class.", e);
        }
        return null;
    }

    /**
     * 
     */
    public String getCurrentContextNameValue(final HttpServletRequest request) throws Exception {
        return EngineSettingWebAppContext.valueOf(getContextName()).getPropertyKey();
    }

    /**
     * 
     */
    public String getCurrentVelocityWebPrefix(final HttpServletRequest request) throws Exception {
        String velocityPath = "/" + getCurrentTheme(request) + "/www/" + getCurrentDevice(request) + "/content/";
        return velocityPath;
    }

    /**
     * 
     */
    public String getCurrentVelocityEmailPrefix(final HttpServletRequest request) throws Exception {
        String velocityPath = "/" + getCurrentTheme(request) + "/email/";
        return velocityPath;
    }

    /**
     * 
     */
    protected String handleUrl(String url) {
        return url;
    }

    /**
     * 
     */
    public EngineEcoSession getCurrentEcoSession(final HttpServletRequest request) throws Exception {
        EngineEcoSession engineEcoSession = (EngineEcoSession) request.getSession().getAttribute(Constants.ENGINE_ECO_SESSION_OBJECT);
        engineEcoSession = checkEngineEcoSession(request, engineEcoSession);
        return engineEcoSession;
    }

    /**
     * 
     */
    public void updateCurrentEcoSession(final HttpServletRequest request, final EngineEcoSession engineEcoSession) throws Exception {
        setCurrentEcoSession(request, engineEcoSession);
    }

    /**
     * 
     */
    public void setCurrentEcoSession(final HttpServletRequest request, final EngineEcoSession engineEcoSession) throws Exception {
        request.getSession().setAttribute(Constants.ENGINE_ECO_SESSION_OBJECT, engineEcoSession);
    }

    /**
     * 
     */
    public EngineBoSession getCurrentBoSession(final HttpServletRequest request) throws Exception {
        EngineBoSession engineBoSession = (EngineBoSession) request.getSession().getAttribute(Constants.ENGINE_BO_SESSION_OBJECT);
        engineBoSession = checkEngineBoSession(request, engineBoSession);
        return engineBoSession;
    }

    /**
     * 
     */
    public void updateCurrentBoSession(final HttpServletRequest request, final EngineBoSession engineBoSession) throws Exception {
        setCurrentBoSession(request, engineBoSession);
    }

    /**
     * 
     */
    public void setCurrentBoSession(final HttpServletRequest request, final EngineBoSession engineBoSession) throws Exception {
        request.getSession().setAttribute(Constants.ENGINE_BO_SESSION_OBJECT, engineBoSession);
    }

    /**
     * 
     */
    public Cart getCurrentCart(final HttpServletRequest request) throws Exception {
        EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
        return engineEcoSession.getCart();
    }

    /**
     * 
     */
    public void updateCurrentCart(final HttpServletRequest request, final Cart cart) throws Exception {
        EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
        engineEcoSession.setCart(cart);
        updateCurrentEcoSession(request, engineEcoSession);
    }

    /**
     * 
     */
    public void updateCurrentCart(final HttpServletRequest request, final String skuCode, final int quantity) throws Exception {
        // SANITY CHECK : sku code is empty or null : no sense
        if (StringUtils.isEmpty(skuCode)) {
            throw new Exception("");
        }

        // SANITY CHECK : quantity is equal zero : no sense
        if (quantity == 0) {
            throw new Exception("");
        }

        Cart cart = getCurrentCart(request);
        Set<CartItem> cartItems = cart.getCartItems();
        boolean productSkuIsNew = true;
        for (Iterator<CartItem> iterator = cartItems.iterator(); iterator.hasNext();) {
            CartItem cartItem = (CartItem) iterator.next();
            if (cartItem.getProductSkuCode().equalsIgnoreCase(skuCode)) {
                int newQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(newQuantity);
                productSkuIsNew = false;
            }
        }
        if (productSkuIsNew) {
            final MarketArea marketArea = getCurrentMarketArea(request);
            final Retailer retailer = getCurrentRetailer(request);
            CartItem cartItem = new CartItem();
            ProductSku productSku = productSkuService.getProductSkuByCode(marketArea.getId(), retailer.getId(), skuCode);
            cartItem.setProductSkuCode(skuCode);
            cartItem.setProductSku(productSku);
            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem);
        }
        updateCurrentCart(request, cart);

        // TODO update session/cart db ?
    }

    /**
     * 
     */
    public void updateCurrentCart(final HttpServletRequest request, final Long billingAddressId, final Long shippingAddressId) throws Exception {
        Cart cart = getCurrentCart(request);
        cart.setBillingAddressId(billingAddressId);
        cart.setShippingAddressId(shippingAddressId);
        updateCurrentCart(request, cart);

        // TODO update session/cart db ?
    }

    /**
     * 
     */
    public void cleanCurrentCart(final HttpServletRequest request) throws Exception {
        Cart cart = new Cart();
        updateCurrentCart(request, cart);

        // TODO update session/cart db ?
    }

    /**
     * 
     */
    public Order getLastOrder(final HttpServletRequest request) throws Exception {
        EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
        return engineEcoSession.getLastOrder();
    }

    /**
     * 
     */
    public void saveLastOrder(final HttpServletRequest request, final Order order) throws Exception {
        if (order != null) {
            EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
            engineEcoSession.setLastOrder(order);
            updateCurrentEcoSession(request, engineEcoSession);
        }
    }

    /**
     * 
     */
    public void removeCartItemFromCurrentCart(final HttpServletRequest request, final String skuCode) throws Exception {
        Cart cart = getCurrentCart(request);
        Set<CartItem> cartItems = cart.getCartItems();
        for (Iterator<CartItem> iterator = cartItems.iterator(); iterator.hasNext();) {
            CartItem cartItem = (CartItem) iterator.next();
            if (cartItem.getProductSkuCode().equalsIgnoreCase(skuCode)) {
                cartItems.remove(cartItem);
            }
        }
        cart.setCartItems(cartItems);
        updateCurrentCart(request, cart);

        // TODO update session/cart db ?

    }

    /**
     * 
     */
    public MarketPlace getCurrentMarketPlace(final HttpServletRequest request) throws Exception {
        MarketPlace marketPlace = null;
        if (isBackoffice()) {
            EngineBoSession engineBoSession = getCurrentBoSession(request);
            marketPlace = engineBoSession.getCurrentMarketPlace();
            if (marketPlace == null) {
                initDefaultBoMarketPlace(request);
                marketPlace = engineBoSession.getCurrentMarketPlace();
            }
        } else {
            EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
            marketPlace = engineEcoSession.getCurrentMarketPlace();
            if (marketPlace == null) {
                initDefaultEcoMarketPlace(request);
                marketPlace = engineEcoSession.getCurrentMarketPlace();
            }
        }
        return marketPlace;
    }

    /**
     * 
     */
    public Market getCurrentMarket(final HttpServletRequest request) throws Exception {
        Market market = null;
        if (isBackoffice()) {
            EngineBoSession engineBoSession = getCurrentBoSession(request);
            market = engineBoSession.getCurrentMarket();
            if (market == null) {
                initDefaultEcoMarketPlace(request);
                market = engineBoSession.getCurrentMarket();
            }
        } else {
            EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
            market = engineEcoSession.getCurrentMarket();
            if (market == null) {
                initDefaultEcoMarketPlace(request);
                market = engineEcoSession.getCurrentMarket();
            }
        }
        return market;
    }

    /**
     * 
     */
    public MarketArea getCurrentMarketArea(final HttpServletRequest request) throws Exception {
        MarketArea marketArea = null;
        if (isBackoffice()) {
            EngineBoSession engineBoSession = getCurrentBoSession(request);
            marketArea = engineBoSession.getCurrentMarketArea();
            if (marketArea == null) {
                initDefaultBoMarketPlace(request);
                marketArea = engineBoSession.getCurrentMarketArea();
            }
        } else {
            EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
            marketArea = engineEcoSession.getCurrentMarketArea();
            if (marketArea == null) {
                initDefaultEcoMarketPlace(request);
                marketArea = engineEcoSession.getCurrentMarketArea();
            }
        }
        return marketArea;
    }

    /**
     * 
     */
    public Localization getCurrentMarketLocalization(final HttpServletRequest request) throws Exception {
        Localization localization = null;
        if (isBackoffice()) {
            EngineBoSession engineBoSession = getCurrentBoSession(request);
            localization = engineBoSession.getCurrentMarketLocalization();
        } else {
            EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
            localization = engineEcoSession.getCurrentLocalization();
        }
        return localization;
    }

    /**
     * 
     */
    public Localization getCurrentLocalization(final HttpServletRequest request) throws Exception {
        Localization localization = null;
        if (isBackoffice()) {
            EngineBoSession engineBoSession = getCurrentBoSession(request);
            localization = engineBoSession.getCurrentMarketLocalization();
        } else {
            EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
            localization = engineEcoSession.getCurrentLocalization();
        }
        return localization;
    }

    /**
     * 
     */
    public Locale getCurrentLocale(final HttpServletRequest request) throws Exception {
        Localization localization = getCurrentLocalization(request);
        if (localization != null) {
            return localization.getLocale();
        } else {
            logger.warn("Current Locale is null and it is not possible. Need to reset session.");
            if (isBackoffice()) {
                initDefaultBoMarketPlace(request);
            } else {
                initDefaultEcoMarketPlace(request);
            }
            localization = getCurrentLocalization(request);
            return localization.getLocale();
        }
    }

    /**
     * 
     */
    public void updateCurrentLocalization(final HttpServletRequest request, final Localization localization) throws Exception {
        if (localization != null) {
            if (isBackoffice()) {
                EngineBoSession engineBoSession = getCurrentBoSession(request);
                engineBoSession.setCurrentLocalization(localization);
                updateCurrentBoSession(request, engineBoSession);
            } else {
                EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
                engineEcoSession.setCurrentLocalization(localization);
                updateCurrentEcoSession(request, engineEcoSession);
            }
        }
    }

    /**
     * 
     */
    public Retailer getCurrentRetailer(final HttpServletRequest request) throws Exception {
        Retailer retailer = null;
        if (isBackoffice()) {
            EngineBoSession engineBoSession = getCurrentBoSession(request);
            retailer = engineBoSession.getCurrentRetailer();
            if (retailer == null) {
                initDefaultBoMarketPlace(request);
            }
        } else {
            EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
            retailer = engineEcoSession.getCurrentRetailer();
            if (retailer == null) {
                initDefaultEcoMarketPlace(request);
            }
        }
        return retailer;
    }

    /**
     * 
     */
    public Customer getCurrentCustomer(final HttpServletRequest request) throws Exception {
        EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
        Customer customer = engineEcoSession.getCurrentCustomer();
        if (customer == null) {
            // CHECK
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                if (StringUtils.isNotEmpty(username) && !username.equalsIgnoreCase("anonymousUser")) {
                    customer = customerService.getCustomerByLoginOrEmail(username);
                    updateCurrentCustomer(request, customer);
                }
            }
        }
        return customer;
    }

    /**
     * 
     */
    public String getCustomerAvatar(final HttpServletRequest request, final Customer customer) throws Exception {
        String customerAvatar = null;
        if (customer != null) {
            if (StringUtils.isNotEmpty(customer.getAvatarImg())) {
                customerAvatar = customer.getAvatarImg();
            } else {
                String email = customer.getEmail().toLowerCase().trim();
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] array = md.digest(email.getBytes("CP1252"));
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < array.length; ++i) {
                    sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
                }
                String gravatarId = sb.toString();
                if ("https".equals(request.getScheme())) {
                    customerAvatar = "https://secure.gravatar.com/avatar/" + gravatarId;
                } else {
                    customerAvatar = "http://www.gravatar.com/avatar/" + gravatarId;
                }
            }
        }
        return customerAvatar;
    }

    /**
     * 
     */
    public boolean hasKnownCustomerLogged(final HttpServletRequest request) throws Exception {
        final Customer customer = getCurrentCustomer(request);
        if (customer != null) {
            return true;
        }
        return false;
    }

    /**
     * 
     */
    public Long getCurrentCustomerId(final HttpServletRequest request) throws Exception {
        Customer customer = getCurrentCustomer(request);
        if (customer == null) {
            return null;
        }
        return customer.getId();
    }

    /**
     * 
     */
    public String getCurrentCustomerLogin(final HttpServletRequest request) throws Exception {
        EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
        Customer customer = engineEcoSession.getCurrentCustomer();
        if (customer == null) {
            return null;
        }
        return customer.getLogin();
    }

    /**
     * 
     */
    public void updateCurrentCustomer(final HttpServletRequest request, final Customer customer) throws Exception {
        if (customer != null) {
            final EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
            engineEcoSession.setCurrentCustomer(customer);
            updateCurrentEcoSession(request, engineEcoSession);
        }
    }

    /**
     * 
     */
    public void cleanCurrentCustomer(final HttpServletRequest request) throws Exception {
        final EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
        engineEcoSession.setCurrentCustomer(null);
        updateCurrentEcoSession(request, engineEcoSession);
    }

    /**
	  * 
	  */
    public void handleFrontofficeUrlParameters(final HttpServletRequest request) throws Exception {
        String marketPlaceCode = request.getParameter(RequestConstants.REQUEST_PARAMETER_MARKET_PLACE_CODE);
        String marketCode = request.getParameter(RequestConstants.REQUEST_PARAMETER_MARKET_CODE);
        String marketAreaCode = request.getParameter(RequestConstants.REQUEST_PARAMETER_MARKET_AREA_CODE);
        String localeCode = request.getParameter(RequestConstants.REQUEST_PARAMETER_MARKET_LANGUAGE);
        String retailerCode = request.getParameter(RequestConstants.REQUEST_PARAMETER_RETAILER_CODE);
        EngineEcoSession engineEcoSession = getCurrentEcoSession(request);

        MarketPlace currentMarketPlace = engineEcoSession.getCurrentMarketPlace();
        if (StringUtils.isNotEmpty(marketPlaceCode) && StringUtils.isNotEmpty(marketCode) && StringUtils.isNotEmpty(marketAreaCode) && StringUtils.isNotEmpty(localeCode)) {
            if (currentMarketPlace != null && !currentMarketPlace.getCode().equalsIgnoreCase(marketPlaceCode)) {
                // RESET ALL SESSION AND CHANGE THE MARKET PLACE
                resetCart(request);
                initEcoSession(request);
                MarketPlace newMarketPlace = marketPlaceService.getMarketPlaceByCode(marketPlaceCode);
                if (newMarketPlace == null) {
                    // INIT A DEFAULT MARKET PLACE
                    initDefaultEcoMarketPlace(request);
                } else {
                    // MARKET PLACE
                    engineEcoSession.setCurrentMarketPlace(newMarketPlace);
                    updateCurrentTheme(request, newMarketPlace.getTheme());

                    // MARKET
                    Market market = newMarketPlace.getMarket(marketCode);
                    if (market == null) {
                        market = newMarketPlace.getDefaultMarket();
                    }
                    engineEcoSession.setCurrentMarket(market);

                    // MARKET MODE
                    MarketArea marketArea = market.getMarketArea(marketAreaCode);
                    if (marketArea == null) {
                        marketArea = market.getDefaultMarketArea();
                    }
                    // TODO : why : SET A RELOAD OBJECT MARKET AREA -> event
                    // LazyInitializationException: could not initialize proxy -
                    // no Session
                    engineEcoSession.setCurrentMarketArea(marketService.getMarketAreaById(marketArea.getId().toString()));

                    // LOCALE
                    Localization localization = marketArea.getLocalization(localeCode);
                    if (localization == null) {
                        Localization defaultLocalization = marketArea.getDefaultLocalization();
                        engineEcoSession.setCurrentLocalization(defaultLocalization);
                    } else {
                        engineEcoSession.setCurrentLocalization(localization);
                    }

                    // RETAILER
                    Retailer retailer = marketArea.getRetailer(retailerCode);
                    if (retailer == null) {
                        Retailer defaultRetailer = marketArea.getDefaultRetailer();
                        engineEcoSession.setCurrentRetailer(defaultRetailer);
                    } else {
                        engineEcoSession.setCurrentRetailer(retailer);
                    }
                }

            } else {
                Market market = engineEcoSession.getCurrentMarket();
                if (market != null && !market.getCode().equalsIgnoreCase(marketCode)) {
                    // RESET CART
                    resetCart(request);

                    // CHANGE THE MARKET
                    Market newMarket = marketService.getMarketByCode(marketCode);
                    if (newMarket == null) {
                        newMarket = currentMarketPlace.getDefaultMarket();
                    }
                    engineEcoSession.setCurrentMarket(newMarket);
                    updateCurrentTheme(request, newMarket.getTheme());

                    // MARKET MODE
                    MarketArea marketArea = newMarket.getMarketArea(marketAreaCode);
                    if (marketArea == null) {
                        marketArea = market.getDefaultMarketArea();
                    }
                    // TODO : why : SET A RELOAD OBJECT MARKET AREA -> event
                    // LazyInitializationException: could not initialize proxy -
                    // no Session
                    engineEcoSession.setCurrentMarketArea(marketService.getMarketAreaById(marketArea.getId().toString()));

                    // LOCALE
                    Localization localization = marketArea.getLocalization(localeCode);
                    if (localization == null) {
                        Localization defaultLocalization = marketArea.getDefaultLocalization();
                        engineEcoSession.setCurrentLocalization(defaultLocalization);
                    } else {
                        engineEcoSession.setCurrentLocalization(localization);
                    }

                    // RETAILER
                    Retailer retailer = marketArea.getRetailer(retailerCode);
                    if (retailer == null) {
                        Retailer defaultRetailer = marketArea.getDefaultRetailer();
                        engineEcoSession.setCurrentRetailer(defaultRetailer);
                    } else {
                        engineEcoSession.setCurrentRetailer(retailer);
                    }
                } else {
                    MarketArea marketArea = engineEcoSession.getCurrentMarketArea();
                    if (marketArea != null && !marketArea.getCode().equalsIgnoreCase(marketAreaCode)) {
                        // RESET CART
                        resetCart(request);

                        // CHANGE THE MARKET MODE
                        MarketArea newMarketArea = market.getMarketArea(marketAreaCode);
                        if (newMarketArea == null) {
                            newMarketArea = market.getDefaultMarketArea();
                        }
                        // TODO : why : SET A RELOAD OBJECT MARKET AREA -> event
                        // LazyInitializationException: could not initialize
                        // proxy - no Session
                        engineEcoSession.setCurrentMarketArea(marketService.getMarketAreaById(newMarketArea.getId().toString()));
                        updateCurrentTheme(request, newMarketArea.getTheme());

                        // LOCALE
                        Localization localization = newMarketArea.getLocalization(localeCode);
                        if (localization == null) {
                            Localization defaultLocalization = marketArea.getDefaultLocalization();
                            engineEcoSession.setCurrentLocalization(defaultLocalization);
                        } else {
                            engineEcoSession.setCurrentLocalization(localization);
                        }

                        // RETAILER
                        Retailer retailer = marketArea.getRetailer(retailerCode);
                        if (retailer == null) {
                            Retailer defaultRetailer = marketArea.getDefaultRetailer();
                            engineEcoSession.setCurrentRetailer(defaultRetailer);
                        } else {
                            engineEcoSession.setCurrentRetailer(retailer);
                        }
                    } else {
                        Localization localization = engineEcoSession.getCurrentLocalization();
                        if (localization != null && !localization.getLocale().toString().equalsIgnoreCase(localeCode)) {
                            // CHANGE THE LOCALE
                            Localization newLocalization = marketArea.getLocalization(localeCode);
                            if (newLocalization == null) {
                                Localization defaultLocalization = marketArea.getDefaultLocalization();
                                engineEcoSession.setCurrentLocalization(defaultLocalization);
                            } else {
                                engineEcoSession.setCurrentLocalization(newLocalization);
                            }

                            // RETAILER
                            Retailer retailer = marketArea.getRetailer(retailerCode);
                            if (retailer == null) {
                                Retailer defaultRetailer = marketArea.getDefaultRetailer();
                                engineEcoSession.setCurrentRetailer(defaultRetailer);
                            } else {
                                engineEcoSession.setCurrentRetailer(retailer);
                            }

                        } else {
                            Retailer retailer = engineEcoSession.getCurrentRetailer();
                            if (retailer != null && !retailer.getCode().toString().equalsIgnoreCase(retailerCode)) {
                                // CHANGE THE RETAILER
                                Retailer newRetailer = marketArea.getRetailer(retailerCode);
                                if (newRetailer == null) {
                                    Retailer defaultRetailer = marketArea.getDefaultRetailer();
                                    engineEcoSession.setCurrentRetailer(defaultRetailer);
                                } else {
                                    engineEcoSession.setCurrentRetailer(newRetailer);
                                }
                            }
                        }
                    }
                }
            }
        }

        // THEME
        final MarketArea marketArea = getCurrentMarketArea(request);
        String themeFolder = "default";
        if (StringUtils.isNotEmpty(marketArea.getTheme())) {
            themeFolder = marketArea.getTheme();
        }
        updateCurrentTheme(request, themeFolder);

        // SAVE THE ENGINE SESSION
        updateCurrentEcoSession(request, engineEcoSession);
    }

    /**
     * 
     */
    public User getCurrentUser(final HttpServletRequest request) throws Exception {
        EngineBoSession engineBoSession = getCurrentBoSession(request);
        return engineBoSession.getCurrentUser();
    }

    /**
     * 
     */
    public Long getCurrentUserId(final HttpServletRequest request) throws Exception {
        User user = getCurrentUser(request);
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    /**
     * 
     */
    public void updateCurrentUser(final HttpServletRequest request, final User user) throws Exception {
        if (user != null) {
            final EngineBoSession engineBoSSession = getCurrentBoSession(request);
            engineBoSSession.setCurrentUser(user);
            updateCurrentBoSession(request, engineBoSSession);
        }
    }

    /**
	  * 
	  */
    public void handleBackofficeUrlParameters(final HttpServletRequest request) throws Exception {
        String marketPlaceCode = request.getParameter(RequestConstants.REQUEST_PARAMETER_MARKET_PLACE_CODE);
        String marketCode = request.getParameter(RequestConstants.REQUEST_PARAMETER_MARKET_CODE);
        String marketAreaCode = request.getParameter(RequestConstants.REQUEST_PARAMETER_MARKET_AREA_CODE);
        String marketLanguageCode = request.getParameter(RequestConstants.REQUEST_PARAMETER_MARKET_LANGUAGE);
        String retailerCode = request.getParameter(RequestConstants.REQUEST_PARAMETER_RETAILER_CODE);

        EngineBoSession engineBoSession = getCurrentBoSession(request);

        MarketPlace currentMarketPlace = engineBoSession.getCurrentMarketPlace();
        if (StringUtils.isNotEmpty(marketPlaceCode) && StringUtils.isNotEmpty(marketCode) && StringUtils.isNotEmpty(marketAreaCode) && StringUtils.isNotEmpty(marketLanguageCode)) {
            if (currentMarketPlace != null && !currentMarketPlace.getCode().equalsIgnoreCase(marketPlaceCode)) {
                // RESET ALL SESSION AND CHANGE THE MARKET PLACE
                initBoSession(request);
                MarketPlace newMarketPlace = marketPlaceService.getMarketPlaceByCode(marketPlaceCode);
                if (newMarketPlace == null) {
                    // INIT A DEFAULT MARKET PLACE
                    initDefaultBoMarketPlace(request);
                } else {
                    // MARKET PLACE
                    engineBoSession.setCurrentMarketPlace(newMarketPlace);
                    updateCurrentTheme(request, newMarketPlace.getTheme());

                    // MARKET
                    Market market = newMarketPlace.getMarket(marketCode);
                    if (market == null) {
                        market = newMarketPlace.getDefaultMarket();
                    }
                    engineBoSession.setCurrentMarket(market);

                    // MARKET MODE
                    MarketArea marketArea = market.getMarketArea(marketAreaCode);
                    if (marketArea == null) {
                        marketArea = market.getDefaultMarketArea();
                    }
                    // TODO : why : SET A RELOAD OBJECT MARKET AREA -> event
                    // LazyInitializationException: could not initialize proxy -
                    // no Session
                    engineBoSession.setCurrentMarketArea(marketService.getMarketAreaById(marketArea.getId().toString()));

                    // LOCALE
                    Localization localization = marketArea.getLocalization(marketLanguageCode);
                    if (localization == null) {
                        Localization defaultLocalization = marketArea.getDefaultLocalization();
                        engineBoSession.setCurrentMarketLocalization(defaultLocalization);
                    } else {
                        engineBoSession.setCurrentMarketLocalization(localization);
                    }

                    // RETAILER
                    Retailer retailer = marketArea.getRetailer(retailerCode);
                    if (retailer == null) {
                        Retailer defaultRetailer = marketArea.getDefaultRetailer();
                        engineBoSession.setCurrentRetailer(defaultRetailer);
                    } else {
                        engineBoSession.setCurrentRetailer(retailer);
                    }
                }

            } else {
                Market market = engineBoSession.getCurrentMarket();
                if (market != null && !market.getCode().equalsIgnoreCase(marketCode)) {

                    // CHANGE THE MARKET
                    Market newMarket = marketService.getMarketByCode(marketCode);
                    if (newMarket == null) {
                        newMarket = currentMarketPlace.getDefaultMarket();
                    }
                    engineBoSession.setCurrentMarket(newMarket);
                    updateCurrentTheme(request, newMarket.getTheme());

                    // MARKET MODE
                    MarketArea marketArea = newMarket.getMarketArea(marketAreaCode);
                    if (marketArea == null) {
                        marketArea = market.getDefaultMarketArea();
                    }
                    // TODO : why : SET A RELOAD OBJECT MARKET AREA -> event
                    // LazyInitializationException: could not initialize proxy -
                    // no Session
                    engineBoSession.setCurrentMarketArea(marketService.getMarketAreaById(marketArea.getId().toString()));

                    // LOCALE
                    Localization localization = marketArea.getLocalization(marketLanguageCode);
                    if (localization == null) {
                        Localization defaultLocalization = marketArea.getDefaultLocalization();
                        engineBoSession.setCurrentMarketLocalization(defaultLocalization);
                    } else {
                        engineBoSession.setCurrentMarketLocalization(localization);
                    }

                    // RETAILER
                    Retailer retailer = marketArea.getRetailer(retailerCode);
                    if (retailer == null) {
                        Retailer defaultRetailer = marketArea.getDefaultRetailer();
                        engineBoSession.setCurrentRetailer(defaultRetailer);
                    } else {
                        engineBoSession.setCurrentRetailer(retailer);
                    }
                } else {
                    MarketArea marketArea = engineBoSession.getCurrentMarketArea();
                    if (marketArea != null && !marketArea.getCode().equalsIgnoreCase(marketAreaCode)) {

                        // CHANGE THE MARKET MODE
                        MarketArea newMarketArea = market.getMarketArea(marketAreaCode);
                        if (newMarketArea == null) {
                            newMarketArea = market.getDefaultMarketArea();
                        }
                        // TODO : why : SET A RELOAD OBJECT MARKET AREA -> event
                        // LazyInitializationException: could not initialize
                        // proxy - no Session
                        engineBoSession.setCurrentMarketArea(marketService.getMarketAreaById(newMarketArea.getId().toString()));

                        updateCurrentTheme(request, newMarketArea.getTheme());

                        // LOCALE
                        Localization localization = newMarketArea.getLocalization(marketLanguageCode);
                        if (localization == null) {
                            Localization defaultLocalization = marketArea.getDefaultLocalization();
                            engineBoSession.setCurrentMarketLocalization(defaultLocalization);
                        } else {
                            engineBoSession.setCurrentMarketLocalization(localization);
                        }

                        // RETAILER
                        Retailer retailer = marketArea.getRetailer(retailerCode);
                        if (retailer == null) {
                            Retailer defaultRetailer = marketArea.getDefaultRetailer();
                            engineBoSession.setCurrentRetailer(defaultRetailer);
                        } else {
                            engineBoSession.setCurrentRetailer(retailer);
                        }
                    } else {
                        Localization localization = engineBoSession.getCurrentMarketLocalization();
                        if (localization != null && !localization.getLocale().toString().equalsIgnoreCase(marketLanguageCode)) {
                            // CHANGE THE LOCALE
                            Localization newLocalization = marketArea.getLocalization(marketLanguageCode);
                            if (newLocalization == null) {
                                Localization defaultLocalization = marketArea.getDefaultLocalization();
                                engineBoSession.setCurrentMarketLocalization(defaultLocalization);
                            } else {
                                engineBoSession.setCurrentMarketLocalization(newLocalization);
                            }

                            // RETAILER
                            Retailer retailer = marketArea.getRetailer(retailerCode);
                            if (retailer == null) {
                                Retailer defaultRetailer = marketArea.getDefaultRetailer();
                                engineBoSession.setCurrentRetailer(defaultRetailer);
                            } else {
                                engineBoSession.setCurrentRetailer(retailer);
                            }

                        } else {
                            Retailer retailer = engineBoSession.getCurrentRetailer();
                            if (retailer != null && !retailer.getCode().toString().equalsIgnoreCase(retailerCode)) {
                                // CHANGE THE RETAILER
                                Retailer newRetailer = marketArea.getRetailer(retailerCode);
                                if (newRetailer == null) {
                                    Retailer defaultRetailer = marketArea.getDefaultRetailer();
                                    engineBoSession.setCurrentRetailer(defaultRetailer);
                                } else {
                                    engineBoSession.setCurrentRetailer(newRetailer);
                                }
                            }
                        }
                    }
                }
            }
        }

        // CHECK BACKOFFICE LANGUAGES
        String localeCode = request.getParameter(RequestConstants.REQUEST_PARAMETER_LOCALE_CODE);
        // LOCALIZATIONS
        Company company = getCurrentCompany(request);
        if (company != null) {
            Localization localization = company.getLocalization(localeCode);
            if (localization != null) {
                engineBoSession.setCurrentLocalization(localization);
            }
        }

        // SAVE THE ENGINE SESSION
        updateCurrentBoSession(request, engineBoSession);
    }

    /**
     * 
     */
    @Override
    public String getCurrentTheme(final HttpServletRequest request) throws Exception {
        EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
        String currenTheme = engineEcoSession.getTheme();
        // SANITY CHECK
        if (StringUtils.isEmpty(currenTheme)) {
            return "default";
        }
        return currenTheme;
    }

    /**
     * 
     */
    @Override
    public void updateCurrentTheme(final HttpServletRequest request, final String theme) throws Exception {
        final EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
        if (StringUtils.isNotEmpty(theme)) {
            engineEcoSession.setTheme(theme);
            updateCurrentEcoSession(request, engineEcoSession);
        }
    }

    /**
     * 
     */
    @Override
    public String getCurrentDevice(final HttpServletRequest request) throws Exception {
        String currenDevice = "default";
        if (isBackoffice()) {
            EngineBoSession engineBoSession = getCurrentBoSession(request);
            if (StringUtils.isNotEmpty(engineBoSession.getDevice())) {
                currenDevice = engineBoSession.getDevice();
            }
        } else {
            EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
            if (StringUtils.isNotEmpty(engineEcoSession.getDevice())) {
                currenDevice = engineEcoSession.getDevice();
            }
        }
        return currenDevice;
    }

    /**
     * 
     */
    @Override
    public void updateCurrentDevice(final HttpServletRequest request, final String device) throws Exception {
        if (isBackoffice()) {
            final EngineBoSession engineBoSession = getCurrentBoSession(request);
            if (StringUtils.isNotEmpty(device)) {
                engineBoSession.setDevice(device);
                updateCurrentBoSession(request, engineBoSession);
            }
        } else {
            final EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
            if (StringUtils.isNotEmpty(device)) {
                engineEcoSession.setDevice(device);
                updateCurrentEcoSession(request, engineEcoSession);
            }
        }
    }

    /**
     * 
     */
    public Company getCurrentCompany(final HttpServletRequest request) throws Exception {
        EngineBoSession engineBoSession = getCurrentBoSession(request);
        User user = engineBoSession.getCurrentUser();
        if (user == null) {
            return null;
        }
        return user.getCompany();
    }

    /**
     * 
     */
    public RequestData getRequestData(final HttpServletRequest request) throws Exception {
        RequestData requestData = new RequestData();
        requestData.setRequest(request);
        String contextPath = "";
        if (request.getRequestURL().toString().contains("localhost") || request.getRequestURL().toString().contains("127.0.0.1")) {
            contextPath = contextPath + request.getContextPath() + "/";
        } else {
            contextPath = "/";
        }
        requestData.setContextPath(contextPath);
        requestData.setContextNameValue(getCurrentContextNameValue(request));

        requestData.setVelocityEmailPrefix(getCurrentVelocityEmailPrefix(request));

        requestData.setMarketPlace(getCurrentMarketPlace(request));
        requestData.setMarket(getCurrentMarket(request));
        requestData.setMarketArea(getCurrentMarketArea(request));
        requestData.setLocalization(getCurrentLocalization(request));
        requestData.setRetailer(getCurrentRetailer(request));

        Customer customer = getCurrentCustomer(request);
        if (customer != null) {
            requestData.setCustomer(customer);
        }

        return requestData;
    }

    /**
	 * 
	 */
    protected EngineEcoSession initEcoSession(final HttpServletRequest request) throws Exception {
        final EngineEcoSession engineEcoSession = new EngineEcoSession();
        EngineSetting engineSettingEnvironmentStagingModeEnabled = engineSettingService.getEnvironmentStagingModeEnabled();
        if (engineSettingEnvironmentStagingModeEnabled != null) {
            engineEcoSession.setEnvironmentStagingModeEnabled(BooleanUtils.toBoolean(engineSettingEnvironmentStagingModeEnabled.getDefaultValue()));
        } else {
            engineEcoSession.setEnvironmentStagingModeEnabled(false);
            logger.warn("Environment Type is not define in your database. Check the " + EngineSettingService.ENGINE_SETTING_ENVIRONMENT_STAGING_MODE_ENABLED + " value in settings table.");
        }
        EngineSetting engineSettingEnvironmentType = engineSettingService.getEnvironmentType();
        if (engineSettingEnvironmentType != null) {
            String environmentType = engineSettingEnvironmentType.getDefaultValue();
            try {
                engineEcoSession.setEnvironmentType(EnvironmentType.valueOf(environmentType));
            } catch (Exception e) {
                logger.error("Environment Type has wrong value define in your database. Check the " + EngineSettingService.ENGINE_SETTING_ENVIRONMENT_TYPE + " value in settings table.");
            }
        } else {
            engineEcoSession.setEnvironmentType(EnvironmentType.REEL);
            logger.warn("Environment Type is not define in your database. Check the " + EngineSettingService.ENGINE_SETTING_ENVIRONMENT_TYPE + " value in settings table.");
        }

        // INIT STAGING OR REEL ENVIRONMENT

        setCurrentEcoSession(request, engineEcoSession);
        String jSessionId = request.getSession().getId();
        engineEcoSession.setjSessionId(jSessionId);
        initCart(request);
        initDefaultEcoMarketPlace(request);
        updateCurrentEcoSession(request, engineEcoSession);
        return engineEcoSession;
    }

    /**
     * 
     */
    protected EngineEcoSession checkEngineEcoSession(final HttpServletRequest request, EngineEcoSession engineEcoSession) throws Exception {
        if (engineEcoSession == null) {
            engineEcoSession = initEcoSession(request);
        }
        String jSessionId = request.getSession().getId();
        if (!engineEcoSession.getjSessionId().equals(jSessionId)) {
            engineEcoSession.setjSessionId(jSessionId);
            updateCurrentEcoSession(request, engineEcoSession);
        }
        return engineEcoSession;
    }

    /**
     * 
     */
    protected EngineBoSession checkEngineBoSession(final HttpServletRequest request, EngineBoSession engineBoSession) throws Exception {
        if (engineBoSession == null) {
            engineBoSession = initBoSession(request);
        }
        String jSessionId = request.getSession().getId();
        if (!engineBoSession.getjSessionId().equals(jSessionId)) {
            engineBoSession.setjSessionId(jSessionId);
            updateCurrentBoSession(request, engineBoSession);
        }
        return engineBoSession;
    }

    /**
     * 
     */
    protected void initDefaultBoMarketPlace(final HttpServletRequest request) throws Exception {
        final EngineBoSession engineBoSession = getCurrentBoSession(request);
        MarketPlace marketPlace = marketPlaceService.getDefaultMarketPlace();
        engineBoSession.setCurrentMarketPlace(marketPlace);

        Market market = marketPlace.getDefaultMarket();
        engineBoSession.setCurrentMarket(market);

        MarketArea marketArea = market.getDefaultMarketArea();
        // TODO : why : SET A RELOAD OBJECT MARKET AREA -> event
        // LazyInitializationException: could not initialize proxy - no Session
        engineBoSession.setCurrentMarketArea(marketService.getMarketAreaById(marketArea.getId().toString()));

        // DEFAULT LOCALE IS FROM THE REQUEST OR FROM THE MARKET AREA
        String requestLocale = request.getLocale().toString();
        Localization localization = marketArea.getDefaultLocalization();
        if (marketArea.getLocalization(requestLocale) != null) {
            localization = marketArea.getLocalization(requestLocale);
        } else {
            if (requestLocale.length() > 2) {
                String localeLanguage = request.getLocale().getLanguage();
                if (marketArea.getLocalization(localeLanguage) != null) {
                    localization = marketArea.getLocalization(localeLanguage);
                }
            }
        }
        engineBoSession.setCurrentMarketLocalization(localization);

        Retailer retailer = marketArea.getDefaultRetailer();
        engineBoSession.setCurrentRetailer(retailer);

        updateCurrentBoSession(request, engineBoSession);
    }

    /**
	 * 
	 */
    protected EngineBoSession initBoSession(final HttpServletRequest request) throws Exception {
        final EngineBoSession engineBoSession = new EngineBoSession();
        EngineSetting engineSettingEnvironmentStagingModeEnabled = engineSettingService.getEnvironmentStagingModeEnabled();
        if (engineSettingEnvironmentStagingModeEnabled != null) {
            engineBoSession.setEnvironmentStagingModeEnabled(BooleanUtils.toBoolean(engineSettingEnvironmentStagingModeEnabled.getDefaultValue()));
        } else {
            engineBoSession.setEnvironmentStagingModeEnabled(false);
            logger.warn("Environment Type is not define in your database. Check the " + EngineSettingService.ENGINE_SETTING_ENVIRONMENT_STAGING_MODE_ENABLED + " value in settings table.");
        }
        EngineSetting engineSetting = engineSettingService.getEnvironmentType();
        if (engineSetting != null) {
            String environmentType = engineSetting.getDefaultValue();
            try {
                engineBoSession.setEnvironmentType(EnvironmentType.valueOf(environmentType));
            } catch (Exception e) {
                logger.error("Environment Type has wrong value define in your database. Check the " + EngineSettingService.ENGINE_SETTING_ENVIRONMENT_TYPE + " value in settings table.");
            }
        } else {
            engineBoSession.setEnvironmentType(EnvironmentType.REEL);
            logger.warn("Environment Type is not define in your database. Check the " + EngineSettingService.ENGINE_SETTING_ENVIRONMENT_TYPE + " value in settings table.");
        }

        setCurrentBoSession(request, engineBoSession);
        String jSessionId = request.getSession().getId();
        engineBoSession.setjSessionId(jSessionId);
        initDefaultBoMarketPlace(request);

        // Default Localization
        Company company = getCurrentCompany(request);
        if (company != null) {
            // USER IS LOGGED
            engineBoSession.setCurrentLocalization(company.getDefaultLocalization());
        } else {
            Localization defaultLocalization = localizationService.getLocalizationByCode("en");
            engineBoSession.setCurrentLocalization(defaultLocalization);
        }

        updateCurrentBoSession(request, engineBoSession);
        return engineBoSession;
    }

    /**
     * 
     */
    protected void initCart(final HttpServletRequest request) throws Exception {
        final EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
        Cart cart = engineEcoSession.getCart();
        if (cart == null) {
            // Init a new empty Cart with a default configuration
            cart = new Cart();
        }
        engineEcoSession.setCart(cart);
        updateCurrentEcoSession(request, engineEcoSession);
    }

    /**
     * @throws Exception
     * 
     */
    protected void resetCart(final HttpServletRequest request) throws Exception {
        // TODO : save the current cart

        // Reset Cart
        updateCurrentCart(request, new Cart());
    }

    /**
     * 
     */
    protected void initDefaultEcoMarketPlace(final HttpServletRequest request) throws Exception {
        final EngineEcoSession engineEcoSession = getCurrentEcoSession(request);
        MarketPlace marketPlace = marketPlaceService.getDefaultMarketPlace();
        engineEcoSession.setCurrentMarketPlace(marketPlace);

        Market market = marketPlace.getDefaultMarket();
        engineEcoSession.setCurrentMarket(market);

        MarketArea marketArea = market.getDefaultMarketArea();
        // TODO : why : SET A RELOAD OBJECT MARKET AREA -> event
        // LazyInitializationException: could not initialize proxy - no Session
        engineEcoSession.setCurrentMarketArea(marketService.getMarketAreaById(marketArea.getId().toString()));

        // DEFAULT LOCALE IS FROM THE REQUEST OR FROM THE MARKET AREA
        String requestLocale = request.getLocale().toString();
        Localization localization = marketArea.getDefaultLocalization();
        if (marketArea.getLocalization(requestLocale) != null) {
            localization = marketArea.getLocalization(requestLocale);
        } else {
            if (requestLocale.length() > 2) {
                String localeLanguage = request.getLocale().getLanguage();
                if (marketArea.getLocalization(localeLanguage) != null) {
                    localization = marketArea.getLocalization(localeLanguage);
                }
            }
        }
        engineEcoSession.setCurrentLocalization(localization);

        Retailer retailer = marketArea.getDefaultRetailer();
        engineEcoSession.setCurrentRetailer(retailer);

        updateCurrentEcoSession(request, engineEcoSession);
    }

    protected boolean isBackoffice() throws Exception {
        if (getContextName().contains("BO_")) {
            return true;
        }
        return false;
    }
}