package com.codemages.Moviee.cinema.session.controller.v1;

import com.codemages.Moviee.cinema.session.SessionService;
import com.codemages.Moviee.cinema.session.assembler.SessionModelAssembler;
import com.codemages.Moviee.cinema.session.dto.SessionResponseDTO;
import com.codemages.Moviee.core.constant.ControllerConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerConstant.PUBLIC_API_BASE + "/v1/sessions")
@RequiredArgsConstructor
public class PublicSessionController {
  private final SessionService sessionService;
  private final SessionModelAssembler sessionModelAssembler = new SessionModelAssembler();

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<SessionResponseDTO>>> findAll() {
    var response = sessionService.findAll();
    return ResponseEntity.ok( sessionModelAssembler.toCollectionModel( response ) );
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<SessionResponseDTO>> findById(@PathVariable Long id) {
    var response = sessionService.findById( id );
    return ResponseEntity.ok( sessionModelAssembler.toModel( response ) );
  }
}
