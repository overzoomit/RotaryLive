package it.stasbranger.rotarylive.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import it.stasbranger.rotarylive.RotaryLiveApplicationTests;

@Transactional
public class AccessControllerTests extends RotaryLiveApplicationTests {

private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
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
