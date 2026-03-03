package com.codemages.Moviee.cinema.movie.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record PersonCreationDTO(
  @NotBlank(message = "O nome é obrigatório.") String artisticName,
  @NotBlank(message = "O nome real é obrigatório.") String realName,
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Past(message = "A data de nascimento deve ser no passado.")
  @NotNull(
    "A data de nascimento é obrigatória.") LocalDate dob, String about

) {}
