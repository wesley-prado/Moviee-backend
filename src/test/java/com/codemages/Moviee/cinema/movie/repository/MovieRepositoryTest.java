package com.codemages.Moviee.cinema.movie.repository;

import com.codemages.Moviee.DatabaseTestContainer;
import com.codemages.Moviee.cinema.movie.constant.GenreEnum;
import com.codemages.Moviee.cinema.movie.constant.MovieRole;
import com.codemages.Moviee.cinema.movie.entity.Genre;
import com.codemages.Moviee.cinema.movie.entity.Movie;
import com.codemages.Moviee.cinema.movie.entity.MovieCredit;
import com.codemages.Moviee.cinema.person.Person;
import com.codemages.Moviee.cinema.person.PersonRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieRepositoryTest extends DatabaseTestContainer {
  private static final ZoneId ZONE_ID = ZoneId.of( "UTC" );

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private PersonRepository personRepository;

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

    var director = Person.builder()
      .dob( ZonedDateTime.of( 1959, 12, 18, 0, 0, 0, 0, ZONE_ID ) )
      .artisticName( "Frank Darabont" )
      .realName( "Frank Árpád Darabont" )
      .about(
        "Frank Árpád Darabont is a Hungarian-American film director, screenwriter and producer. " +
          "He has been nominated for three Academy Awards and a Golden Globe Award for his work " +
          "on The Shawshank Redemption (1994), The Green Mile (1999) and The Mist (2007)." )
      .build();
    var johnCoffey = Person.builder()
      .dob( ZonedDateTime.of( 1951, 9, 22, 0, 0, 0, 0, ZONE_ID ) )
      .artisticName( "Michael Clarke Duncan" )
      .realName( "Michael Clarke Duncan" )
      .about(
        "Michael Clarke Duncan was an American actor. He was nominated for an Academy Award for " +
          "Best Supporting Actor for his role as John Coffey in The Green Mile (1999)." )
      .build();
    var paulEdgecomb = Person.builder()
      .dob( ZonedDateTime.of( 1955, 3, 28, 0, 0, 0, 0, ZONE_ID ) )
      .artisticName( "Tom Hanks" )
      .realName( "Thomas Jeffrey Hanks" )
      .about( "Thomas Jeffrey Hanks is an American actor and filmmaker. He has received various " +
        "accolades, including two Academy Awards, four Golden Globe Awards, a Primetime Emmy " +
        "Award and the Cecil B. DeMille Award." )
      .build();
    personRepository.saveAll( List.of( director, johnCoffey, paulEdgecomb ) );

    var movieCreditDirector = MovieCredit.builder()
      .movie( movie )
      .person( director )
      .role( MovieRole.DIRECTOR )
      .build();

    var movieCreditJohnCoffey = MovieCredit.builder()
      .movie( movie )
      .person( johnCoffey )
      .role( MovieRole.ACTOR )
      .build();

    var movieCreditPaulEdgecomb = MovieCredit.builder()
      .movie( movie )
      .person( paulEdgecomb )
      .role( MovieRole.ACTOR )
      .build();

    movie.addCredit( movieCreditDirector );
    movie.addCredit( movieCreditJohnCoffey );
    movie.addCredit( movieCreditPaulEdgecomb );

    movieRepository.save( movie );
    entityManager.flush();
    entityManager.clear();
  }

  @AfterEach
  public void tearDown() {
    movieRepository.deleteAll();
    personRepository.deleteAll();
  }

  @Nested
  @DisplayName("DML")
  class DmlTests {
    @Test
    @DisplayName("Should persist MovieCredits correctly when saving a Movie with associated " +
      "credits")
    void shouldPersistMovieCreditsCorrectly() {
      var actor = Person.builder()
        .artisticName( "Keanu Reeves" )
        .realName( "Keanu Charles Reeves" )
        .dob( ZonedDateTime.of( 1964, 9, 2, 0, 0, 0, 0, ZONE_ID ) )
        .about( "Neo himself." )
        .build();

      personRepository.save( actor );

      var movie = Movie.builder()
        .title( "The Matrix" )
        .year( 1999 )
        .durationInMinutes( 136 )
        .rating( 9 )
        .description( "Wake up, Neo." )
        .genre( Genre.builder().id( 2L ).name( GenreEnum.ACTION ).build() )
        .build();

      var credit = MovieCredit.builder()
        .movie( movie )
        .person( actor )
        .role( MovieRole.ACTOR )
        .build();

      movie.addCredit( credit );

      var savedMovie = movieRepository.save( movie );

      // force flush and clear to ensure we read from the database and not from the persistence 
      // context
      entityManager.flush();
      entityManager.clear();

      var foundMovie = movieRepository.findById( savedMovie.getId() ).orElseThrow();

      assertThat( foundMovie.getTitle() ).isEqualTo( "The Matrix" );

      assertThat( foundMovie.getCredits() ).hasSize( 1 );
      var foundCredit = foundMovie.getCredits().getFirst();

      assertThat( foundCredit.getRole() ).isEqualTo( MovieRole.ACTOR );
      assertThat( foundCredit.getPerson().getArtisticName() ).isEqualTo( "Keanu Reeves" );
      assertThat( foundCredit.getMovie().getTitle() ).isEqualTo( "The Matrix" );
    }
  }

  @Nested
  @DisplayName("DQL")
  class DqlTests {
    @Test
    @DisplayName("When valid title is passed, should return the movie entity")
    public void findByTitle_success() {
      var movie = Movie.builder()
        .title( "The Green Mile" )
        .year( 1999 )
        .durationInMinutes( 189 )
        .rating( 8 )
        .description(
          "Based on Stephen King's 1996 novel, The Green Mile (1999) follows Paul Edgecomb, a " +
            "1930s" +
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

    @Test
    @DisplayName("When invalid title is passed, should return empty optional")
    public void findByTitle_notFound() {
      var foundMovie = movieRepository.findMovieByTitle( "Non-Existent Movie" );
      assert foundMovie.isEmpty();
    }


    @Test
    @DisplayName("When a valid title is passed, should return MovieCredits correctly linked")
    public void findByTitle_returnsMovieCredits() {
      var foundMovie = movieRepository.findMovieByTitle( "The Green Mile" );

      assertThat( foundMovie ).isPresent();
      var credits = foundMovie.get().getCredits();

      assertThat( credits ).hasSize( 3 );

      assertThat( credits ).filteredOn( c -> c.getRole() == MovieRole.DIRECTOR )
        .extracting( c -> c.getPerson().getArtisticName() )
        .containsOnly( "Frank Darabont" );

      assertThat( credits ).filteredOn( c -> c.getRole() == MovieRole.ACTOR )
        .extracting( c -> c.getPerson().getArtisticName() )
        .containsExactlyInAnyOrder( "Michael Clarke Duncan", "Tom Hanks" );
    }
  }
}
