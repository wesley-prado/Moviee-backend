package com.codemages.Moviee;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureTestEntityManager
@Transactional
public abstract class DatabaseTestContainer {
  private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>(
    "postgres:latest" ).withEnv( "POSTGRES_INITDB_ARGS", "-d" );

  @Autowired
  protected TestEntityManager entityManager;

  private static boolean containerStarted = false;

  @BeforeAll
  static void beforeAll() {
    if ( containerStarted ) return;

    CONTAINER.start();
    containerStarted = true;

    Runtime.getRuntime().addShutdownHook( new Thread( () -> {
      if ( CONTAINER.isRunning() ) {
        CONTAINER.stop();
      }
    } ) );
  }

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add( "spring.datasource.url", CONTAINER::getJdbcUrl );
    registry.add( "spring.datasource.username", CONTAINER::getUsername );
    registry.add( "spring.datasource.password", CONTAINER::getPassword );
    registry.add( "spring.jpa.hibernate.ddl-auto", () -> "validate" );
    registry.add( "moviee.security.remember-me-key", () -> "remember-me-key" );
    registry.add( "moviee.security.issuer-uri", () -> "https://moviee.test.com/" );
  }
}
