package com.codemages.Moviee.cinema.movie.controller;

import com.codemages.Moviee.cinema.movie.assembler.MovieModelAssembler;
import com.codemages.Moviee.cinema.movie.dto.MovieCreationDTO;
import com.codemages.Moviee.cinema.movie.dto.MovieResponseDTO;
import com.codemages.Moviee.cinema.movie.service.MovieService;
import com.codemages.Moviee.core.constant.ControllerConstant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerConstant.API_BASE + "/v1/movies")
@RequiredArgsConstructor
public class PrivateMovieController {
  private final MovieService movieService;
  private final MovieModelAssembler movieModelAssembler =
    new MovieModelAssembler();

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping
  public ResponseEntity<EntityModel<MovieResponseDTO>> createMovie(
    @RequestBody @Valid
    MovieCreationDTO dto
  ) {
    MovieResponseDTO result = movieService.save( dto );
    var model = movieModelAssembler.toModel( result );

    return ResponseEntity.created( model.getRequiredLink( IanaLinkRelations.SELF ).toUri() )
      .contentType( MediaTypes.HAL_JSON )
      .body( model );
  }
}
