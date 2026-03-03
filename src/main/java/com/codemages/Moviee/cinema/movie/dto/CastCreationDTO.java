package com.codemages.Moviee.cinema.movie.dto;

import com.codemages.Moviee.cinema.movie.constant.MovieRole;
import org.jetbrains.annotations.NotNull;

public record CastCreationDTO(@NotNull("É necessário informar o Id da pessoa") Long personId,
                              @NotNull("É necessário informar qual o papel desta pessoa no filme") MovieRole role) {}
