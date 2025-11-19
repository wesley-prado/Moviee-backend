package com.codemages.Moviee.auth.security.controller.v1;

import com.codemages.Moviee.auth.security.config.constants.ApiPaths;
import com.codemages.Moviee.auth.security.factory.RegisteredClientFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthConsentController.class)
public class AuthConsentControllerTest {
  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private RegisteredClientRepository registeredClientRepository;

  @Test
  @WithMockUser(username = "testuser")
  void consent_withValidClientIdAndScope_shouldReturnConsentView() throws Exception {
    var client = RegisteredClientFactory.createValidClient();
    when( registeredClientRepository.findByClientId( client.getClientId() ) ).thenReturn( client );

    mvc.perform( get( ApiPaths.CONSENT )
        .param( OAuth2ParameterNames.CLIENT_ID, client.getClientId() )
        .param( OAuth2ParameterNames.SCOPE, String.join( " ", client.getScopes() ) )
        .param( OAuth2ParameterNames.STATE, "xyz" ) )
      .andExpect( status().isOk() )
      .andExpect( view().name( "consent" ) );
  }

  @Test
  @WithMockUser(username = "testuser")
  void consent_withInvalidClientIdAndScope_shouldReturnConsentView() throws Exception {
    mvc.perform( get( ApiPaths.CONSENT )
        .param( OAuth2ParameterNames.CLIENT_ID, "invalid_id" )
        .param( OAuth2ParameterNames.SCOPE, "openid profile" )
        .param( OAuth2ParameterNames.STATE, "xyz" ) )
      .andExpect( status().is2xxSuccessful() )
      .andExpect( view().name( "error" ) );
  }
}
