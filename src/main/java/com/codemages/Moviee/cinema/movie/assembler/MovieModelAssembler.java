package com.codemages.Moviee.cinema.movie.assembler;

import com.codemages.Moviee.cinema.movie.controller.PublicGenreController;
import com.codemages.Moviee.cinema.movie.controller.PublicMovieController;
import com.codemages.Moviee.cinema.movie.dto.MovieResponseDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;

public class MovieModelAssembler
  implements RepresentationModelAssembler<MovieResponseDTO, EntityModel<MovieResponseDTO>> {
  @Override
  public @NotNull EntityModel<MovieResponseDTO> toModel(@NotNull MovieResponseDTO dto) {
    Link self = WebMvcLinkBuilder.linkTo(
      WebMvcLinkBuilder.methodOn( PublicMovieController.class ).findMovieById( dto.id() )
    ).withSelfRel();

    return EntityModel.of( dto, self );
  }

  @Override
  public @NotNull CollectionModel<EntityModel<MovieResponseDTO>> toCollectionModel(@NotNull Iterable<? extends MovieResponseDTO> dtos) {
    List<EntityModel<MovieResponseDTO>> models = ((List<MovieResponseDTO>) dtos).stream()
      .map( movie -> toModel( movie ) )
      .toList();

    Link moviesLink = WebMvcLinkBuilder.linkTo(
      WebMvcLinkBuilder.methodOn( PublicMovieController.class ).getAllMovies()
    ).withRel( "movies" );
    Link genresLink = WebMvcLinkBuilder.linkTo(
      WebMvcLinkBuilder.methodOn( PublicGenreController.class ).findAllGenres()
    ).withRel( "genres" );

    return CollectionModel.of(
      models,
      moviesLink,
      genresLink
    );
  }
}
