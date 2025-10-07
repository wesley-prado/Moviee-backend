package com.codemages.Moviee.cinema.movie.repository;

import com.codemages.Moviee.cinema.movie.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
  List<Genre> findByNameIn(List<String> names);
}
