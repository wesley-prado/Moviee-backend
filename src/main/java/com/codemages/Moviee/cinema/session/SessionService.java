package com.codemages.Moviee.cinema.session;

import com.codemages.Moviee.cinema.movie.MovieRepository;
import com.codemages.Moviee.cinema.movie.exception.MovieNotFoundException;
import com.codemages.Moviee.cinema.room.RoomRepository;
import com.codemages.Moviee.cinema.room.exception.RoomNotFoundException;
import com.codemages.Moviee.cinema.session.dto.SessionCreationDTO;
import com.codemages.Moviee.cinema.session.dto.SessionResponseDTO;
import com.codemages.Moviee.cinema.session.exception.MinimumSessionDurationException;
import com.codemages.Moviee.cinema.session.exception.SessionDatetimeOverlapException;
import com.codemages.Moviee.cinema.session.exception.SessionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
  private final SessionRepository sessionRepository;
  private final MovieRepository movieRepository;
  private final RoomRepository roomRepository;
  private static final ZoneId ZONE_ID = ZoneId.of( "America/Sao_Paulo" );

  @Transactional(readOnly = true)
  public List<SessionResponseDTO> findAll() {
    return sessionRepository.findAll().stream()
      .map( this::toSessionResponseDTO )
      .toList();
  }

  @Transactional(readOnly = true)
  public SessionResponseDTO findById(Long id) {
    return sessionRepository.findById( id )
      .map( this::toSessionResponseDTO )
      .orElseThrow( () -> new SessionNotFoundException(
        "Sessão com o Id " + id + " não encontrada" ) );
  }

  @Transactional
  public SessionResponseDTO createSession(SessionCreationDTO dto) {
    var movie = movieRepository.findById( dto.movieId() )
      .orElseThrow( () -> new MovieNotFoundException(
        "Filme com o Id " + dto.movieId() + " não encontrado" ) );
    var room = roomRepository.findById( dto.roomId() )
      .orElseThrow( () -> new RoomNotFoundException(
        "Sala com o Id " + dto.roomId() + " não encontrada" ) );

    long timeDiffInMinutes = getTimeDiffInMinutes( dto.startTime(), dto.endTime() );

    if ( timeDiffInMinutes < movie.getDurationInMinutes() ) {
      throw new MinimumSessionDurationException(
        "O horário de término da sessão deve ser ao menos " + movie.getDurationInMinutes() +
          " minutos após o horário de início, considerando a duração do filme." );
    }

    var session = Session.builder()
      .movie( movie )
      .room( room )
      .startTime( dto.startTime() )
      .endTime( dto.endTime() )
      .build();

    try {
      var savedEntity = sessionRepository.save( session );
      return toSessionResponseDTO( savedEntity );
    } catch (DataIntegrityViolationException e) {
      if ( e.getMessage().contains( "sessions_room_time_overlap_excl" ) ) {
        throw new SessionDatetimeOverlapException(
          "Conflito de horário: Já existe uma sessão agendada para essa sala nesse horário." );
      }

      throw e;
    }
  }

  private SessionResponseDTO toSessionResponseDTO(Session session) {
    return SessionResponseDTO.builder()
      .id( session.getId() )
      .movieTitle( session.getMovie().getTitle() )
      .roomName( session.getRoom().getName() )
      .startTime( SessionService.convertToLocalTime( session.getStartTime() ) )
      .endTime( SessionService.convertToLocalTime( session.getEndTime() ) )
      .build();
  }

  private static ZonedDateTime convertToLocalTime(ZonedDateTime dateTime) {
    return dateTime.withZoneSameInstant( ZONE_ID );
  }

  private long getTimeDiffInMinutes(ZonedDateTime start, ZonedDateTime end) {
    return ChronoUnit.MINUTES.between( start, end );
  }
}
