package com.solrSearch.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;

import com.solrSearch.util.SOLRSearch;

public class SOLRService implements SOLRSearch {
	
	private static final Logger logger = Logger.getLogger(SOLRService.class);
	@Autowired
	private SolrTemplate solrTemplate;
	
	
	public List<String> getResults(String searchCriteria, List<String>displayFields){
		
		List<String> results = new ArrayList<>(); 
		
		//set filter criteria for the query,
		Criteria criteriaQuery = new Criteria("*:*");
		criteriaQuery = criteriaQuery.and(new Criteria(searchCriteria));
		
		Query queryStr = new SimpleQuery(criteriaQuery);
		QueryResponse response = null;
		try{
			@SuppressWarnings("deprecation")
			SolrServer solrServer = solrTemplate.getSolrServer();
			SolrQuery query = new SolrQuery();
			query.setQuery(queryStr.toString());
			query.setFacet(true);
			for(String field : displayFields){
				query.addFacetField(field);
			}
			
			//get result as json
			query.set("wt","json");
			
			//get solr response
			response = solrServer.query(query);
		} catch (SolrServerException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		
		if(response != null){
			List<SolrDocument> sd = response.getResults();
			
			ObjectMapper mapper = new ObjectMapper();
			for(SolrDocument solrDoc: sd){
				try {
					//convert it into JSON and store it into a list
					results.add(mapper.writeValueAsString(solrDoc));
				} catch (JsonGenerationException map) {
					logger.error(map);
				} catch (JsonMappingException map) {
					logger.error(map);
				} catch (IOException map) {
					logger.error(map);
				}
				
			}
			
			
		}
		return results;
	}

}