package com.codemages.Moviee.cinema.room.controller.v1;

import com.codemages.Moviee.cinema.room.RoomService;
import com.codemages.Moviee.cinema.room.assembler.RoomModelAssembler;
import com.codemages.Moviee.cinema.room.dto.response.RoomResponseDTO;
import com.codemages.Moviee.core.constant.ControllerConstant;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ControllerConstant.PUBLIC_API_BASE + "/v1/rooms")
@AllArgsConstructor
public class PublicRoomController {
  private final RoomService roomService;
  private final RoomModelAssembler roomModelAssembler = new RoomModelAssembler();

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<RoomResponseDTO>>> findAll() {
    List<RoomResponseDTO> rooms = roomService.findAll();
    return ResponseEntity.ok( roomModelAssembler.toCollectionModel( rooms ) );
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<RoomResponseDTO>> findRoomById(
    @PathVariable Long id
  ) {
    RoomResponseDTO room = roomService.findRoomById( id );
    return ResponseEntity.ok( roomModelAssembler.toModel( room ) );
  }
}
