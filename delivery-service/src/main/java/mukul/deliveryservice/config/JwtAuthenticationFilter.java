package mukul.deliveryservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mukul.deliveryservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {
                jwtUtil.validateToken(token);
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                List<SimpleGrantedAuthority> authorities = new java.util.ArrayList<>();
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role));
                    authorities.add(new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role.substring(5) : role));
                    Set<Permission> permissions = Permission.getPermissionsForRole(role);
                    for (Permission perm : permissions) {
                        authorities.add(new SimpleGrantedAuthority(perm.name()));
                    }
                }

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                authorities);

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

            } catch(Exception e) {
                logger.error("Error while assigning security context: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request,response);
    }
}