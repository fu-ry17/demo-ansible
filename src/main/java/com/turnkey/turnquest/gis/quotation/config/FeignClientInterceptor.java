package com.turnkey.turnquest.gis.quotation.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {

    private final AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager;

    @Override
    public void apply(RequestTemplate template) {
        final String authorization = HttpHeaders.AUTHORIZATION;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
                String tokenValue = jwtAuthenticationToken.getToken().getTokenValue();
                template.header(authorization, "Bearer " + tokenValue);
            }
        } else {
            template.header(authorization, "Bearer " + getAccessToken(authorizedClientManager));
        }
    }

    public String getAccessToken(OAuth2AuthorizedClientManager manager) {
        OAuth2AuthorizeRequest req = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                .principal("quotation-service").build();
        OAuth2AuthorizedClient client = manager.authorize(req);
        if (client != null) {
            return client.getAccessToken().getTokenValue();
        }
        return null;
    }

}
