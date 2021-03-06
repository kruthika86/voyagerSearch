package com.solrSearch.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solrSearch.service.SOLRService;
import com.solrSearch.util.MyJSONComparator;

@Controller
@RequestMapping("/voyager")
public class VoyagerSearchController {
	
	@Autowired
	SOLRService solrService;
	
	private static final boolean Ascending = true;
	
	final GsonBuilder gsonBuilder = new GsonBuilder();

	final Gson gson = gsonBuilder.disableHtmlEscaping().create();

	private int pageSize;

	private int pageNo;
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<String> getsearchResults(@PathVariable String searchCriteria ,@RequestBody List<String> displayFields){
		
		List<String> searchResult = solrService.getResults(searchCriteria, displayFields);
		List<String> clonedResult = searchResult;
		
		sortList(clonedResult, Ascending);
		
		List<String> resultList = cropList(clonedResult, pageNo, pageSize);
		return resultList;
		
		
	}

	//For chunking/limiting (paging)
	private List<String> cropList(List<String> clonedResult, int pageNo, int pageSize) {
		List<String> list = clonedResult;
		List<String> croppedList = new ArrayList<String>(); 
		if(list != null){
			int startIndex = pageNo == 0 ?pageNo : pageNo * pageSize;
			int endIndex = startIndex + pageSize;
			
			endIndex = endIndex > list.size() ? list.size() : endIndex;
			
			croppedList = list.subList(startIndex, endIndex);
			
		}
		return croppedList;
		
	}

	//For sorting
	private void sortList(List<String> clonedResults, boolean order) {
		List<String> values = new ArrayList<String>();
		
		for (String result : clonedResults){
		    
		    values.add(gson.toJson(result));
		}
		
		Collections.sort(values,new MyJSONComparator());		
	}

}
