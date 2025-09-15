package com.codemages.Moviee.cinema.room.controller.v1;

import com.codemages.Moviee.cinema.room.RoomService;
import com.codemages.Moviee.cinema.room.assembler.RoomModelAssembler;
import com.codemages.Moviee.cinema.room.dto.request.RoomCreationDTO;
import com.codemages.Moviee.cinema.room.dto.response.RoomResponseDTO;
import com.codemages.Moviee.core.constant.ControllerConstant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerConstant.API_BASE + "/v1/rooms")
@RequiredArgsConstructor
public class PrivateRoomController {
  private final RoomService roomService;
  private final RoomModelAssembler roomModelAssembler =
    new RoomModelAssembler();

  @PostMapping
  @PreAuthorize("hasAuthority('MODERATOR')")
  public ResponseEntity<EntityModel<RoomResponseDTO>> createRoom(
    @RequestBody
    @Valid
    RoomCreationDTO createRequest
  ) {
    RoomResponseDTO createdRoom = roomService.createRoom( createRequest );
    EntityModel<RoomResponseDTO> roomModel =
      roomModelAssembler.toModel( createdRoom );

    return ResponseEntity.created( roomModel.getRequiredLink( IanaLinkRelations.SELF ).toUri() )
      .body( roomModel );
  }
}
