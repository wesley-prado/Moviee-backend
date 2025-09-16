package com.codemages.Moviee.cinema.ticket;

import com.codemages.Moviee.cinema.session.Session;
import com.codemages.Moviee.cinema.ticket.constant.TicketStatus;
import com.codemages.Moviee.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tickets",
  schema = "cinema",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_unique_session_seat",
      columnNames = { "session_id", "seat_number" }
    )
  })
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "session_id", nullable = false)
  private Session session;

  @Column(name = "seat_number", nullable = false, length = 3)
  private String seatNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'BOOKED'")
  private TicketStatus status;
}
