package com.codemages.Moviee.cinema.movie.controller;

import com.codemages.Moviee.cinema.movie.assembler.PersonModelAssembler;
import com.codemages.Moviee.cinema.movie.dto.PersonCreationDTO;
import com.codemages.Moviee.cinema.movie.dto.PersonResponseDTO;
import com.codemages.Moviee.cinema.movie.service.PersonService;
import com.codemages.Moviee.core.constant.ControllerConstant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(ControllerConstant.API_BASE + "/v1/person")
@RequiredArgsConstructor
public class PrivatePersonController {
  private final PersonService personService;
  private final PersonModelAssembler personModelAssembler = new PersonModelAssembler();

  @PreAuthorize("ADMIN")
  @RequestMapping("/create")
  public ResponseEntity<EntityModel<PersonResponseDTO>> createPerson(
    @RequestBody @Valid PersonCreationDTO dto
  ) {
    var response = personService.createPerson( dto );
    return ResponseEntity.ok( personModelAssembler.toModel( response ) );
  }
}
