package com.codemages.Moviee.user.controller.v1;

import com.codemages.Moviee.core.constant.ControllerConstant;
import com.codemages.Moviee.user.UserService;
import com.codemages.Moviee.user.assembler.UserModelAssembler;
import com.codemages.Moviee.user.dto.PublicUserCreationDTO;
import com.codemages.Moviee.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerConstant.PUBLIC_API_BASE + "/v1/users")
@RequiredArgsConstructor
public class PublicUserController {
  private final UserModelAssembler userModelAssembler;
  private final UserService userService;

  @PostMapping(consumes = "application/json", produces = MediaTypes.HAL_JSON_VALUE)
  public ResponseEntity<EntityModel<UserResponseDTO>> createUser(
    @RequestBody @Valid PublicUserCreationDTO dto
  ) {
    UserResponseDTO result = userService.createPublicUser( dto );

    return ResponseEntity.status( HttpStatus.CREATED )
      .contentType( MediaTypes.HAL_JSON )
      .body( userModelAssembler.toModel( result ) );
  }
}
