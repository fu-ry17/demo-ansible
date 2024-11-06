package com.turnkey.turnquest.gis.quotation.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestAndResponseLoggingFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private RequestAndResponseLoggingFilter filter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new RequestAndResponseLoggingFilter();
    }

    @Test
    void shouldLogRequestAndResponseWhenContentTypeIsVisible() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("text/plain");
        request.setContent("Hello, World!".getBytes());

        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setContentType("text/plain");
        response.getWriter().write("Hello, World!");

        filter.doFilterInternal(request, response, (req, res) -> {});

        assertEquals("Hello, World!", response.getContentAsString());
    }

    @Test
    void shouldNotLogRequestAndResponseWhenContentTypeIsNotVisible() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/octet-stream");
        request.setContent(new byte[10]);

        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setContentType("application/octet-stream");
        response.getOutputStream().write(new byte[10]);

        filter.doFilterInternal(request, response, (req, res) -> {});

        assertEquals(10, response.getContentAsByteArray().length);
    }

}