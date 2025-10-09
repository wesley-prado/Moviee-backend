package com.codemages.Moviee.auth.security.factory;

import com.codemages.Moviee.cinema.movie.constant.GenreEnum;
import com.codemages.Moviee.cinema.movie.dto.GenreResponseDTO;
import com.codemages.Moviee.cinema.movie.dto.MovieCreationDTO;
import com.codemages.Moviee.cinema.movie.dto.MovieResponseDTO;
import com.codemages.Moviee.cinema.movie.entity.Movie;
import com.codemages.Moviee.test.util.IdGenerator;

import java.util.List;

public class MovieFactory {
  private static final String entityName = Movie.class.getTypeName();

  public static MovieCreationDTO createMovieRequest() {
    return new MovieFactory.Builder().build();
  }

  public static MovieResponseDTO createMovieResponseDTO() {
    return MovieResponseDTO.builder()
      .id( IdGenerator.nextId( entityName ) )
      .title( "Inception" )
      .year( 2010 )
      .genres( List.of(
        new GenreResponseDTO( 1L, GenreEnum.SCI_FI.name(), "A tiny description" ),
        new GenreResponseDTO( 2L, GenreEnum.ACTION.name(), "A tiny description" )
      ) )
      .description( "A mind-bending thriller about dream invasion." )
      .build();
  }

  private static class Id {
    private static long value;

    private static synchronized long getValue() {
      return value++;
    }
  }

  public static class Builder {
    private String title = "Inception";
    private Integer year = 2010;
    private List<GenreEnum> genreEnums = List.of( GenreEnum.SCI_FI, GenreEnum.ACTION );
    private String description = "A mind-bending thriller about dream invasion.";
    private int rating = 8;
    private int durationInMinutes = 148;
    private String director = "Christopher Nolan";
    private String[] cast = new String[]{
      "Leonardo DiCaprio", "Joseph Gordon-Levitt", "Ellen Page"
    };
    private String[] writers = new String[]{ "Christopher Nolan" };

    public Builder withTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder withYear(Integer year) {
      this.year = year;
      return this;
    }

    public Builder withGenres(List<GenreEnum> genreEnums) {
      this.genreEnums = genreEnums;
      return this;
    }

    public Builder withDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder withRating(int rating) {
      this.rating = rating;
      return this;
    }

    public Builder withDurationInMinutes(int durationInMinutes) {
      this.durationInMinutes = durationInMinutes;
      return this;
    }

    public Builder withDirector(String director) {
      this.director = director;
      return this;
    }

    public Builder withCast(String[] cast) {
      this.cast = cast;
      return this;
    }

    public Builder withWriters(String[] writers) {
      this.writers = writers;
      return this;
    }

    public MovieCreationDTO build() {
      return new MovieCreationDTO(
        title,
        year,
        genreEnums,
        description,
        rating,
        durationInMinutes,
        director,
        cast,
        writers
      );
    }
  }
}
