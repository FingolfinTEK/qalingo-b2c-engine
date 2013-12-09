package org.hoteia.qalingo.core.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.hoteia.qalingo.core.domain.ProductMarketing;
import org.hoteia.qalingo.core.solr.response.ProductMarketingResponseBean;
import org.hoteia.qalingo.core.solr.service.ProductMarketingSolrService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ProductMarketingSolrServiceTest Test
 */
@RunWith(SpringJUnit4ClassRunner.class) 	
@ContextConfiguration({ "/conf/spring/qalingo-core-solr-test.xml" })
public class ProductMarketingSolrServiceTest {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
	@Autowired
	protected ProductMarketingSolrService productMarketingSolrService; 

	protected ProductMarketing productMarketing;

	protected ProductMarketingResponseBean responseBean;

    /**
     * Test Case to check: if required field is blank of null (i.e. id here)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIndexDataWithBlankID() throws SolrServerException, IOException {
        productMarketing = new ProductMarketing();
        productMarketing.setBusinessName("Product Marketing");
        productMarketing.setDescription("Product Marketing ...");
        productMarketing.setCode("productMarketing");
        productMarketingSolrService.addOrUpdateProductMarketing(productMarketing);
        logger.debug("--------------->testFirstIndexData()");
    }
    
	/**
	 * Test Case to check for all given fields have  indexed
	 */
    @Test
    public void testIndexData() throws SolrServerException, IOException {
        logger.debug("--------------->testIndexData");
        productMarketing = new ProductMarketing();
        productMarketing.setId(Long.parseLong("91"));
        productMarketing.setBusinessName("Product Marketing");
        productMarketing.setDescription("Product Marketing ...");
        productMarketing.setCode("productMarketing");
        productMarketingSolrService.addOrUpdateProductMarketing(productMarketing);
    }
    
	/**
	 * Test case to check: search by given field Id
	 */
    @Test
    public void testSearchId() throws SolrServerException, IOException {
        logger.debug("--------------->Search: Id");
        responseBean = productMarketingSolrService.searchProductMarketing("id", "", "");
        printData();
    }
    
	/**
	 *  Test case to check: search by given field code with given text
	 */
    @Test
    public void testSearchIdWithText() throws SolrServerException, IOException {
        logger.debug("--------------->search: code with some text");
        responseBean = productMarketingSolrService.searchProductMarketing("code", "N", "");
        printData();
    }
    
	/**
	 * Test case to check: search by given field id with given facet
	 */
    @Test
    public void testSearchIdWithFacet() throws SolrServerException, IOException {
        logger.debug("--------------->search: code with facet");
        responseBean = productMarketingSolrService.searchProductMarketing("code", "", "code");
        printData();
    }
    
	/**
	 * Test case to check: search by given field  Illegal Argument which is not mansion in schema
	 */
    @Test(expected = org.apache.solr.common.SolrException.class)
    public void testSearch() throws SolrServerException, IOException {
        logger.debug("--------------->Search unknown field");
        responseBean = productMarketingSolrService.searchProductMarketing("abc", "91", "xyz");
        printData();
    }
    
	/**
	 * Test case to check: search by: empty String
	 */ 
    @Test(expected = IllegalArgumentException.class)
    public void testEmptySearch() throws SolrServerException, IOException {
        logger.debug("--------------->Empty Search ");
        responseBean = productMarketingSolrService.searchProductMarketing("", "", "");
        printData();
    }
    
	/**
	 * Test case to check: search by default argument i.e.code
	 * 
	 */
    @Test
    public void testDefaultSearch() throws SolrServerException, IOException {
        logger.debug("--------------->Default Search ");
        responseBean = productMarketingSolrService.searchProductMarketing();
        printData();
    }

    public void printData() {
        if (responseBean != null) {
            logger.debug("---Facets---");
            for (int i = 0; i < responseBean.getProductMarketingSolrFacetFieldList().size(); i++) {
                logger.debug("" + responseBean.getProductMarketingSolrFacetFieldList().get(i));
            }
            logger.debug("---PRODUCT LIST---");
            for (int i = 0; i < responseBean.getProductMarketingSolrList().size(); i++) {
                logger.debug("" + responseBean.getProductMarketingSolrList().get(i).getId());
                logger.debug(responseBean.getProductMarketingSolrList().get(i).getBusinessname());
                logger.debug(responseBean.getProductMarketingSolrList().get(i).getDescription());
                logger.debug(responseBean.getProductMarketingSolrList().get(i).getCode());
            }
        }
    }

}