package com.codemages.Moviee;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class WebIntegrationTestContainer {
  //TODO: Remover esta classe e configurar diretamente nos testes de integração quando o 
  @Container
  private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>(
    "postgres:latest" ).withEnv( "POSTGRES_INITDB_ARGS", "-d" ).withReuse( true );

  private static boolean containerStarted = false;

  @BeforeAll
  static void beforeAll() {
    if ( !CONTAINER.isRunning() ) {
      CONTAINER.start();
      System.out.println( "🐳 Testcontainer iniciado: " + CONTAINER.getJdbcUrl() );
    }
  }

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add( "spring.datasource.url", CONTAINER::getJdbcUrl );
    registry.add( "spring.datasource.username", CONTAINER::getUsername );
    registry.add( "spring.datasource.password", CONTAINER::getPassword );
    registry.add( "spring.jpa.hibernate.ddl-auto", () -> "create-drop" );
    registry.add( "moviee.security.remember-me-key", () -> "remember-me-key" );
    registry.add( "moviee.security.issuer-uri", () -> "https://moviee.test.com/" );

    registry.add( "spring.datasource.hikari.maximum-pool-size", () -> "10" );
    registry.add( "spring.datasource.hikari.minimum-idle", () -> "2" );
    registry.add( "spring.datasource.hikari.connection-timeout", () -> "30000" ); // 30s
    registry.add( "spring.datasource.hikari.idle-timeout", () -> "600000" ); // 10min
    registry.add( "spring.datasource.hikari.max-lifetime", () -> "1800000" ); // 30min
  }
}
