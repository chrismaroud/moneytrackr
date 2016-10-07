package com.bitsfromspace.moneytracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import java.security.Principal;
import java.time.Clock;

@SpringBootApplication
@EnableOAuth2Client
@RestController
@ComponentScan({"com.bitsfromspace.moneytracker.rest", "com.bitsfromspace.moneytracker.dao.impl", "com.bitsfromspace.moneytracker"})
public class Application extends WebSecurityConfigurerAdapter{

    @Inject
    private OAuth2ClientContext oauth2ClientContext;
    @Inject
    private Credentials credentials;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @SuppressWarnings("unused")
    @Bean
    @Singleton

    public Clock createClock() {
        return Clock.systemDefaultZone();
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**", "/css/**", "/img/**", "/js/**", "/index.html").permitAll()
                .anyRequest().authenticated()
                    .and().logout().logoutSuccessUrl("/").permitAll()
                    .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .and().addFilterBefore(ssoFilter(credentials), BasicAuthenticationFilter.class);

    }

    private Filter ssoFilter(Credentials credentials) {
        final AuthorizationCodeResourceDetails google = google(credentials);
        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google, oauth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        googleFilter.setTokenServices(new UserInfoTokenServices(googleResource().getUserInfoUri(), google.getClientId()));
        return googleFilter;
    }

    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google(Credentials credentials) {
        final AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setClientId(credentials.getGoogleClientId());
        details.setClientSecret(credentials.getGoogleClientSecret());
        return details;
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }
}

