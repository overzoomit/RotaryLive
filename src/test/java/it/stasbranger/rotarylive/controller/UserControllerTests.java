package it.stasbranger.rotarylive.controller;

import static com.lordofthejars.nosqlunit.mongodb.InMemoryMongoDb.InMemoryMongoRuleBuilder.newInMemoryMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.InMemoryMongoDb;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.MongoFlexibleComparisonStrategy;

import it.stasbranger.rotarylive.RotaryLiveApplicationTests;
import it.stasbranger.rotarylive.domain.User;
import it.stasbranger.rotarylive.service.UserService;

@Transactional
@CustomComparisonStrategy(comparisonStrategy = MongoFlexibleComparisonStrategy.class)
public class UserControllerTests extends RotaryLiveApplicationTests {

	private MockMvc mvc;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private UserService userService;
	
	@ClassRule
    public static final InMemoryMongoDb IN_MEMORY_MONGO_DB = newInMemoryMongoDbRule().build();

	@Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultEmbeddedMongoDb("test");


	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.alwaysDo(print()).build();
	}

	@Test
	@UsingDataSet(locations={"AttachControllerTests.json", "ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void showUsersTEST() throws Exception {
		String result = mvc.perform(get("/api/user").contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
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
	@UsingDataSet(locations={"AttachControllerTests.json", "ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void showUsersAdminTEST2() throws Exception {
		UsernamePasswordAuthenticationToken principal = this.getPrincipal("flavio");
		
		String result = mvc.perform(get("/api/user").contentType("application/json")
				.accept(MediaType.APPLICATION_JSON).principal(principal))
				.andExpect(status().isOk())
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
	@UsingDataSet(locations={"ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void showMeTEST() throws Exception {
	
		UsernamePasswordAuthenticationToken principal = this.getPrincipal("flavio");

		String result = mvc.perform(get("/api/user/me").contentType("application/json")
				.accept(MediaType.APPLICATION_JSON).principal(principal))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		assertTrue(json.get("username").equals(principal.getName()));
	}
	
	@Test
	@UsingDataSet(locations={"AttachControllerTests.json", "ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.DELETE_ALL)
	public void showUsersFilterTEST() throws Exception {
		String result = mvc.perform(get("/api/user")
				.param("q", "troia")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("userList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.getString("name").equals("Flavio Troia") || j.getString("name").equals("Silvio Troia"));
		}
	}
	
	@Test
	@UsingDataSet(locations={"AttachControllerTests.json", "ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.DELETE_ALL)
	public void showUsersFilterTEST3() throws Exception {
		String result = mvc.perform(get("/api/user")
				.param("q", "tro")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("userList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.getString("name").equals("Flavio Troia") || j.getString("name").equals("Silvio Troia"));
		}
	}
	
	@Test
	@UsingDataSet(locations={"AttachControllerTests.json", "ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.DELETE_ALL)
	public void showUsersFilterTEST2() throws Exception {
		String result = mvc.perform(get("/api/user")
				.param("q", "34733423322")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("_embedded.userList",hasSize(is(1))))
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("userList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.getString("name").equals("Flavio Troia"));
		}
	}
	
	@Test
	@UsingDataSet(locations={"AttachControllerTests.json", "ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.DELETE_ALL)
	public void showUsersFilterTEST4() throws Exception {
		String result = mvc.perform(get("/api/user")
				.param("q", "Ingegnere")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("_embedded.userList",hasSize(is(1))))
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("userList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.getString("name").equals("Flavio Troia"));
		}
	}
	
	@Test
	@UsingDataSet(locations={"AttachControllerTests.json", "ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.DELETE_ALL)
	public void showUsersFilterTEST5() throws Exception {
		String result = mvc.perform(get("/api/user")
				.param("q", "Club Andria")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("_embedded.userList",hasSize(is(1))))
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("userList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.getString("name").equals("Flavio Troia"));
		}
	}
	
	@Test
	@UsingDataSet(locations={"RoleControllerTests.json", "ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void crateUserTEST() throws Exception {
		
		String content = "{\"name\":\"Lolli Pop\", \"club\": {\"id\":\"56fab17ee4b074b1e6b6ca80\", \"name\":\"Rotary Club Andria Castelli Svevi\", \"address\":{\"ref\": \"Hotel L'Ottagono\", \"street\": \"via Barletta 138\", \"zipCode\":\"76123\", \"city\":\"Andria\", \"province\":\"BT\", \"country\":\"Italy\"}, \"logoId\":\"56fab17ee4b074b1e6b6cb79\", \"version\":\"0\"}, \"username\":\"lollipop\",\"password\":\"test\", \"member\":{\"firstName\" : \"Lolli\", \"lastName\" : \"Pop\", \"email\":\"lollipop@gmail.com\"} }";
		JSONObject json = new JSONObject(content);
		
		mvc.perform(post("/api/user")
				.content(json.toString())
				.contentType("application/json")
				.accept(MediaType.parseMediaType("application/json"))
				)
		.andExpect(status().isCreated());
		
		User user = userService.findByUsername("lollipop");
		assertTrue(user.getMember().getEmail().equals("lollipop@gmail.com"));
	}
	
	@Test
	@UsingDataSet(locations={"AttachControllerTests.json", "ClubControllerTests.json", "UserControllerTests.json"}, loadStrategy=LoadStrategyEnum.DELETE_ALL)
	public void showUsersFilterTEST6() throws Exception {
		String result = mvc.perform(get("/api/user")
				.param("q", "3473342")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("_embedded.userList",hasSize(is(1))))
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("userList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.getString("name").equals("Flavio Troia"));
		}
	}
}
