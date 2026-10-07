package org.artostapyshyn.auth.config;

import org.artostapyshyn.auth.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class ReactiveSecurityConfiguration {

    @Bean
    public ServerSecurityContextRepository securityContextRepository() {
        return NoOpServerSecurityContextRepository.getInstance();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(JwtUtil jwtUtil) {
        return authentication -> {
            if (!(authentication instanceof BearerTokenAuthenticationToken bearer)) {
                return Mono.error(new BadCredentialsException("Bearer token is required"));
            }

            try {
                var claims = jwtUtil.getClaims(bearer.getToken());
                String role = claims.get("role", String.class);
                if (role == null || role.isBlank()) {
                    return Mono.error(new BadCredentialsException("JWT role is missing"));
                }

                return Mono.just(new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        bearer.getToken(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))));
            } catch (RuntimeException exception) {
                return Mono.error(new BadCredentialsException("Invalid JWT token", exception));
            }
        };
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            ReactiveAuthenticationManager authenticationManager,
            ServerSecurityContextRepository securityContextRepository) {
        AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(authenticationManager);
        jwtFilter.setServerAuthenticationConverter(new BearerTokenAuthenticationConverter());
        jwtFilter.setSecurityContextRepository(securityContextRepository);
        jwtFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.anyExchange());

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(securityContextRepository)
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/**", "/api/v1/auth/**").permitAll()
                        .anyExchange().authenticated())
                .build();
    }

    private static final class BearerTokenAuthenticationConverter
            implements ServerAuthenticationConverter {

        @Override
        public Mono<Authentication> convert(ServerWebExchange exchange) {
            String authorization = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return Mono.empty();
            }

            String token = authorization.substring("Bearer ".length()).trim();
            return token.isEmpty()
                    ? Mono.empty()
                    : Mono.just(new BearerTokenAuthenticationToken(token));
        }
    }
}
