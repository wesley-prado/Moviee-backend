package com.codemages.Moviee.cinema.person;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "persons", schema = "cinema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
