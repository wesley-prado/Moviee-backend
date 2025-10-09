package com.codemages.Moviee.cinema.movie.controller;

import com.codemages.Moviee.cinema.movie.assembler.MovieModelAssembler;
import com.codemages.Moviee.cinema.movie.dto.MovieResponseDTO;
import com.codemages.Moviee.cinema.movie.service.MovieService;
import com.codemages.Moviee.core.constant.ControllerConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ControllerConstant.PUBLIC_API_BASE + "/v1/movies")
@RequiredArgsConstructor
public class PublicMovieController {
  private static final MovieModelAssembler movieModelAssembler = new MovieModelAssembler();
  private final MovieService movieService;

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<MovieResponseDTO>>> getAllMovies() {
    List<MovieResponseDTO> movies = movieService.findAll();

    return ResponseEntity.ok( movieModelAssembler.toCollectionModel( movies ) );
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<MovieResponseDTO>> findMovieById(@PathVariable Long id) {
    MovieResponseDTO result = movieService.findById( id );
    return ResponseEntity.ok( movieModelAssembler.toModel( result ) );
  }
}
