package com.codemages.Moviee.auth.security.config;

import com.codemages.Moviee.auth.security.config.constants.ApiPaths;
import com.codemages.Moviee.user.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class ResourceServerConfig {
  private final Customizer<RememberMeConfigurer<HttpSecurity>> rememberMeCustomizer;

  @Bean
  @Order(1)
  SecurityFilterChain resourceServerFilterChain(HttpSecurity http)
    throws Exception {

    http
      .securityMatchers( matchers -> matchers
        .requestMatchers( ApiPaths.PRIVATE_RESOURCES )
        .requestMatchers( ApiPaths.EXPLORER_RESOURCES )
      );

    http.sessionManagement( session -> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) );

    http
      .cors( withDefaults() )
      .csrf( AbstractHttpConfigurer::disable )
      .authorizeHttpRequests( auth -> auth
        .requestMatchers( ApiPaths.PUBLIC_RESOURCES ).permitAll()
        .requestMatchers( ApiPaths.EXPLORER_RESOURCES ).hasAuthority( Role.ADMIN.name() )
        .anyRequest().authenticated() )
      .oauth2ResourceServer( oauth2 -> oauth2.jwt(
        jwt -> jwt.jwtAuthenticationConverter( jwtAuthenticationConverter() ) ) );
    return http.build();
  }

  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
      new CustomJwtGrantedAuthoritiesConverter()
        .jwtGrantedAuthoritiesConverter() );

    return jwtAuthenticationConverter;
  }
}
