package com.codemages.Moviee.cinema.movie.service;

import com.codemages.Moviee.cinema.movie.constant.GenreEnum;
import com.codemages.Moviee.cinema.movie.dto.GenreResponseDTO;
import com.codemages.Moviee.cinema.movie.entity.Genre;
import com.codemages.Moviee.cinema.movie.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {
  private final GenreRepository genreRepository;

  @Transactional
  public void insertMissingGenres() {
    List<GenreEnum> existingGenres = genreRepository.findAll()
      .stream()
      .map( Genre::getName )
      .toList();

    List<Genre> missingGenres = Arrays.stream( GenreEnum.values() )
      .filter( g -> !existingGenres.contains( g ) )
      .map( g -> new Genre( null, g ) )
      .toList();

    if ( !missingGenres.isEmpty() ) {
      genreRepository.saveAll( missingGenres );
    }
  }

  @Transactional(readOnly = true)
  public GenreResponseDTO findById(Long id) {
    Genre genre = genreRepository.findById( id )
      .orElseThrow( () -> new IllegalArgumentException( "Genre with id " + id + " not found" ) );

    return toDTO( genre );
  }

  @Transactional(readOnly = true)
  public List<GenreResponseDTO> findAll() {
    List<Genre> genres = genreRepository.findAll();

    return toDTOs( genres );
  }

  private List<GenreResponseDTO> toDTOs(List<Genre> genres) {
    return genres.stream()
      .map( this::toDTO )
      .collect( Collectors.toList() );
  }

  private GenreResponseDTO toDTO(Genre genre) {
    return new GenreResponseDTO(
      genre.getId(),
      genre.getName().name(),
      genre.getName().getDescription()
    );
  }

  @Transactional(readOnly = true)
  public List<Genre> toEntities(List<GenreEnum> genreEnums) {
    List<Genre> genres = genreRepository.findByNameIn( genreEnums.stream()
      .map( g -> g.getName() )
      .toList() );

    return genres;
  }
}