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
import java.util.List;

public class OrderViewBean extends AbstractViewBean implements Serializable {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -7481342045685333815L;
	
    private Long id;
    private int version;
    private String status;
    private String orderNum;

    private String orderItemsTotal;
    private String orderShippingTotal;
    private String orderFeesTotal;
    private String orderTotal;

    private String cardHolder;
    private String cardNumber;
    private String cardCrypto;
    private String cardExpirationDate;
    private String cardExpirationMonth;
    private String cardExpirationYear;

    private Long customerId;
    private Long billingAddressId;
    private Long shippingAddressId;

    private List<OrderItemViewBean> orderItems = new ArrayList<OrderItemViewBean>();
    private List<OrderShippingViewBean> orderShippings = new ArrayList<OrderShippingViewBean>();
    private List<OrderTaxViewBean> orderTaxes = new ArrayList<OrderTaxViewBean>();

    private String detailsUrl;

    private String dateCreate;
    private String dateUpdate;
	    
	public OrderViewBean() {
	}
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
	    
	public String getOrderNum() {
		return orderNum;
	}
	
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getOrderItemsTotal() {
		return orderItemsTotal;
	}

	public void setOrderItemsTotal(String orderItemsTotal) {
		this.orderItemsTotal = orderItemsTotal;
	}

	public String getOrderShippingTotal() {
		return orderShippingTotal;
	}

	public void setOrderShippingTotal(String orderShippingTotal) {
		this.orderShippingTotal = orderShippingTotal;
	}

	public String getOrderFeesTotal() {
		return orderFeesTotal;
	}

	public void setOrderFeesTotal(String orderFeesTotal) {
		this.orderFeesTotal = orderFeesTotal;
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardCrypto() {
		return cardCrypto;
	}

	public void setCardCrypto(String cardCrypto) {
		this.cardCrypto = cardCrypto;
	}

	public String getCardExpirationDate() {
		return cardExpirationDate;
	}

	public void setCardExpirationDate(String cardExpirationDate) {
		this.cardExpirationDate = cardExpirationDate;
	}

	public String getCardExpirationMonth() {
		return cardExpirationMonth;
	}

	public void setCardExpirationMonth(String cardExpirationMonth) {
		this.cardExpirationMonth = cardExpirationMonth;
	}

	public String getCardExpirationYear() {
		return cardExpirationYear;
	}

	public void setCardExpirationYear(String cardExpirationYear) {
		this.cardExpirationYear = cardExpirationYear;
	}

	public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(Long billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public Long getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(Long shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public List<OrderItemViewBean> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemViewBean> orderItems) {
		this.orderItems = orderItems;
	}

	public List<OrderShippingViewBean> getOrderShippings() {
		return orderShippings;
	}

	public void setOrderShippings(List<OrderShippingViewBean> orderShippings) {
		this.orderShippings = orderShippings;
	}

    public List<OrderTaxViewBean> getOrderTaxes() {
        return orderTaxes;
    }

    public void setOrderTaxes(List<OrderTaxViewBean> orderTaxes) {
        this.orderTaxes = orderTaxes;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }
    
    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
}