package com.turnkey.turnquest.gis.quotation.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SecurityConfigTest {

    @Mock
    private ClientRegistrationRepository clientRegistrationRepository;

    @Mock
    private OAuth2AuthorizedClientService authorizedClientService;

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should configure HttpSecurity correctly")
    public void shouldConfigureHttpSecurity() throws Exception {
        HttpSecurity httpSecurity = Mockito.mock(HttpSecurity.class);

        // Stubbing the methods to return the mock itself
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.oauth2ResourceServer(any())).thenReturn(httpSecurity);

        SecurityFilterChain securityFilterChain = securityConfig.securityFilterChain(httpSecurity);

        verify(httpSecurity).authorizeHttpRequests(any());
        verify(httpSecurity).cors(any());
        verify(httpSecurity).oauth2ResourceServer(any());
    }

    @Test
    @DisplayName("Should configure AuthorizedClientManager correctly")
    public void shouldConfigureAuthorizedClientManager() {
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = securityConfig.authorizedClientManager(clientRegistrationRepository, authorizedClientService);

        assertNotNull(authorizedClientManager);
    }
}