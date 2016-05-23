package it.stasbranger.rotarylive.controller;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.ReplicationMongoDbConfigurationBuilder.replicationMongoDbConfiguration;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;

import it.stasbranger.rotarylive.RotaryLiveApplicationTests;

@Transactional
public class EventControllerTests extends RotaryLiveApplicationTests {

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
	@UsingDataSet(locations={"LocationControllerTests.json", "UserControllerTests.json", "EventControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void findAllEventsTEST() throws Exception {
		
		String result = mvc.perform(get("/api/event")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		
		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("eventList");
		
		assertTrue(array.length()>0);
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.get("name")!=null);
		}
	}
	
	@Test
	@UsingDataSet(locations={"LocationControllerTests.json", "UserControllerTests.json", "EventControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void findAllEventsByQueryDateTEST() throws Exception {
		
		String result = mvc.perform(get("/api/event").contentType("application/json")
				.param("q", "sipe")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		
		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("eventList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.get("name").equals("sipe"));
		}
	}
	
	@Test
	@UsingDataSet(locations={"LocationControllerTests.json", "UserControllerTests.json", "EventControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void findAllEventsByQueryDateTEST2() throws Exception {
		
		String result = mvc.perform(get("/api/event/timeline").contentType("application/json")
				.param("q", "sipe")
				.param("d1", "2016/04/01")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		
		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("eventList");
		assertTrue(array.length() > 0);
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.get("name").equals("SIPE 2016"));
		}
	}
	
	@Test
	@UsingDataSet(locations={"LocationControllerTests.json", "UserControllerTests.json", "EventControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void findAllEventsByQueryDateTEST4() throws Exception {
		
		UsernamePasswordAuthenticationToken principal = this.getPrincipal("flavio");
		
		String result = mvc.perform(get("/api/event/timeline").contentType("application/json")
				.param("d1", "2016/04/01")
				.accept(MediaType.APPLICATION_JSON).principal(principal))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		
		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("eventList");
		assertTrue(array.length() == 2);
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.get("name").equals("SIPE 2016") || j.get("name").equals("RYLA 2016"));
			if(j.get("name").equals("SIPE 2016")){
				assertTrue((Boolean)j.get("booked"));
			}else{
				assertFalse((Boolean)j.get("booked"));
			}
		}
	}
	
	@Test
	@UsingDataSet(locations={"LocationControllerTests.json", "UserControllerTests.json", "EventControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void findAllEventsByQueryDateTEST5() throws Exception {
		
		String result = mvc.perform(get("/api/event/timeline").contentType("application/json")
				.param("d1", "2016/04/01")
				.param("d2", "2016/04/01")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		
		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("eventList");
		assertTrue(array.length() == 1);
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.get("name").equals("SIPE 2016"));
		}
	}
	
	@Test
	@UsingDataSet(locations={"LocationControllerTests.json", "UserControllerTests.json", "EventControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void findAllEventsByQueryDateTEST3() throws Exception {
		
		String result = mvc.perform(get("/api/event").contentType("application/json")
				.param("d1", "2016/04/03")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		
		JSONObject json = new JSONObject(result);
		JSONObject emb = json.getJSONObject("_embedded");
		JSONArray array = emb.getJSONArray("eventList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject j = array.getJSONObject(i);
			assertTrue(j.get("name").equals("SIPE 2016"));
		}
	}
	
	@Test
	@UsingDataSet(locations={"AttachControllerTests.json", "EventControllerTests.json", "UserControllerTests.json", "ClubControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void createBookingTEST() throws Exception {
		UsernamePasswordAuthenticationToken principal = this.getPrincipal("flavio");

		String result = mvc.perform(MockMvcRequestBuilders.post("/api/event/5718ec5c3fa7db28901eeb9a/booking")
				.accept(MediaType.APPLICATION_JSON).principal(principal))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		JSONArray array = json.getJSONArray("booking");
		for(int i = 0 ; i < array.length(); i++){
			JSONObject booking = array.getJSONObject(i);
			JSONObject user = booking.getJSONObject("user");
			assertTrue(user.getString("username").equals("flavio"));
		}
		
		mvc.perform(MockMvcRequestBuilders.post("/api/event/5718ec5c3fa7db28901eeb9a/booking")
				.accept(MediaType.APPLICATION_JSON).principal(principal))
				.andDo(print())
				.andExpect(status().isConflict());
		
		principal = this.getPrincipal("silvio");

		result = mvc.perform(MockMvcRequestBuilders.post("/api/event/5718ec5c3fa7db28901eeb9a/booking")
				.accept(MediaType.APPLICATION_JSON).principal(principal))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();
		
		JSONObject json2 = new JSONObject(result);
		JSONArray array2 = json2.getJSONArray("booking");
		assertTrue(array2.length() == 2);
		for(int i = 0 ; i < array2.length(); i++){
			JSONObject booking = array2.getJSONObject(i);
			JSONObject user = booking.getJSONObject("user");
			assertTrue(user.getString("username").equals("flavio") || user.getString("username").equals("silvio"));
		}
	}
	
	@Test
	@UsingDataSet(locations={"AttachControllerTests.json", "EventControllerTests.json", "UserControllerTests.json", "ClubControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void removeBookingTEST() throws Exception {
		UsernamePasswordAuthenticationToken principal = this.getPrincipal("flavio");

		String result = mvc.perform(MockMvcRequestBuilders.post("/api/event/5718ec5c3fa7db28901eeb9a/booking")
				.accept(MediaType.APPLICATION_JSON).principal(principal))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		JSONObject json = new JSONObject(result);
		JSONArray array = json.getJSONArray("booking");
		assertTrue(array.length() == 1);
		
		result = mvc.perform(MockMvcRequestBuilders.delete("/api/event/5718ec5c3fa7db28901eeb9a/booking")
				.accept(MediaType.APPLICATION_JSON).principal(principal))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		JSONObject json2 = new JSONObject(result);
		JSONArray array2 = json2.getJSONArray("booking");
		assertTrue(array2.length() == 0);
	}
	
	/*@Test
	@UsingDataSet(locations={"LocationControllerTests.json", "UserControllerTests.json", "EventControllerTests.json"}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void crateEventTEST() throws Exception {
		
		String content = "{\"name\":\"RYLA 2016\", \"startDate\" : { \"$date\" : \"2016-04-02T09:00:00.000Z\"}, \"endDate\" : { \"$date\" : \"2016-04-02T13:30:00.000Z\"}, \"content\": \"Evento RYLA organizzato dal Distretto 2120\"}";
		JSONObject json = new JSONObject(content);
		
		mvc.perform(post("/api/event")
				.content(json.toString())
				.contentType("application/json")
				.accept(MediaType.parseMediaType("application/json"))
				)
		.andExpect(status().isCreated());
	}*/
}
