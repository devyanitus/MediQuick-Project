package com.mediquick.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    // ===== AUTH PATHS - should always pass through =====

    @Test
    void authPath_SkipsFilter_CallsFilterChain() throws Exception {
        when(request.getServletPath()).thenReturn("/auth/login");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void authRegisterPath_SkipsFilter_CallsFilterChain() throws Exception {
        when(request.getServletPath()).thenReturn("/auth/register");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
    }

    // ===== MISSING / INVALID HEADER =====

    @Test
    void missingAuthHeader_Returns401() throws Exception {
        when(request.getServletPath()).thenReturn("/some/protected/path");
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1))
                .setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void authHeaderWithoutBearer_Returns401() throws Exception {
        when(request.getServletPath()).thenReturn("/some/protected/path");
        when(request.getHeader("Authorization")).thenReturn("Basic sometoken");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1))
                .setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    // ===== INVALID TOKEN =====

    @Test
    void invalidToken_Returns401() throws Exception {
        when(request.getServletPath()).thenReturn("/some/protected/path");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(jwtService.isTokenValid("invalid.token")).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1))
                .setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    // ===== VALID TOKEN =====

    @Test
    void validToken_CallsFilterChain() throws Exception {
        when(request.getServletPath()).thenReturn("/some/protected/path");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.jwt.token");
        when(jwtService.isTokenValid("valid.jwt.token")).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
    }
}