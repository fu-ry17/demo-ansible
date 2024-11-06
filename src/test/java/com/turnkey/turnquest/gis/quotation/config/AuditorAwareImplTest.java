package com.turnkey.turnquest.gis.quotation.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Titus Murithi Bundi
 */
class AuditorAwareImplTest {

    private AuditorAwareImpl auditorAware;
    private Jwt mockJwt;

    @BeforeEach
    void setUp() {
        auditorAware = new AuditorAwareImpl();
        mockJwt = mock(Jwt.class);
    }

    @Test
    void shouldReturnCurrentAuditorWhenAuthenticationIsNotNull() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", "username");
        when(mockJwt.getClaims()).thenReturn(claims);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(mockJwt, null));

        assertEquals("username", auditorAware.getCurrentAuditor().get());
    }

    @Test
    void shouldReturnDefaultAuditorWhenNameClaimIsNull() {
        Map<String, Object> claims = new HashMap<>();
        when(mockJwt.getClaims()).thenReturn(claims);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(mockJwt, null));

        assertEquals("agencify_admin", auditorAware.getCurrentAuditor().get());
    }

    @Test
    void shouldReturnEmptyWhenAuthenticationIsNull() {
        SecurityContextHolder.getContext().setAuthentication(null);

        assertFalse(auditorAware.getCurrentAuditor().isPresent());
    }

}
