package com.codemages.Moviee.cinema.movie.repository;

import com.codemages.Moviee.cinema.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
  java.util.Optional<Movie> findMovieByTitle(String title);
}
