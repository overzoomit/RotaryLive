package it.stasbranger.rotarylive.controller;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.ReplicationMongoDbConfigurationBuilder.replicationMongoDbConfiguration;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;

import it.stasbranger.rotarylive.RotaryLiveApplicationTests;

@Transactional
public class UserControllerTests extends RotaryLiveApplicationTests {

	private MockMvc mvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

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
				.alwaysDo(print()).build();
	}

	@Test
	@UsingDataSet(locations="UserControllerTests.json", loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void showUsersTEST() throws Exception {
		String result = mvc.perform(get("/api/user").contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("userList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.get("username")!=null);
		}
	}
	
	@Test
	@UsingDataSet(locations="UserControllerTests.json", loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void showMeTEST() throws Exception {
	
		UsernamePasswordAuthenticationToken principal = this.getPrincipal("flavio");

		String result = mvc.perform(get("/api/user/me").contentType("application/json")
				.accept(MediaType.APPLICATION_JSON).principal(principal))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		assertTrue(json.get("username").equals(principal.getName()));
	}
}
