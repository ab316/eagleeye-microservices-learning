package com.learning.eagleeye.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
public class JwtOAuth2Config extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private TokenStore tokenStore;
    private DefaultTokenServices tokenServices;
    private JwtAccessTokenConverter accessTokenConverter;
    private TokenEnhancer tokenEnhancer;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public JwtOAuth2Config(AuthenticationManager authenticationManager,
                           UserDetailsService userDetailsService,
                           TokenStore tokenStore,
                           DefaultTokenServices tokenServices,
                           JwtAccessTokenConverter accessTokenConverter,
                           TokenEnhancer tokenEnhancer,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenStore = tokenStore;
        this.tokenServices = tokenServices;
        this.accessTokenConverter = accessTokenConverter;
        this.tokenEnhancer = tokenEnhancer;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Defines which clients are registered with the service
     *
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("eagleeye")
                .secret(passwordEncoder.encode("thisismysecret"))
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                .scopes("webclient", "mobileclient");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer, accessTokenConverter));

        endpoints
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
}
