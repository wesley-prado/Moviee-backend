package com.codemages.Moviee.cinema.person;

import com.codemages.Moviee.DatabaseTestContainer;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


public class PersonRepositoryTest extends DatabaseTestContainer {

  @Autowired
  private PersonRepository personRepository;

  @Test
  @DisplayName("Should save and retrieve a person with all fields correctly mapped")
  @Transactional
  void shouldSaveAndRetrievePerson() {
    ZonedDateTime dateOfBirth = ZonedDateTime.of( 1956, 7, 9, 0, 0, 0, 0, ZoneOffset.UTC );
    Person newPerson = Person.builder()
      .artisticName( "Tom Hanks" )
      .realName( "Thomas Jeffrey Hanks" )
      .dob( dateOfBirth )
      .about( "An American actor and filmmaker." )
      .build();

    Person savedPerson = entityManager.persistAndFlush( newPerson );

    entityManager.clear();

    Person foundPerson = personRepository.findById( savedPerson.getId() ).orElseThrow();

    assertThat( foundPerson ).isNotNull();
    assertThat( foundPerson.getArtisticName() ).isEqualTo( newPerson.getArtisticName() );
    assertThat( foundPerson.getRealName() ).isEqualTo( newPerson.getRealName() );
    assertThat( foundPerson.getDob() ).isEqualTo( newPerson.getDob() );
    assertThat( foundPerson.getAbout() ).isEqualTo( newPerson.getAbout() );
  }

  @Test
  @DisplayName("Should fail when saving a person with null real name")
  @Transactional
  void shouldFailWhenSavingPersonWithNullRealName() {
    Person invalidPerson = Person.builder()
      .artisticName( "Artist Without Name" )
      .realName( null )
      .dob( ZonedDateTime.now() )
      .build();

    assertThatThrownBy( () -> {
      entityManager.persistAndFlush( invalidPerson );
    } ).isInstanceOf( ConstraintViolationException.class );
  }
}
