package it.stasbranger.rotarylive.controller;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.ReplicationMongoDbConfigurationBuilder.replicationMongoDbConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

@Transactional
public class AttachControllerTests extends RotaryLiveApplicationTests {

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
	@UsingDataSet(locations={"AttachControllerTests.json"/*, "UserControllerTests.json"*/}, loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void showFileTEST() throws Exception {
		mvc.perform(get("/api/attach/image/5707859be4b0820ad86399f4").contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
