package org.hoteia.qalingo.core.pojo.product;

import org.hoteia.qalingo.core.pojo.CurrencyReferentialPojo;

import java.math.BigDecimal;

public class ProductSkuPricePojo {

    private Long id;
    private int version;
    private BigDecimal price;
    private CurrencyReferentialPojo currency;
    private Long marketAreaId;
    private Long retailerId;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CurrencyReferentialPojo getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyReferentialPojo currency) {
        this.currency = currency;
    }

    public Long getMarketAreaId() {
        return marketAreaId;
    }

    public void setMarketAreaId(Long marketAreaId) {
        this.marketAreaId = marketAreaId;
    }

    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }
}
