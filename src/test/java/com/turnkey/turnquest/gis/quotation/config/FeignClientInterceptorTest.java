package com.turnkey.turnquest.gis.quotation.config;

import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FeignClientInterceptorTest {

    @Mock
    private OAuth2AuthorizedClientManager authorizedClientManager;

    @InjectMocks
    private FeignClientInterceptor feignClientInterceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should add Bearer token from JwtAuthenticationToken when authentication is present")
    void shouldAddBearerTokenFromJwtAuthenticationToken() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("testToken");

        JwtAuthenticationToken jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getToken()).thenReturn(jwt);
        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);

        RequestTemplate requestTemplate = new RequestTemplate();
        feignClientInterceptor.apply(requestTemplate);

        assertEquals("Bearer testToken", requestTemplate.headers().get("Authorization").iterator().next());
    }

}
