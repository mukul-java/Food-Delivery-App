package com.mukul.apigateway.filter;

import com.mukul.apigateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;


    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    log.error("Missing authorization header for request path: {}", exchange.getRequest().getURI().getPath());
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {

                    // validate token
                    jwtUtil.validateToken(authHeader);

                    // extract username, role, and userId
                    String username = jwtUtil.extractUserName(authHeader);
                    String role = jwtUtil.extractRole(authHeader);
                    String userId = jwtUtil.extractUserId(authHeader);

                    log.info("Token validated successfully for user: {} (id: {}) with role: {}", username, userId, role);

                    // forward headers downstream
                    request = exchange.getRequest()
                            .mutate()
                            .header("loggedInUser", username)
                            .header("loggedInRole", role)
                            .header("loggedInUserId", userId != null ? userId : "")
                            .build();

                } catch (Exception e) {
                    log.error("Authentication failed for path {}: {}", exchange.getRequest().getURI().getPath(), e.getMessage());
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    public static class Config {}
}
