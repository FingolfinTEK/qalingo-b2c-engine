package org.hoteia.qalingo.core.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.hoteia.qalingo.core.domain.Store;
import org.hoteia.qalingo.core.solr.response.StoreResponseBean;
import org.hoteia.qalingo.core.solr.service.StoreSolrService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * StoreSolrServiceTest Test
 */
@RunWith(SpringJUnit4ClassRunner.class)     
@ContextConfiguration({ "/conf/spring/qalingo-core-solr-test.xml" })
public class StoreSolrServiceTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    protected StoreSolrService  storeSolrService;

    protected Store store;
    
    protected StoreResponseBean responseBean;

	/**
	 * Test Case to check: if required field is blank of null (i.e. id here)
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testIndexDataWithBlankID() throws SolrServerException, IOException {
        store = new Store();
        // store.setId(Long.parseLong("31"));
        store.setBusinessName("development");
        store.setAreaCode("Area-21");
        store.setCity("pune");
        store.setCountryCode("IND");
        store.setPostalCode("411014");
        store.setType("retailer");
        storeSolrService.addOrUpdateStore(store);
    }

	/**
	 * Test Case to check for all given fields have  indexed
	 */
    @Test
    public void testIndexData() throws SolrServerException, IOException {
        logger.debug("--------------->testIndexDataSecond()");
        store = new Store();
        store.setId(Long.parseLong("31"));
        store.setBusinessName("development");
        store.setAreaCode("Area-21");
        store.setCity("pune");
        store.setCountryCode("IND");
        store.setPostalCode("411014");
        store.setType("retailer");
        storeSolrService.addOrUpdateStore(store);

    }

	/**
	 * Test case to check: search by given field Id
	 */
    @Test
    public void testSearchId() throws SolrServerException, IOException {
        logger.debug("--------------->Search: Id");
        responseBean = storeSolrService.searchStore("id", "", "");
        printData();
    }

	/**
	 * Test case to check: search by given field city
	 */
    @Test
    public void testSearchCity() throws SolrServerException, IOException {
        logger.debug("--------------->search: City");
        responseBean = storeSolrService.searchStore("city", "", "");
        printData();
    }

	/**
	 * Test case to check: search by given field country
	 */
    @Test
    public void testSearchCountry() throws SolrServerException, IOException {
        logger.debug("--------------->search: Country");
        responseBean = storeSolrService.searchStore("countrycode", "", "");
        printData();
    }

	/**
	 * Test case to check: search by given field country with some startWith text parameter 
	 */
    @Test
    public void testSearchCountryWithText() throws SolrServerException, IOException {
        logger.debug("--------------->search: Country with text");
        responseBean = storeSolrService.searchStore("countrycode", "IN", "");
        printData();
    }

	/**
	 * Test case to check: search by given field country with facet
	 */
    @Test
    public void testSearchCountryWithFacet() throws SolrServerException, IOException {
        logger.debug("--------------->search: Country with facet");
        responseBean = storeSolrService.searchStore("countrycode", "", "countrycode");
        printData();
    }

	/**
	 * Test case to check: search by given field  Illegal Argument which is not mansion in schema
	 */ 
    @Test(expected = org.apache.solr.common.SolrException.class)
    public void testSearch() throws SolrServerException, IOException {
        logger.debug("--------------->Search unknown field");
        responseBean = storeSolrService.searchStore("xyz", "123", "abc");
        printData();
    }

	/**
	 *  Test case to check: search by: empty String
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptySearch() throws SolrServerException, IOException {
        logger.debug("--------------->Empty Search ");
        responseBean = storeSolrService.searchStore("", "", "");
        printData();
    }
    
	/**
	 * Test case to check: search by default argument i.e.code
	 */
    @Test
    public void testDefaultSearch() throws SolrServerException, IOException {
        logger.debug("--------------->Default Search ");
        responseBean = storeSolrService.searchStore();
        printData();
    }

    public void printData() {
        if (responseBean != null) {
            logger.debug("---Facets---");
            for (int i = 0; i < responseBean.getStoreSolrFacetFieldList().size(); i++) {
                logger.debug("" + responseBean.getStoreSolrFacetFieldList().get(i));
            }
            logger.debug("---STORE LIST---");
            for (int i = 0; i < responseBean.getStoreSolrList().size(); i++) {
                logger.debug(responseBean.getStoreSolrList().get(i).getType());
                logger.debug(responseBean.getStoreSolrList().get(i).getBusinessname());
                logger.debug(responseBean.getStoreSolrList().get(i).getCity());
                logger.debug(responseBean.getStoreSolrList().get(i).getCountryCode());
                logger.debug(responseBean.getStoreSolrList().get(i).getPostalCode());
            }
        }
    }

}