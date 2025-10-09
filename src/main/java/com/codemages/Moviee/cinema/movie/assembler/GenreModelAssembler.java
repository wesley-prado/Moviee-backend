package com.codemages.Moviee.cinema.movie.assembler;

import com.codemages.Moviee.cinema.movie.controller.PublicGenreController;
import com.codemages.Moviee.cinema.movie.dto.GenreResponseDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class GenreModelAssembler
  implements RepresentationModelAssembler<GenreResponseDTO, EntityModel<GenreResponseDTO>> {


  @Override
  public @NotNull CollectionModel<EntityModel<GenreResponseDTO>> toCollectionModel(@NotNull Iterable<? extends GenreResponseDTO> entities) {
    var genresLink = linkTo( methodOn( PublicGenreController.class ).findAllGenres() ).withRel(
      "genres" );
    var genreModels = StreamSupport.stream( entities.spliterator(), false )
      .map( this::toModel )
      .toList();

    return CollectionModel.of( genreModels, genresLink );
  }

  @Override
  public @NotNull EntityModel<GenreResponseDTO> toModel(@NotNull GenreResponseDTO entity) {
    EntityModel<GenreResponseDTO> model = EntityModel.of( entity );
    model.add( linkTo( methodOn( PublicGenreController.class ).findGenreById( entity.id() ) ).withSelfRel() );

    return model;
  }
}
