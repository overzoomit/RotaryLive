package it.stasbranger.rotarylive.controller;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.ReplicationMongoDbConfigurationBuilder.replicationMongoDbConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
	@UsingDataSet(locations="UserControllerTests.json", loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void signupTEST() throws Exception {
		
		JSONObject json = new JSONObject();
		json.put("name", "Mickey Mouse");
		json.put("clubName", "Rotary Club Disney");
		json.put("login", "mickey");
		json.put("password", "mouse");
		
		mvc.perform(post("/signup")
				.content(json.toString())
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isCreated())
		.andExpect(content().contentType("application/json;charset=utf-8"));
	}
	
	@Test
	@UsingDataSet(locations="UserControllerTests.json", loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void signupConflictTEST() throws Exception {
		
		String content = "{\"name\":\"Flavio Troia\",\"clubName\":\"Rotary Club Andria\",\"login\":\"flavio\",\"password\":\"mypassword\"}";
		JSONObject json = new JSONObject(content);
		mvc.perform(post("/signup")
				.content(json.toString())
				.contentType("application/json")
				.accept(MediaType.parseMediaType("application/json"))
				)
		.andExpect(status().isConflict())
		.andExpect(content().contentType("application/json;charset=utf-8"));
	}
}
