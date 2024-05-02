package jkarcsi.security;

import jkarcsi.utils.exception.CustomAuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtTokenFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
    }

    @Test
    void test_doFilterInternal_catchCustomAuthException() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        String testToken = "testToken";
        CustomAuthException testException = new CustomAuthException("Test exception", HttpStatus.FORBIDDEN); // Adjust constructor as needed

        when(jwtTokenProvider.resolveToken(any(HttpServletRequest.class))).thenReturn(testToken);
        when(jwtTokenProvider.validateToken(testToken)).thenThrow(testException);

        jwtTokenFilter.doFilterInternal(request, response, chain);

        verify(jwtTokenProvider, times(1)).validateToken(testToken);
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
        assertEquals("Test exception", response.getErrorMessage());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}