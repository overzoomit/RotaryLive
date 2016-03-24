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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;

import it.stasbranger.rotarylive.RotaryLiveApplicationTests;
import it.stasbranger.rotarylive.domain.Role;
import it.stasbranger.rotarylive.domain.User;
import it.stasbranger.rotarylive.service.RoleService;
import it.stasbranger.rotarylive.service.UserService;

@Transactional
public class UserControllerTests extends RotaryLiveApplicationTests {

	private MockMvc mvc;

	@Autowired private UserService userService;
	
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
		String result = mvc.perform(get("/api/user").accept(MediaType.parseMediaType("application/json")))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("userList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.get("login")!=null);
		}
	}
	
	@Test
	@UsingDataSet(locations={"RoleControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void createUsersTEST() throws Exception {
		User user = new User();
		user.setClubName("Castelli Svevi");
		user.setLogin("Ciao Silvio");
		user.setName("Silvio Ciaoaoaoaoao");
		user.setPassword("cicciobello");
		
		userService.create(user);
	}
}
