package com.codemages.Moviee.cinema.movie.controller;

import com.codemages.Moviee.cinema.movie.assembler.GenreModelAssembler;
import com.codemages.Moviee.cinema.movie.dto.GenreResponseDTO;
import com.codemages.Moviee.cinema.movie.service.GenreService;
import com.codemages.Moviee.core.constant.ControllerConstant;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerConstant.PUBLIC_API_BASE + "/v1/genres")
@AllArgsConstructor
public class PublicGenreController {
  private final GenreService genreService;
  private final GenreModelAssembler genreModelAssembler = new GenreModelAssembler();

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<GenreResponseDTO>>> findAllGenres() {
    return ResponseEntity.ok()
      .contentType( MediaTypes.HAL_JSON )
      .body( genreModelAssembler.toCollectionModel( genreService.findAll() ) );
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<GenreResponseDTO>> findGenreById(@PathVariable Long id) {
    return ResponseEntity.ok()
      .body( genreModelAssembler.toModel( genreService.findById( id ) ) );
  }
}
