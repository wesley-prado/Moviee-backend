package com.codemages.Moviee.cinema.person;

import com.codemages.Moviee.DatabaseTestContainer;
import com.codemages.Moviee.cinema.factory.CinemaFactory;
import com.codemages.Moviee.cinema.movie.constant.MovieRole;
import com.codemages.Moviee.cinema.movie.entity.Movie;
import com.codemages.Moviee.cinema.movie.entity.MovieCredit;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration test focused on the persistence layer for the Person entity.
 * Validates entity mapping, attributes, constraints, and relationships.
 * Uses @SpringBootTest (inherited), Testcontainers, and TestEntityManager.
 */
class PersonRepositoryTest extends DatabaseTestContainer {
  @Autowired
  private PersonRepository personRepository;

  @Test
  @DisplayName("Should save and retrieve Person with direct attributes")
  @Transactional
  void shouldSaveAndRetrievePersonWithDirectAttributes() {
    Person personToSave = CinemaFactory.createPersonInstance();

    Person savedPerson = entityManager.persistAndFlush( personToSave );
    entityManager.clear();
    Person foundPerson = personRepository.findById( savedPerson.getId() ).orElseThrow();

    assertThat( foundPerson ).isNotNull();
    assertThat( foundPerson.getArtisticName() ).isEqualTo( personToSave.getArtisticName() );
    assertThat( foundPerson.getRealName() ).isEqualTo( personToSave.getRealName() );
    assertThat( foundPerson.getDob() ).isEqualTo( personToSave.getDob() );
    assertThat( foundPerson.getAbout() ).isEqualTo( personToSave.getAbout() );
  }

  @Test
  @DisplayName("Should correctly persist Person-MovieCredit relationship")
  @Transactional
  void shouldCorrectlyPersistMovieCreditRelationship() {
    Movie movie = entityManager.persist( CinemaFactory.createMovieInstance() );
    Person person = entityManager.persist( CinemaFactory.createPersonInstance() );

    MovieCredit credit = MovieCredit.builder()
      .role( MovieRole.ACTOR )
      .build();

    person.addCredit( credit );
    movie.addCredit( credit );

    entityManager.flush();
    entityManager.clear();

    Person foundPerson = personRepository.findById( person.getId() ).orElseThrow();

    assertThat( foundPerson.getCredits() ).hasSize( 1 );

    MovieCredit foundCredit = foundPerson.getCredits().iterator().next();
    assertThat( foundCredit.getRole() ).isEqualTo( MovieRole.ACTOR );
    assertThat( foundCredit.getMovie().getId() ).isEqualTo( movie.getId() );
  }

  @Test
  @DisplayName("Should fail when saving a Person with null real name")
  @Transactional
  void shouldFailWhenSavingPersonWithNullRealName() {
    Person invalidPerson = Person.builder()
      .artisticName( "Artist Without Name" )
      .realName( null )
      .dob( ZonedDateTime.now() )
      .build();

    assertThatThrownBy( () -> entityManager.persistAndFlush( invalidPerson ) ).isInstanceOf(
      ConstraintViolationException.class );
  }
}