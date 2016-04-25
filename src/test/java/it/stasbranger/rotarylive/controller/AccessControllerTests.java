package it.stasbranger.rotarylive.controller;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.ReplicationMongoDbConfigurationBuilder.replicationMongoDbConfiguration;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONArray;
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
	public void forgotPasswordTEST() throws Exception {
		
		mvc.perform(post("/forgot_password")
				.param("username", "flavio")
				.contentType("application/x-www-form-urlencoded; charset=UTF-8")
				.accept(MediaType.parseMediaType("application/json"))
				)
		.andExpect(status().isOk());
	}
	
	@Test
	@UsingDataSet(locations={"RoleControllerTests.json", "ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void resetPasswordTEST() throws Exception {
		
		mvc.perform(post("/reset_password")
				.param("password", "troia")
				.param("code", "NTZmYWIxN2VlNGIwNzRiMWU2YjZjYjAx_4BE78B10ABDC8FBF5FA656D55ADC48AB")
				.contentType("application/x-www-form-urlencoded; charset=UTF-8")
				.accept(MediaType.parseMediaType("application/json"))
				)
		.andExpect(status().isOk());
	}
	
	@Test
	@UsingDataSet(locations={"RoleControllerTests.json", "ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void signupTEST() throws Exception {
		
		String content = "{\"name\":\"Flavio Troia\", \"club\": {\"id\":\"56fab17ee4b074b1e6b6ca80\", \"name\":\"Rotary Club Andria Castelli Svevi\", \"address\":{\"ref\":\"Hotel L'Ottagono\", \"street\" : \"via Barletta 138\", \"zipCode\":\"76123\", \"city\":\"Andria\", \"province\":\"BT\", \"country\":\"Italy\"}, \"logoId\":\"56fab17ee4b074b1e6b6cb79\", \"version\":\"0\"}, \"username\":\"test\",\"password\":\"test\"}";
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
		
		String content = "{\"name\":\"Flavio Troia\", \"club\":{\"id\":\"56fab17ee4b074b1e6b6ca80\", \"name\":\"Rotary Club Andria Castelli Svevi\", \"address\":{\"ref\": \"Hotel L'Ottagono\", \"street\": \"via Barletta 138\", \"zipCode\":\"76123\", \"city\":\"Andria\", \"province\":\"BT\", \"country\":\"Italy\"}, \"logoId\":\"56fab17ee4b074b1e6b6cb79\", \"version\":\"0\"}, \"username\":\"flavio\",\"password\":\"mypassword\"}";
		JSONObject json = new JSONObject(content); 
		mvc.perform(post("/signup")
				.content(json.toString())
				.contentType("application/json")
				.accept(MediaType.parseMediaType("application/json"))
				)
		.andExpect(status().isConflict());
	}
	
	@Test
	@UsingDataSet(locations={"ClubControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void showClubsTEST() throws Exception {
		String result = mvc.perform(get("/club")
				.param("q", "cas")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("clubList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.get("name").toString().contains("Castelli Svevi"));
		}
	}
	
	@Test
	@UsingDataSet(locations={"AttachControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void showFileTEST() throws Exception {
		mvc.perform(get("/attach/image/5707859be4b0820ad86399f4").contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	@UsingDataSet(locations={"AttachControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void showFileThumbnailTEST() throws Exception {
		mvc.perform(get("/attach/thumbnail/5707859be4b0820ad86399f4").contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
