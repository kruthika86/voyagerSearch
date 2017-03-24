package com.solrSearch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.solrSearch.controller.VoyagerSearchController;
import com.solrSearch.service.SOLRService;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@EnableConfigurationProperties
@WebAppConfiguration
@SpringBootTest
public class VoyagerSolrSearchApplicationTests {

	@Mock
    private SOLRService solrService;
	
	@InjectMocks
	private VoyagerSearchController voyagerSearchController;
	
	private MockMvc mockMvc;

	private String searchCriteria;
	
	private List<String> value = new ArrayList<>();
	
	@Before
    public void setup() {
 
        // Process mock annotations
        MockitoAnnotations.initMocks(this);
 
        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders.standaloneSetup(voyagerSearchController).build();
 
        searchCriteria = "";
        	
       
    }
	@Test
	public void testGetResults() {
		
		when(solrService.getResults(searchCriteria, null)).thenReturn(value);
	}

}
