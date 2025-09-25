package com.codemages.Moviee.cinema.movie;

import com.codemages.Moviee.cinema.movie.constant.Genre;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "movies", schema = "cinema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "MovieBuilder")
public class Movie {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  @Range(min = 1878)
  private Integer year;

  @ElementCollection(targetClass = Genre.class)
  @CollectionTable(name = "movie_genres",
    joinColumns = @JoinColumn(name = "movie_id"),
    schema = "cinema")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private List<Genre> genres = new ArrayList<>();

  @Column(columnDefinition = "TEXT")
  private String description;

  @Min(0)
  @Max(10)
  private int rating;

  @Min(50)
  @Column(nullable = false)
  private int durationInMinutes;

  @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<MovieCredit> credits = new HashSet<>();

  private void setCredits(Set<MovieCredit> credits) {
    this.credits = credits;
  }

  public void addCredit(MovieCredit credit) {
    credits.add( credit );
    credit.setMovie( this );
  }

  public void removeCredit(MovieCredit credit) {
    credits.remove( credit );
    credit.setMovie( null );
  }

  public static class MovieBuilder {
    @Singular
    private Set<MovieCredit> credits = new HashSet<>();

    private MovieBuilder credits(Set<MovieCredit> credits) {
      return this;
    }

    public MovieBuilder credit(MovieCredit credit) {
      this.credits.add( credit );
      return this;
    }
  }
}
