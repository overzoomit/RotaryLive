package it.stasbranger.rotarylive.controller;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.ReplicationMongoDbConfigurationBuilder.replicationMongoDbConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;

import it.stasbranger.rotarylive.RotaryLiveApplicationTests;

@Transactional
public class AccessControllerTests extends RotaryLiveApplicationTests {

	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Rule
	public MongoDbRule mongoDbRule = newMongoDbRule().configure(
            replicationMongoDbConfiguration().databaseName("rotarytest")
                             .enableSharding()
                             .seed("localhost", 27017)
                             .configure())
                        .build(); 
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.alwaysDo(print()).build();
	}
	
	@Test
	@UsingDataSet(locations={"RoleControllerTests.json", "ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void signupTEST() throws Exception {
		
		String content = "{\"name\":\"Flavio Troia\", \"club\": {\"id\":\"56fab17ee4b074b1e6b6ca80\", \"name\":\"Rotary Club Andria Castelli Svevi\", \"address\":\"Hotel L'Ottagono - via Barletta 138\", \"zipCode\":\"76123\", \"city\":\"Andria\", \"province\":\"BT\", \"country\":\"Italy\", \"logoId\":\"56fab17ee4b074b1e6b6cb79\", \"version\":\"0\"}, \"username\":\"test\",\"password\":\"test\"}";
		JSONObject json = new JSONObject(content);
		
		mvc.perform(post("/signup")
				.content(json.toString())
				.contentType("application/json")
				.accept(MediaType.parseMediaType("application/json"))
				)
		.andExpect(status().isCreated());
	}
	
	@Test
	@UsingDataSet(locations={ "UserControllerTests.json", "ClubControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void signupConflictTEST() throws Exception {
		
		String content = "{\"name\":\"Flavio Troia\", \"club\":{\"id\":\"56fab17ee4b074b1e6b6ca80\", \"name\":\"Rotary Club Andria Castelli Svevi\", \"address\":\"Hotel L'Ottagono - via Barletta 138\", \"zipCode\":\"76123\", \"city\":\"Andria\", \"province\":\"BT\", \"country\":\"Italy\", \"logoId\":\"56fab17ee4b074b1e6b6cb79\", \"version\":\"0\"}, \"username\":\"flavio\",\"password\":\"mypassword\"}";
		JSONObject json = new JSONObject(content); 
		mvc.perform(post("/signup")
				.content(json.toString())
				.contentType("application/json")
				.accept(MediaType.parseMediaType("application/json"))
				)
		.andExpect(status().isConflict());
	}
}
