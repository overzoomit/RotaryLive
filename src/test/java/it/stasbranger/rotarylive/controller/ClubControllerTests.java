package it.stasbranger.rotarylive.controller;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.ReplicationMongoDbConfigurationBuilder.replicationMongoDbConfiguration;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;

import it.stasbranger.rotarylive.RotaryLiveApplicationTests;
import it.stasbranger.rotarylive.domain.Club;
import it.stasbranger.rotarylive.service.ClubService;

@Transactional
public class ClubControllerTests  extends RotaryLiveApplicationTests {

	private MockMvc mvc;

	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private ClubService clubService;

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
	@UsingDataSet(locations={"ClubControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void showClubsTEST() throws Exception {
		String result = mvc.perform(get("/api/club")
				.param("q", "Cas")
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
	@UsingDataSet(locations={"ClubControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void showClubsFAILTEST() throws Exception {
		String result = mvc.perform(get("/api/club")
				.param("q", "XXXX")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		JSONObject page = json.getJSONObject("page");
		assertTrue((int) page.get("totalElements") == 0);
	}

	@Test
	@UsingDataSet(locations={"ClubControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void createClubTEST() throws Exception {

		String content = "{\"name\" : \"Rotary Club Trani\", \"address\" : {\"ref\":\"Il Melograno\", \"street\": \"Via Giovanni Bovio, 189\", \"zipCode\" : \"76125\", \"city\" : \"Trani\", \"province\": \"BT\", \"country\" : \"Italy\"}, \"email\" : \"info@rotarytrani.it\", \"phone\" : \"0883588010\", \"website\" : \"http://www.rotarytrani.it\"}";
		JSONObject json = new JSONObject(content);

		mvc.perform(post("/api/club")
				.content(json.toString())
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());
	}

	@Test
	@UsingDataSet(locations={"ClubControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void createClubFAILTEST() throws Exception {

		String content = "{\"name\" : \"Rotary Club Andria Castelli Svevi\", \"address\" : {\"ref\":\"Il Melograno\", \"street\": \"Via Giovanni Bovio, 189\", \"zipCode\" : \"76125\", \"city\" : \"Trani\", \"province\": \"BT\", \"country\" : \"Italy\"}, \"email\" : \"info@rotarytrani.it\", \"phone\" : \"0883588010\", \"website\" : \"http://www.rotarytrani.it\"}";
		JSONObject json = new JSONObject(content);

		mvc.perform(post("/api/club")
				.content(json.toString())
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isConflict());
	}

	@Test
	@UsingDataSet(locations={"ClubControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void deleteClubTEST() throws Exception {
		
		mvc.perform(delete("/api/club/56fab17ee4b074b1e6b6ca80")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		
		Club club = clubService.findOne(new ObjectId("56fab17ee4b074b1e6b6ca80"));
		assertNull(club);
	}
	
	@Test
	@UsingDataSet(locations={"ClubControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void getClubTEST() throws Exception {
		
		String result = mvc.perform(get("/api/club/56fab17ee4b074b1e6b6ca80")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		assertTrue(json.getString("name").equals("Rotary Club Andria Castelli Svevi"));
	}

	@Test
	@UsingDataSet(locations={"ClubControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void updateClubTEST() throws Exception {

		String content = "{\"id\" : \"56fab17ee4b074b1e6b6ca80\", \"name\" : \"Rotary Club Andria Castelli Svevi\", \"address\" : {\"ref\":\"Hotel L'Ottagono\", \"street\": \"via Barletta 138\", \"zipCode\" : \"76123\", \"city\" : \"Andria\", \"province\" : \"BT\", \"country\" : \"Italy\"}, \"website\" : \"http://www.rotaryandria.it\", \"email\" : \"info@rotaryandria.it\", \"version\": \"0\"}";
		JSONObject json = new JSONObject(content);

		String result = mvc.perform(put("/api/club/56fab17ee4b074b1e6b6ca80")
				.content(json.toString())
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		json = new JSONObject(result);
		assertTrue(json.getString("version").equals("1"));
		assertTrue(json.getString("email").equals("info@rotaryandria.it"));
		assertTrue(json.getString("website").equals("http://www.rotaryandria.it"));
	}
	
	@Test
	@UsingDataSet(locations={"AttachControllerTests.json", "UserControllerTests.json", "ClubControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void updateWithImageTEST() throws Exception {
		FileInputStream fis = new FileInputStream("src/main/resources/static/logo.jpg");
		MockMultipartFile data = new MockMultipartFile("file","logo.jpg", "image/jpeg", fis);

		UsernamePasswordAuthenticationToken principal = this.getPrincipal("flavio");

		String result = mvc.perform(MockMvcRequestBuilders.fileUpload("/api/club/56fab17ee4b074b1e6b6ca80/image").file(data)
				.accept(MediaType.APPLICATION_JSON).principal(principal))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		assertTrue(json.get("logoId")!=null);
	}
}
