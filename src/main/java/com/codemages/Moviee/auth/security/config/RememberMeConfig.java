package com.codemages.Moviee.auth.security.config;

import com.codemages.Moviee.auth.security.config.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@Configuration
@RequiredArgsConstructor
public class RememberMeConfig {
  private static final int FOURTEEN_DAYS_IN_SECONDS = 60 * 60 * 24 * 14;

  private final DataSource dataSource;
  private final SecurityProperties securityProperties;

  @Bean
  public Customizer<RememberMeConfigurer<HttpSecurity>> getConfig() {
    return rememberMe -> rememberMe.tokenRepository( persistentTokenRepository() )
      .tokenValiditySeconds( FOURTEEN_DAYS_IN_SECONDS )
      .key( securityProperties.rememberMeKey() );
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepository = new AuthSchemaTokenRepository();
    tokenRepository.setDataSource( dataSource );

    tokenRepository.setCreateTableOnStartup( false ); // flyway handles table creation

    return tokenRepository;
  }
}
