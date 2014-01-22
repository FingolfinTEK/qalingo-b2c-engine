/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.domain;

public abstract class AbstractAttribute extends AbstractEntity {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 2397084953607874647L;
	
	// TODO : ENUM OR NOT ?
    public final static String MARKET_AREA_ATTRIBUTE_EMAIL_FROM_ADDRESS = "MARKET_AREA_EMAIL_FROM_ADDRESS";
    public final static String MARKET_AREA_ATTRIBUTE_EMAIL_FROM_NAME = "MARKET_AREA_EMAIL_FROM_NAME";
    public final static String MARKET_AREA_ATTRIBUTE_EMAIL_TO_CONTACT = "MARKET_AREA_EMAIL_CONTACT";
    public final static String MARKET_AREA_ATTRIBUTE_DOMAIN_NAME = "MARKET_AREA_DOMAIN_NAME";
    public final static String MARKET_AREA_ATTRIBUTE_SHARE_OPTIONS = "MARKET_AREA_SHARE_OPTIONS";
    public final static String MARKET_AREA_ATTRIBUTE_SAVE_PAYMENT_INFORMATION = "MARKET_AREA_SAVE_PAYMENT_INFORMATION";
    public final static String MARKET_AREA_ATTRIBUTE_ORDER_CONFIRMATION_TEMPLATE = "MARKET_AREA_ORDER_CONFIRMATION_TEMPLATE";
    public final static String MARKET_AREA_ATTRIBUTE_SHIPPING_CONFIRMATION_TEMPLATE = "MARKET_AREA_SHIPPING_CONFIRMATION_TEMPLATE";
    public final static String MARKET_AREA_ATTRIBUTE_INVOICE_TEMPLATE = "MARKET_AREA_INVOICE_TEMPLATE";

	public final static String CUSTOMER_ATTRIBUTE_SCREENAME = "CUSTOMER_ATTRIBUTE_SCREENNAME";

	public final static String STORE_ATTRIBUTE_I18N_CITY = "STORE_ATTRIBUTE_I18N_CITY";
	public final static String STORE_ATTRIBUTE_ORDER = "STORE_ATTRIBUTE_ORDER";

	public final static String CATALOG_CATEGORY_ATTRIBUTE_I18N_NAME = "CATALOG_CATEGORY_ATTRIBUTE_I18N_NAME";
	public final static String CATALOG_CATEGORY_ATTRIBUTE_ORDER = "CATALOG_CATEGORY_ATTRIBUTE_ORDER";
	
	public final static String PRODUCT_MARKETING_ATTRIBUTE_I18N_NAME = "PRODUCT_MARKETING_ATTRIBUTE_I18N_NAME";
	public final static String PRODUCT_MARKETING_ATTRIBUTE_ORDER = "PRODUCT_MARKETING_ATTRIBUTE_ORDER";
    public final static String PRODUCT_MARKETING_ATTRIBUTE_FEATURED = "PRODUCT_MARKETING_ATTRIBUTE_FEATURED";
    
	public final static String PRODUCT_SKU_ATTRIBUTE_I18N_NAME = "PRODUCT_SKU_ATTRIBUTE_I18N_NAME";
	public final static String PRODUCT_SKU_ATTRIBUTE_ORDER = "PRODUCT_SKU_ATTRIBUTE_ORDER";
    public final static String PRODUCT_SKU_ATTRIBUTE_WIDTH = "PRODUCT_SKU_ATTRIBUTE_WIDTH";
    public final static String PRODUCT_SKU_ATTRIBUTE_HEIGHT = "PRODUCT_SKU_ATTRIBUTE_HEIGHT";
    public final static String PRODUCT_SKU_ATTRIBUTE_LENGTH = "PRODUCT_SKU_ATTRIBUTE_LENGTH";
    public final static String PRODUCT_SKU_ATTRIBUTE_WEIGHT = "PRODUCT_SKU_ATTRIBUTE_WEIGHT";

	public final static String RETAILER_ATTRIBUTE_I18N_NAME = "RETAILER_ATTRIBUTE_I18N_NAME";
	public final static String RETAILER_ATTRIBUTE_ORDER = "RETAILER_ATTRIBUTE_ORDER";

	public abstract AttributeDefinition getAttributeDefinition();
	
	public abstract String getStringValue();

	public abstract Integer getIntegerValue();

	public abstract Double getDoubleValue();

	public abstract Float getFloatValue();

	public abstract byte[] getBlobValue();

	public abstract Boolean getBooleanValue();

	public Object getValue() {
		AttributeDefinition attributeDefinition = getAttributeDefinition();
		if(attributeDefinition.getAttributeType() == AttributeDefinition.ATTRIBUTE_TYPE_STRING) {
			return (Object) getStringValue();
		} else if(attributeDefinition.getAttributeType() == AttributeDefinition.ATTRIBUTE_TYPE_DOUBLE) {
			return (Object) getDoubleValue();
		} else if(attributeDefinition.getAttributeType() == AttributeDefinition.ATTRIBUTE_TYPE_FLOAT) {
			return (Object) getFloatValue();
		} else if(attributeDefinition.getAttributeType() == AttributeDefinition.ATTRIBUTE_TYPE_INTEGER) {
			return (Object) getIntegerValue();
		} else if(attributeDefinition.getAttributeType() == AttributeDefinition.ATTRIBUTE_TYPE_BLOB) {
			return (Object) getBlobValue();
		} else if(attributeDefinition.getAttributeType() == AttributeDefinition.ATTRIBUTE_TYPE_BOOLEAN) {
			return (Object) getBooleanValue();
		}
		return null;
	}
	
	public String getValueAsString() {
		if(getAttributeDefinition().getAttributeType() == AttributeDefinition.ATTRIBUTE_TYPE_STRING){
			if(getStringValue() != null){
				return getStringValue();
			}
		} else if(getAttributeDefinition().getAttributeType() == AttributeDefinition.ATTRIBUTE_TYPE_DOUBLE){
			if(getDoubleValue() != null){
				return getDoubleValue().toString();
			}
		} else if(getAttributeDefinition().getAttributeType() == AttributeDefinition.ATTRIBUTE_TYPE_FLOAT){
			if(getFloatValue() != null){
				return getFloatValue().toString();
			}
		} else if(getAttributeDefinition().getAttributeType() == AttributeDefinition.ATTRIBUTE_TYPE_INTEGER){
			if(getIntegerValue() != null){
				return getIntegerValue().toString();
			}
		} else if(getAttributeDefinition().getAttributeType() == AttributeDefinition.ATTRIBUTE_TYPE_BLOB){
			if(getBlobValue() != null){
				return getBlobValue().toString();
			}
		} else if(getAttributeDefinition().getAttributeType() == AttributeDefinition.ATTRIBUTE_TYPE_BOOLEAN){
			if(getBooleanValue() != null){
				return getBooleanValue().toString();
			}
		}
		return null;
	}
	
}