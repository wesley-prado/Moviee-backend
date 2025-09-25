package com.codemages.Moviee.cinema.person;

import com.codemages.Moviee.cinema.movie.Movie;
import com.codemages.Moviee.cinema.movie.MovieCredit;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "persons", schema = "cinema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "PersonBuilder")
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "artistic_name", nullable = false)
  private String artisticName;
  @Column(name = "real_name", nullable = false)
  private String realName;

  @Column(name = "dob", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private ZonedDateTime dob;

  private String about;

  @OneToMany(mappedBy = "person", cascade = {
    CascadeType.PERSIST, CascadeType.MERGE
  }, orphanRemoval = true)
  @Builder.Default
  private Set<MovieCredit> credits = new HashSet<>();

  public void addCredit(MovieCredit credit) {
    credits.add( credit );
    credit.setPerson( this );
  }

  public void removeCredit(MovieCredit credit) {
    credits.remove( credit );
    credit.setPerson( null );
  }

  private void setCredits(Set<MovieCredit> credits) {
    this.credits = credits;
  }

  @Transient
  public Set<Movie> getMovies() {
    if ( credits == null ) return Collections.emptySet();
    return credits.stream().map( MovieCredit::getMovie ).collect( Collectors.toSet() );
  }

  public static class PersonBuilder {
    @Singular
    private Set<MovieCredit> credits;

    private PersonBuilder credits(Set<MovieCredit> credits) {
      return this;
    }

    public PersonBuilder credit(MovieCredit credit) {
      credits.add( credit );
      return this;
    }
  }
}
