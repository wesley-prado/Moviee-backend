package com.codemages.Moviee.cinema.movie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.HashSet;
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

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "movie_genres",
    schema = "cinema",
    joinColumns = @JoinColumn(name = "movie_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id"))
  @Builder.Default
  private Set<Genre> genres = new HashSet<>();

  @Column(columnDefinition = "TEXT")
  private String description;

  @Min(0)
  @Max(10)
  private int rating;

  @Min(50)
  @Column(nullable = false)
  private int durationInMinutes;

  @OneToMany(mappedBy = "movie",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.EAGER)
  private Set<MovieCredit> credits = new HashSet<>();

  public ArrayList<MovieCredit> getCredits() {
    return new ArrayList<>( credits );
  }

  private void setGenres(Set<Genre> genres) {
    this.genres = genres;
  }

  public void addGenre(Genre genre) {
    this.genres.add( genre );
  }

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
    private final Set<MovieCredit> credits = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();

    private MovieBuilder credits(Set<MovieCredit> credits) {
      return this;
    }

    public MovieBuilder credit(MovieCredit credit) {
      this.credits.add( credit );
      return this;
    }

    private MovieBuilder genres(Set<Genre> genres) {return this;}

    public MovieBuilder genre(Genre genre) {
      this.genres.add( genre );
      return this;
    }
  }
}
