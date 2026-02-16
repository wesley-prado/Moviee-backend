package com.codemages.Moviee.cinema.movie.repository;

import com.codemages.Moviee.DatabaseTestContainer;
import com.codemages.Moviee.cinema.movie.constant.GenreEnum;
import com.codemages.Moviee.cinema.movie.entity.Genre;
import com.codemages.Moviee.cinema.movie.entity.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieRepositoryTest extends DatabaseTestContainer {
  @Autowired
  private MovieRepository movieRepository;

  @BeforeEach
  public void setup() {
    var movie = Movie.builder()
      .title( "The Green Mile" )
      .year( 1999 )
      .durationInMinutes( 189 )
      .rating( 8 )
      .description(
        "Based on Stephen King's 1996 novel, The Green Mile (1999) follows Paul Edgecomb, a 1930s" +
          " prison guard in Louisiana overseeing death row (\"the Green Mile\"). He encounters " +
          "John Coffey, a gentle giant sentenced for murdering two girls, who possesses " +
          "extraordinary supernatural healing powers. Paul struggles with the morality of " +
          "executing such a person" )
      .genre( Genre.builder().id( 1L ).name( GenreEnum.DRAMA ).build() )
      .build();

    movieRepository.save( movie );
  }

  @AfterEach
  public void tearDown() {
    movieRepository.deleteAll();
  }

  @Test
  public void findByTitle_WhenValidTitleIsPassed_ShouldReturnTheMovieEntity() {
    var movie = Movie.builder()
      .title( "The Green Mile" )
      .year( 1999 )
      .durationInMinutes( 189 )
      .rating( 8 )
      .description(
        "Based on Stephen King's 1996 novel, The Green Mile (1999) follows Paul Edgecomb, a 1930s" +
          " prison guard in Louisiana overseeing death row (\"the Green Mile\"). He encounters " +
          "John Coffey, a gentle giant sentenced for murdering two girls, who possesses " +
          "extraordinary supernatural healing powers. Paul struggles with the morality of " +
          "executing such a person" )
      .genre( Genre.builder().id( 1L ).name( GenreEnum.DRAMA ).build() )
      .build();
    var foundMovie = movieRepository.findMovieByTitle( "The Green Mile" );
    assert foundMovie.isPresent();
    assertThat( foundMovie.get() ).usingRecursiveComparison()
      .ignoringFields( "credits", "id" )
      .isEqualTo( movie );
    assertThat( foundMovie.get().getId() ).isNotNull();
  }
}
