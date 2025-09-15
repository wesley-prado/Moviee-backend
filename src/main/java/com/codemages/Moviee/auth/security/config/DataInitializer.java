package com.codemages.Moviee.auth.security.config;

import com.codemages.Moviee.auth.client.ClientService;
import com.codemages.Moviee.auth.client.dto.ClientDTO;
import com.codemages.Moviee.user.UserService;
import com.codemages.Moviee.user.constant.DocumentType;
import com.codemages.Moviee.user.constant.Role;
import com.codemages.Moviee.user.dto.PrivateUserCreationDTO;
import com.codemages.Moviee.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile({ "dev", "test" })
@RequiredArgsConstructor
public class DataInitializer {
  private final UserService userService;
  private final ClientService clientService;

  @Bean
  CommandLineRunner initData() {
    return args -> {
      if ( !userService.isUsernameTaken( "myuser" ) ) {
        PrivateUserCreationDTO user = new PrivateUserCreationDTO(
          "myuser", "user@mail.com",
          "User1#@@", "336189783",
          DocumentType.RG.name(),
          Role.USER
        );
        PrivateUserCreationDTO admin = new PrivateUserCreationDTO(
          "admin", "admin@mail.com",
          "Admin1#@", "479212910",
          DocumentType.RG.name(),
          Role.ADMIN
        );

        UserResponseDTO regularUser = userService.createPrivateUser( user );
        UserResponseDTO adminUser = userService.createPrivateUser( admin );

        log.debug( "Created Regular User: {}", regularUser );
        log.debug( "Created Admin User: {}", adminUser );
      }

      if ( !clientService.existsByClientId( "postman" ) ) {
        clientService.save( new ClientDTO(
          "postman",
          "my_client_secret",
          "Postman Client",
          "https://oauth.pstmn.io/v1/callback"
        ) );

        log.debug( "Created Client: postman" );
      }
    };
  }
}