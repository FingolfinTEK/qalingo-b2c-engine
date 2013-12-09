/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.solr.response;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.response.FacetField;
import org.hoteia.qalingo.core.solr.bean.ProductMarketingSolr;

public class ProductMarketingResponseBean {

    private List<ProductMarketingSolr> productMarketingSolrList = new ArrayList<ProductMarketingSolr>();

    private List<FacetField> productMarketingSolrFacetFieldList = new ArrayList<FacetField>();

    public List<ProductMarketingSolr> getProductMarketingSolrList() {
        return productMarketingSolrList;
    }

    public void setProductMarketingSolrList(List<ProductMarketingSolr> productMarketingSolrList) {
        this.productMarketingSolrList = productMarketingSolrList;
    }

    public List<FacetField> getProductMarketingSolrFacetFieldList() {
        return productMarketingSolrFacetFieldList;
    }

    public void setProductMarketingSolrFacetFieldList(List<FacetField> productMarketingSolrFacetFieldList) {
        this.productMarketingSolrFacetFieldList = productMarketingSolrFacetFieldList;
    }

}