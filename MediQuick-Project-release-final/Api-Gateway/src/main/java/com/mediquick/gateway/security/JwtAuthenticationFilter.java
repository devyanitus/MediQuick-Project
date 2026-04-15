//package com.mediquick.gateway.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import reactor.core.publisher.Mono;
//
//import java.security.Key;
//
//@Component
//public class JwtAuthenticationFilter implements GlobalFilter {
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(secret.getBytes());
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange,
//                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
//
//        String path = exchange.getRequest().getURI().getPath();
//
//        // Allow auth endpoints
//        if (path.contains("/auth")) {
//            return chain.filter(exchange);
//        }
//
//        String header = exchange.getRequest().getHeaders().getFirst("Authorization");
//
//        if (header == null || !header.startsWith("Bearer ")) {
//            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        String token = header.substring(7);
//
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(token);
//
//        } catch (Exception e) {
//            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        return chain.filter(exchange);
//    }
//}