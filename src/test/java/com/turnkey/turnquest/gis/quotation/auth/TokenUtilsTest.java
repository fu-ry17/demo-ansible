package com.turnkey.turnquest.gis.quotation.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Titus Murithi Bundi
 */
class TokenUtilsTest {

    private TokenUtils tokenUtils;
    private Jwt mockJwt;

    @BeforeEach
    void setUp() {
        tokenUtils = new TokenUtils();
        mockJwt = mock(Jwt.class);
    }

    @Test
    void shouldReturnOrganizationId() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("organization_id", "12345");
        when(mockJwt.getClaims()).thenReturn(claims);

        tokenUtils.init(new UsernamePasswordAuthenticationToken(mockJwt, null));

        assertEquals(12345L, tokenUtils.getOrganizationId());
    }

    @Test
    void shouldReturnUsername() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", "username");
        when(mockJwt.getClaims()).thenReturn(claims);

        tokenUtils.init(new UsernamePasswordAuthenticationToken(mockJwt, null));

        assertEquals("username", tokenUtils.getUsername());
    }

    @Test
    void shouldReturnLicenceNo() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("licence_no", "licence123");
        when(mockJwt.getClaims()).thenReturn(claims);

        tokenUtils.init(new UsernamePasswordAuthenticationToken(mockJwt, null));

        assertEquals("licence123", tokenUtils.getLicenceNo());
    }

    @Test
    void shouldReturnPhoneNumber() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phone_number", "1234567890");
        when(mockJwt.getClaims()).thenReturn(claims);

        tokenUtils.init(new UsernamePasswordAuthenticationToken(mockJwt, null));

        assertEquals("1234567890", tokenUtils.getPhoneNumber());
    }

    @Test
    void shouldReturnClaimByName() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("claim", "value");
        when(mockJwt.getClaims()).thenReturn(claims);

        tokenUtils.init(new UsernamePasswordAuthenticationToken(mockJwt, null));

        assertEquals("value", tokenUtils.getClaimByName("claim"));
    }

}
