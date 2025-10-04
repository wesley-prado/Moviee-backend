package com.codemages.Moviee.auth.security.config;

import com.codemages.Moviee.auth.security.config.constants.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class DefaultSecurityConfig {
  private final Customizer<RememberMeConfigurer<HttpSecurity>> rememberMeCustomizer;

  @Bean
  @Order(2)
  public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher( ApiPaths.UI_RESOURCES )
      .authorizeHttpRequests( authorize -> authorize.requestMatchers(
        ApiPaths.LOGIN,
        ApiPaths.CSS,
        ApiPaths.JS,
        ApiPaths.ERROR
      ).permitAll().anyRequest().authenticated() )
      .csrf( csrf -> csrf.ignoringRequestMatchers( ApiPaths.LOGIN, ApiPaths.LOGOUT ) )
      .formLogin( formLogin -> formLogin.loginPage( ApiPaths.LOGIN ) )
      .logout( logout -> logout.logoutUrl( ApiPaths.LOGOUT )
        .logoutRequestMatcher( new AntPathRequestMatcher( ApiPaths.LOGOUT, "POST" ) )
        .logoutSuccessUrl( ApiPaths.LOGIN + "?logout" )
        .deleteCookies( "JSESSIONID", "remember-me" )
        .invalidateHttpSession( true )
        .clearAuthentication( true )
        .permitAll() )
      .rememberMe( rememberMeCustomizer );

    return http.build();
  }
}