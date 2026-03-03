package com.codemages.Moviee.cinema.movie.dto;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PersonResponseDTO(@NotNull("É necessário informar o Id da pessoa") UUID id) {}
