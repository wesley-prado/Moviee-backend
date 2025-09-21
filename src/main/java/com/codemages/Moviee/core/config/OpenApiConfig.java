package com.codemages.Moviee.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
public class OpenApiConfig {

  @Autowired
  private ResourceLoader resourceLoader;
  @Autowired
  private ObjectMapper objectMapper;

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI().components( new Components().addExamples(
      "userCollectionExample", createExample(
        "Example of a user collection response",
        "classpath:openapi/examples/user/CollectionExample.json"
      )
    ).addExamples(
      "accessDeniedExample", createExample(
        "Example of an access denied error response",
        "classpath:openapi/examples/error/AccessDeniedExample.json"
      )
    ).addExamples(
      "internalServerErrorExample", createExample(
        "Example of an internal server error response",
        "classpath:openapi/examples/error/InternalServerErrorExample.json"
      )
    ) );
  }

  private Example createExample(String summary, String resourcePath) {
    try {
      Resource resource = resourceLoader.getResource( resourcePath );
      Object exampleValue = objectMapper.readValue( resource.getInputStream(), Object.class );

      return new Example().summary( summary ).value( exampleValue );

    } catch (IOException e) {
      throw new RuntimeException(
        "Não foi possível carregar o exemplo OpenAPI: " + resourcePath,
        e
      );
    }
  }
}