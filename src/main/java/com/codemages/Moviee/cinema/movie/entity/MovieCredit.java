package com.codemages.Moviee.cinema.movie.entity;

import com.codemages.Moviee.cinema.movie.constant.MovieRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_credits", schema = "cinema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieCredit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "movie_id", nullable = false)
  private Movie movie;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "person_id", nullable = false)
  private Person person;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 50)
  private MovieRole role;
}
