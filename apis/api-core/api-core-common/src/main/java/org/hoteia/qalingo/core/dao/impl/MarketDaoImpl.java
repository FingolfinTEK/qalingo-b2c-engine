/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hoteia.qalingo.core.dao.MarketDao;
import org.hoteia.qalingo.core.domain.Market;
import org.hoteia.qalingo.core.domain.MarketArea;
import org.hoteia.qalingo.core.domain.MarketPlace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("marketDao")
@Transactional
public class MarketDaoImpl extends AbstractGenericDaoImpl implements MarketDao {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// MARKET PLACE
	
    public MarketPlace getDefaultMarketPlace() {
        Criteria criteria = createDefaultCriteria(MarketPlace.class);
        
        addDefaultMarketPlaceFetch(criteria);
        criteria.add(Restrictions.eq("isDefault", true));
        MarketPlace marketPlace = (MarketPlace) criteria.uniqueResult();
        return marketPlace;
    }
    
    public MarketPlace getMarketPlaceById(final Long marketPlaceId) {
        Criteria criteria = createDefaultCriteria(MarketPlace.class);
        
        addDefaultMarketPlaceFetch(criteria);

        criteria.add(Restrictions.eq("id", marketPlaceId));
        MarketPlace marketPlace = (MarketPlace) criteria.uniqueResult();
        return marketPlace;
    }
    
    public MarketPlace getMarketPlaceByCode(final String code) {
        Criteria criteria = createDefaultCriteria(MarketPlace.class);
        
        addDefaultMarketPlaceFetch(criteria);

        criteria.add(Restrictions.eq("code", code));
        MarketPlace marketPlace = (MarketPlace) criteria.uniqueResult();
        return marketPlace;
    }
    
    public List<MarketPlace> findMarketPlaces() {
        Criteria criteria = createDefaultCriteria(MarketPlace.class);

        addDefaultMarketPlaceFetch(criteria);
        
        criteria.addOrder(Order.asc("code"));

        @SuppressWarnings("unchecked")
        List<MarketPlace> marketPlaces = criteria.list();
        
        return marketPlaces;
    }

    public void saveOrUpdateMarketPlace(MarketPlace marketPlace) {
        if(marketPlace.getDateCreate() == null){
            marketPlace.setDateCreate(new Date());
        }
        marketPlace.setDateUpdate(new Date());
        em.merge(marketPlace);
    }

    public void deleteMarketPlace(MarketPlace marketPlace) {
        em.remove(marketPlace);
    }
    
	// MARKET
	
	public Market getDefaultMarket() {
        Criteria criteria = createDefaultCriteria(Market.class);
        
        addDefaultMarketFetch(criteria);
        
        criteria.add(Restrictions.eq("isDefault", true));
        Market market = (Market) criteria.uniqueResult();
		return market;
	}
	
	public Market getMarketById(final Long marketId) {
        Criteria criteria = createDefaultCriteria(Market.class);

        addDefaultMarketFetch(criteria);

        criteria.add(Restrictions.eq("id", marketId));
        Market market = (Market) criteria.uniqueResult();
        return market;
	}

	public Market getMarketByCode(final String code) {
        Criteria criteria = createDefaultCriteria(Market.class);

        addDefaultMarketFetch(criteria);

        criteria.add(Restrictions.eq("code", code));
        Market market = (Market) criteria.uniqueResult();
		return market;
	}
	
	public List<Market> findMarkets() {
        Criteria criteria = createDefaultCriteria(Market.class);
        
        addDefaultMarketFetch(criteria);

        criteria.addOrder(Order.asc("code"));

        @SuppressWarnings("unchecked")
        List<Market> markets = criteria.list();
		return markets;
	}
	
    public List<Market> getMarketsByMarketPlaceCode(final String marketPlaceCode) {
        Criteria criteria = createDefaultCriteria(Market.class);

        addDefaultMarketFetch(criteria);

        criteria.createAlias("marketPlace", "mp", JoinType.LEFT_OUTER_JOIN);
        criteria.add( Restrictions.eq("mp.code", marketPlaceCode));
        
        criteria.addOrder(Order.asc("code"));

        @SuppressWarnings("unchecked")
        List<Market> markets = criteria.list();
        return markets;
    }

	public void saveOrUpdateMarket(Market market) {
		if(market.getDateCreate() == null){
			market.setDateCreate(new Date());
		}
		market.setDateUpdate(new Date());
	    em.merge(market);
	}

	public void deleteMarket(Market market) {
		em.remove(market);
	}
	
	// MARKET AREA

