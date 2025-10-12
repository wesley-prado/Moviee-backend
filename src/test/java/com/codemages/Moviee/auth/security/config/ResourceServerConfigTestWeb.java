package com.codemages.Moviee.auth.security.config;

import com.codemages.Moviee.WebIntegrationTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceServerConfigTestWeb extends WebIntegrationTestContainer {
  private static final String EXPLORER_ENDPOINT = "/explorer/index.html#uri=/";

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup( context ).apply( springSecurity() ).build();
  }

  @Test
  @DisplayName("Deve bloquear acesso a endpoints protegidos para usuários anônimos")
  void protectedEndpoints_shouldBeBlockedForAnonymous() throws Exception {
    mvc.perform( get( EXPLORER_ENDPOINT ) )
      .andExpect( status().isUnauthorized() );
  }

  @Test
  @DisplayName("Deve negar acesso ao /explorer para usuário com role USER")
  @WithMockUser(username = "user", authorities = { "USER" })
  void explorer_shouldBeForbiddenForUserRole() throws Exception {
    mvc.perform( get( EXPLORER_ENDPOINT ) ).andExpect( status().isForbidden() );
  }


  @Test
  @DisplayName("Deve permitir acesso ao /explorer para usuário com role ADMIN")
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  void explorer_shouldBeAllowedForAdminRole() throws Exception {
    mvc.perform( get( EXPLORER_ENDPOINT ) ).andExpect( status().isOk() );
  }
}
