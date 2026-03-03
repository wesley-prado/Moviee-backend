package com.codemages.Moviee.cinema.movie.service;

import com.codemages.Moviee.cinema.movie.dto.PersonCreationDTO;
import com.codemages.Moviee.cinema.movie.dto.PersonResponseDTO;
import com.codemages.Moviee.cinema.movie.entity.Person;
import com.codemages.Moviee.cinema.movie.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {
  private PersonRepository personRepository;

  public PersonResponseDTO createPerson(PersonCreationDTO dto) {
    var entity = personRepository.save( Person.builder()
      .artisticName( dto.artisticName() )
      .realName( dto.realName() )
      .dob( dto.dob() )
      .about( dto.about() )
      .build() );

    return toResponseDTO( entity );
  }

  public PersonResponseDTO findById(UUID id) {
    var entity = personRepository.findById( id ).orElseThrow();
    return toResponseDTO( entity );
  }

  private PersonResponseDTO toResponseDTO(Person entity) {
    return new PersonResponseDTO(
      entity.getId()
    );
  }
}
