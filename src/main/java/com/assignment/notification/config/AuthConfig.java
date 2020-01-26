package com.assignment.notification.config;

import com.assignment.notification.filters.PreAuthFilter;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Configuration
@EnableWebSecurity
public class AuthConfig extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(AuthConfig.class);

    @Value("${api.auth-header-name}")
    private String requestHeader;

    @Value("${api.auth-header-key}")
    private String requestValue;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        PreAuthFilter filter = new PreAuthFilter(requestHeader);
        filter.setAuthenticationManager(new AuthenticationManager() {

            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String principal = (String) authentication.getPrincipal();
                if (!requestValue.equals(principal))
                {
                   logger.info("The API key was not found or not the expected value.");
                }

                else {
                    authentication.setAuthenticated(true);
                }
                return authentication;
            }
        });
        httpSecurity.
                antMatcher("/api/**").
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().addFilter(filter).authorizeRequests().anyRequest().authenticated();
    }

}
