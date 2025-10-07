package com.codemages.Moviee.cinema.movie.entity;

import com.codemages.Moviee.cinema.movie.constant.GenreEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "genres", schema = "cinema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Genre {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  @Enumerated(EnumType.STRING)
  private GenreEnum name;
}
