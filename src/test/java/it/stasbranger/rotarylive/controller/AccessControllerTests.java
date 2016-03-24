package it.stasbranger.rotarylive.controller;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder.mongoDb;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
	protected MongoDbRule remoteMongoDbRule = new MongoDbRule(mongoDb().databaseName("test").build());
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.alwaysDo(print()).build();
	}
	
	@Test
	public void signupTEST() throws Exception {
		
		String content = "{\"name\":\"Mickey Mouse\",\"refererName\":\"Rotary Club Disney\",\"login\":\"mickey\",\"password\":\"mouse\"}";
		
		mvc.perform(post("/signup")
				.content(content)
				.accept(MediaType.parseMediaType("application/json")))
		.andExpect(status().isCreated());
	}
}
