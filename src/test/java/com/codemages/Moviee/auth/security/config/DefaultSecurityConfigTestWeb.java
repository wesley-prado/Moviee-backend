package com.codemages.Moviee.auth.security.config;

import com.codemages.Moviee.WebIntegrationTestContainer;
import com.codemages.Moviee.auth.security.config.constants.ApiPaths;
import com.codemages.Moviee.auth.security.password.PasswordGenerator;
import com.codemages.Moviee.user.User;
import com.codemages.Moviee.user.UserRepository;
import com.codemages.Moviee.user.constant.DocumentType;
import com.codemages.Moviee.user.constant.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DefaultSecurityConfigTestWeb extends WebIntegrationTestContainer {
  private static final String EXPLORER_ENDPOINT = "/explorer/index.html#uri=/";

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private WebApplicationContext context;
  @Autowired
  private PasswordGenerator passwordGenerator;
  @Autowired
  private PersistentTokenRepository persistentTokenRepository;

  private MockMvc mvc;

  private User testUser;
  private String testUserPassword;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup( context ).apply( springSecurity() ).build();

    testUserPassword = passwordGenerator.generate();
    testUser = new User(
      null,
      "testadmin",
      "test@mail.com",
      passwordEncoder.encode( testUserPassword ),
      Role.ADMIN,
      "228514939",
      DocumentType.RG
    );

    try {
      userRepository.deleteAllInBatch();
      userRepository.flush();
    } catch (Exception e) {
      System.out.println( "⚠️ Aviso ao limpar usuários: " + e.getMessage() );
    }

    testUser = userRepository.save( testUser );
  }

  @Test
  @DisplayName(
    "Deve retornar 401 Unauthorized ao tentar acessar /explorer sem autenticação")
  @WithAnonymousUser
  void explorer_shouldReturnUnauthorized() throws Exception {
    mvc.perform( get( EXPLORER_ENDPOINT ) ).andExpect( status().isUnauthorized() );
  }

  @Test
  @DisplayName("Deve realizar logout e redirecionar para a página de login")
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  void logout_shouldRedirectToLogin() throws Exception {
    mvc.perform( post( ApiPaths.LOGIN )
        .param( "username", testUser.getUsername() )
        .param( "password", testUserPassword )
        .with( csrf() ) )
      .andExpect( authenticated() );

    mvc.perform( post( "/logout" ).with( csrf() ) )
      .andExpect( status().is3xxRedirection() )
      .andExpect( redirectedUrl( ApiPaths.LOGIN + "?logout" ) );
  }

  @Test
  @DisplayName("Deve autenticar via cookie remember-me após login inicial")
  void rememberMe_shouldAuthenticateUserOnNewSession() throws Exception {
    MvcResult result = mvc.perform( post( ApiPaths.LOGIN )
        .param( "username", testUser.getUsername() )
        .param( "password", testUserPassword )
        .param( "remember-me", "true" )
        .with( csrf() ) )
      .andExpect( authenticated() )
      .andExpect( cookie().exists( "remember-me" ) )
      .andReturn();

    var rememberMeCookie = result.getResponse().getCookie( "remember-me" );
    assertThat( rememberMeCookie ).isNotNull();

    mvc.perform( get( "/api/v1/users" ).cookie( rememberMeCookie ) )
      .andExpect( authenticated().withAuthenticationName( testUser.getUsername() ) )
      .andExpect( status().isOk() );
  }

  @Test
  @DisplayName("Deve permitir acesso à página de login para usuário anônimo")
  @WithAnonymousUser
  void loginPage_shouldBePublic() throws Exception {
    mvc.perform( get( ApiPaths.LOGIN ) ).andExpect( status().isOk() );
  }
}
