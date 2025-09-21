package com.codemages.Moviee.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;

@Builder
@Relation(value = "user", collectionRelation = "users")
@Schema(description = "DTO for user response", name = "UserResponse")
public record UserResponseDTO(
  @Schema(description = "Unique identifier of the user",
    example = "069af49f-030d-4e41-895a-1b40aaf1cdd3")
  UUID id,
  @Schema(description = "Username of the user", example = "myuser")
  String username,
  @Schema(description = "Email address of the user",
    example = "myemail@mail.com")
  String email,
  @Schema(description = "Role assigned to the user",
    example = "USER")
  String role,
  @Schema(description = "Current status of the user",
    example = "ACTIVE")
  String status) {
}
