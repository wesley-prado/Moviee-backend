package com.codemages.Moviee.cinema.movie.assembler;

import com.codemages.Moviee.cinema.movie.controller.PublicPersonController;
import com.codemages.Moviee.cinema.movie.dto.PersonResponseDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class PersonModelAssembler implements
                                  RepresentationModelAssembler<PersonResponseDTO,
                                    EntityModel<PersonResponseDTO>> {

  @Override
  public EntityModel<PersonResponseDTO> toModel(@NotNull PersonResponseDTO dto) {
    Link self = WebMvcLinkBuilder.linkTo(
      WebMvcLinkBuilder.methodOn( PublicPersonController.class ).findPersonById( dto.id() )
    ).withSelfRel();

    return EntityModel.of( dto, self );
  }
}
