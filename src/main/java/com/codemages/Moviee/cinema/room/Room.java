package com.codemages.Moviee.cinema.room;

import com.codemages.Moviee.cinema.room.constant.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "rooms", schema = "cinema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'ACTIVE'")
  @Builder.Default
  private RoomStatus status = RoomStatus.ACTIVE;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private String rows;
}
