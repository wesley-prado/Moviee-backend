package com.codemages.Moviee.cinema.movie.controller;

import com.codemages.Moviee.cinema.movie.assembler.PersonModelAssembler;
import com.codemages.Moviee.cinema.movie.dto.PersonResponseDTO;
import com.codemages.Moviee.cinema.movie.service.PersonService;
import com.codemages.Moviee.core.constant.ControllerConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping(ControllerConstant.PUBLIC_API_BASE + "/v1/person")
@RequiredArgsConstructor
public class PublicPersonController {
  private final PersonModelAssembler personModelAssembler = new PersonModelAssembler();
  private final PersonService personService;

  public ResponseEntity<EntityModel<PersonResponseDTO>> findPersonById(@RequestBody UUID id) {
    var person = personService.findById( id );
    return ResponseEntity.ok( personModelAssembler.toModel( person ) );
  }
}
