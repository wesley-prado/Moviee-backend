package com.codemages.Moviee.cinema.movie.dto;

import org.springframework.hateoas.server.core.Relation;

@Relation(value = "genre", collectionRelation = "genres")
public record GenreResponseDTO(Long id, String name, String description) {}
