package com.codemages.Moviee.cinema.movie.dto;

import com.codemages.Moviee.cinema.movie.constant.GenreEnum;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@NotNull(message = "Os dados do filme são obrigatórios.")
public record MovieCreationDTO(@NotBlank(message = "O título é obrigatório.") String title,

                               @NotNull(message = "O ano é obrigatório.") @Range(min = 1878,
                                 message = "O ano deve ser maior ou igual a 1878.") Integer year,

                               @Size(min = 1,
                                 message = "O filme deve conter pelo menos 1 gênero.") List<GenreEnum> genres,

                               String description,

                               @Positive(message = "A avaliação deve ser um número positivo.") @Max(
                                 value = 10,
                                 message = "A avaliação máxima é 10.") int rating,

                               @Min(value = 50,
                                 message = "A duração mínima é 50 minutos.") int durationInMinutes,

                               List<Long> castIds) {}