	public MarketArea getMarketAreaById(final Long marketAreaId) {
        Criteria criteria = createDefaultCriteria(MarketArea.class);
        
        addDefaultMarketAreaFetch(criteria);

        criteria.add(Restrictions.eq("id", marketAreaId));
        MarketArea marketArea = (MarketArea) criteria.uniqueResult();
        return marketArea;
	}
	
	public MarketArea getMarketAreaByCode(final String code) {
        Criteria criteria = createDefaultCriteria(MarketArea.class);
        
        addDefaultMarketAreaFetch(criteria);
        
        criteria.add(Restrictions.eq("code", code));
        MarketArea marketArea = (MarketArea) criteria.uniqueResult();
		return marketArea;
	}
	
    private void addDefaultMarketPlaceFetch(Criteria criteria) {
//      ProjectionList projections = Projections.projectionList();
//      criteria.setProjection(projections);

      criteria.setFetchMode("masterCatalog", FetchMode.JOIN);
      criteria.setFetchMode("markets", FetchMode.JOIN);
      criteria.setFetchMode("marketPlaceAttributes", FetchMode.JOIN);
      
//      criteria.createAlias("markets.marketAreas", "marketAreas", JoinType.LEFT_OUTER_JOIN);
//      criteria.setFetchMode("markets.marketAreas", FetchMode.JOIN);

//      criteria.createAlias("markets.marketAreas.defaultLocalization", "defaultLocalization", JoinType.LEFT_OUTER_JOIN);
//      criteria.setFetchMode("defaultLocalization", FetchMode.JOIN);
//
//      projections.add(Projections.property("markets.marketAreas.defaultLocalization"));
//
//      criteria.createAlias("markets.marketAreas.localizations", "localizations", JoinType.LEFT_OUTER_JOIN);
//      criteria.setFetchMode("localizations", FetchMode.JOIN);
//
//      projections.add(Projections.property("markets.marketAreas.localizations"));
//
//      criteria.createAlias("markets.marketAreas.retailers", "retailers", JoinType.LEFT_OUTER_JOIN);
//      criteria.setFetchMode("retailers", FetchMode.JOIN);
//      
//      projections.add(Projections.property("markets.marketAreas.retailers"));
//      
//      criteria.createAlias("markets.marketAreas.marketAreaAttributes", "marketAreaAttributes", JoinType.LEFT_OUTER_JOIN);
//      criteria.setFetchMode("marketAreaAttributes", FetchMode.JOIN);
//      
//      projections.add(Projections.property("markets.marketAreas.marketAreaAttributes"));
      
      
  }
    
    private void addDefaultMarketFetch(Criteria criteria) {
        criteria.setFetchMode("marketPlace", FetchMode.JOIN);
        criteria.setFetchMode("marketAreas", FetchMode.JOIN);
        criteria.setFetchMode("marketAttributes", FetchMode.JOIN);
        
        criteria.createAlias("marketAreas.defaultLocalization", "defaultLocalization", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("defaultLocalization", FetchMode.JOIN);

        criteria.createAlias("marketAreas.localizations", "localizations", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("localizations", FetchMode.JOIN);
        
        criteria.createAlias("marketAreas.retailers", "retailers", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("retailers", FetchMode.JOIN);
        
        criteria.createAlias("marketAreas.marketAreaAttributes", "marketAreaAttributes", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("marketAreaAttributes", FetchMode.JOIN);
        
    }
    
    private void addDefaultMarketAreaFetch(Criteria criteria) {
        
        criteria.setFetchMode("virtualCatalog", FetchMode.JOIN);
        criteria.setFetchMode("market", FetchMode.JOIN);
        
        criteria.setFetchMode("defaultCurrency", FetchMode.JOIN);
        criteria.setFetchMode("currencies", FetchMode.JOIN);

        criteria.setFetchMode("marketAreaAttributes", FetchMode.JOIN);

        criteria.setFetchMode("defaultLocalization", FetchMode.JOIN);
        criteria.setFetchMode("localizations", FetchMode.JOIN);

        criteria.setFetchMode("defaultRetailer", FetchMode.JOIN);
        criteria.setFetchMode("retailers", FetchMode.JOIN);

        criteria.setFetchMode("deliveryMethods", FetchMode.JOIN);
        
        criteria.createAlias("deliveryMethods.countries", "deliveryMethodCountries", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("deliveryMethodCountries", FetchMode.JOIN);

        criteria.createAlias("deliveryMethods.prices", "deliveryMethodPrices", JoinType.LEFT_OUTER_JOIN);
        criteria.setFetchMode("deliveryMethodPrices", FetchMode.JOIN);

        criteria.setFetchMode("paymentGateways", FetchMode.JOIN);
        
    }
	
}