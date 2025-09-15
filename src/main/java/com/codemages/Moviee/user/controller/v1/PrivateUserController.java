package com.codemages.Moviee.user.controller.v1;

import com.codemages.Moviee.core.constant.ControllerConstant;
import com.codemages.Moviee.user.UserService;
import com.codemages.Moviee.user.assembler.UserModelAssembler;
import com.codemages.Moviee.user.dto.PrivateUserCreationDTO;
import com.codemages.Moviee.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ControllerConstant.API_BASE + "/v1/users")
@RequiredArgsConstructor
public class PrivateUserController {
  private final UserModelAssembler userModelAssembler;
  private final UserService userService;

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
  public ResponseEntity<CollectionModel<EntityModel<UserResponseDTO>>> getUsers() {

    List<UserResponseDTO> users = userService.findAll();

    return ResponseEntity.ok().contentType( MediaTypes.HAL_JSON )
      .body( userModelAssembler.toCollectionModel( users ) );
  }

  @PreAuthorize("hasAuthority('MODERATOR')")
  @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
  public ResponseEntity<EntityModel<UserResponseDTO>> getUser(
    @PathVariable UUID id
  ) {
    UserResponseDTO dto = userService.findById( id );

    return ResponseEntity.ok().contentType( MediaTypes.HAL_JSON )
      .body( userModelAssembler.toModel( dto ) );

  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping(consumes = "application/json", produces = MediaTypes.HAL_JSON_VALUE)
  public ResponseEntity<EntityModel<UserResponseDTO>> createUser(
    @RequestBody @Valid PrivateUserCreationDTO dto
  ) {
    UserResponseDTO result = userService.createPrivateUser( dto );

    return ResponseEntity.status( HttpStatus.CREATED )
      .contentType( MediaTypes.HAL_JSON )
      .body( userModelAssembler.toModel( result ) );
  }
}
