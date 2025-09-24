package com.codemages.Moviee.auth.security.config;

import com.codemages.Moviee.WebIntegrationTestContainer;
import com.codemages.Moviee.auth.security.config.constants.ApiPaths;
import com.codemages.Moviee.auth.security.password.PasswordGenerator;
import com.codemages.Moviee.user.User;
import com.codemages.Moviee.user.UserRepository;
import com.codemages.Moviee.user.constant.DocumentType;
import com.codemages.Moviee.user.constant.Role;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DefaultSecurityConfigTestWeb extends WebIntegrationTestContainer {
  @Rule
  public Timeout timeout = new Timeout( 5, TimeUnit.SECONDS );

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

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup( context ).apply( springSecurity() ).build();

    Assertions.assertNotNull( ((JdbcTokenRepositoryImpl) persistentTokenRepository).getJdbcTemplate() );
    ((JdbcTokenRepositoryImpl) persistentTokenRepository).getJdbcTemplate()
      .update( "DELETE FROM persistent_logins" );
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
    mvc.perform( post( "/logout" ).with( csrf() ) )
      .andExpect( status().is3xxRedirection() )
      .andExpect( redirectedUrl( ApiPaths.LOGIN + "?logout" ) );
  }

  @Test
  @Transactional
  @DisplayName("Deve autenticar via cookie remember-me após login inicial")
  void rememberMe_shouldAuthenticateUserOnNewSession() throws Exception {
    String pass = passwordGenerator.generate();
    var userEntity = new User(
      null,
      "myadmin",
      "any_email@mail.com",
      passwordEncoder.encode( pass ),
      Role.ADMIN,
      "228514939",
      DocumentType.RG
    );
    var newUser = userRepository.save( userEntity );

    assertThat( newUser.getId() ).isNotNull();

    MvcResult result = mvc.perform( post( ApiPaths.LOGIN )
        .param( "username", userEntity.getUsername() )
        .param( "password", pass )
        .param( "remember-me", "true" )
        .with( csrf() ) )
      .andExpect( authenticated() )
      .andExpect( cookie().exists( "remember-me" ) )
      .andReturn();

    var rememberMeCookie = result.getResponse().getCookie( "remember-me" );
    assertThat( rememberMeCookie ).isNotNull();

    mvc.perform( get( "/api/v1/users" ).cookie( rememberMeCookie ) )
      .andExpect( authenticated().withAuthenticationName( "myadmin" ) )
      .andExpect( status().isOk() );
  }

  @Test
  @DisplayName("Deve permitir acesso à página de login para usuário anônimo")
  @WithAnonymousUser
  void loginPage_shouldBePublic() throws Exception {
    mvc.perform( get( ApiPaths.LOGIN ) ).andExpect( status().isOk() );
  }
}
