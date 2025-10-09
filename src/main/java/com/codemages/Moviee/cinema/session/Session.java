package com.codemages.Moviee.cinema.session;

import com.codemages.Moviee.cinema.movie.entity.Movie;
import com.codemages.Moviee.cinema.room.Room;
import com.codemages.Moviee.cinema.session.constant.SessionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "sessions",
  schema = "cinema",
  indexes = {
    @Index(name = "idx_active_sessions", columnList = "room_id")
  }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "movie_id", nullable = false)
  private Movie movie;

  @ManyToOne
  @JoinColumn(name = "room_id", nullable = false)
  private Room room;

  @Column(name = "start_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private ZonedDateTime startTime;

  @Column(name = "end_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private ZonedDateTime endTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'SCHEDULED'")
  @Builder.Default
  private SessionStatus status = SessionStatus.SCHEDULED;
}
