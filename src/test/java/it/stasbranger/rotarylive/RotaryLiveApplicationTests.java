package it.stasbranger.rotarylive;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RotaryLiveApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public abstract class RotaryLiveApplicationTests {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
}
