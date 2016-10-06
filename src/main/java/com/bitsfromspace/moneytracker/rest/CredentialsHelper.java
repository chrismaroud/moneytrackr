package com.bitsfromspace.moneytracker.rest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.security.Principal;
import java.util.Map;

/**
 * @author chris
 * @since 02/10/2016.
 */
public interface CredentialsHelper {

    static String getEmail(Principal principal){
        if (principal instanceof OAuth2Authentication){
            final OAuth2Authentication auth2Authentication = (OAuth2Authentication)principal;
            if (auth2Authentication.getUserAuthentication() instanceof UsernamePasswordAuthenticationToken){
                final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) auth2Authentication.getUserAuthentication();
                if (token.getDetails() instanceof Map){
                    final Map details = (Map) token.getDetails();
                    return (String) details.get("email");
                }
            }
        }
        return null;
    }
}
