package it.stasbranger.rotarylive.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import it.stasbranger.rotarylive.service.CustomUserDetailsService;

@Configuration
@EnableAuthorizationServer
public class OAuth2ServerConfiguration extends AuthorizationServerConfigurerAdapter{

	private TokenStore tokenStore = new InMemoryTokenStore();

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;
		
	@Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) 
      throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()");
    }

	@Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
               .withClient("rotarylive")
               .authorizedGrantTypes("implicit")
               .scopes("read")
               .autoApprove(true)
               .and()
               .withClient("rotarylive")
               .secret("HgjF3RAxWBKJq6qb")
               .authorizedGrantTypes("password","authorization_code", "refresh_token", "client_credentials")
               .scopes("read");
    }
 
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		// @formatter:off
		endpoints
		.tokenStore(this.tokenStore)
		.authenticationManager(this.authenticationManager)
		.userDetailsService(userDetailsService);
		// @formatter:on
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setTokenStore(this.tokenStore);
		return tokenServices;
	}
}

