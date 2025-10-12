package com.codemages.Moviee.cinema.session;

import com.codemages.Moviee.cinema.factory.CinemaFactory;
import com.codemages.Moviee.cinema.movie.repository.MovieRepository;
import com.codemages.Moviee.cinema.room.RoomRepository;
import com.codemages.Moviee.cinema.session.dto.SessionResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


public class SessionServiceTest {
  @MockitoBean
  private SessionRepository sessionRepository;
  @MockitoBean
  private MovieRepository movieRepository;
  @MockitoBean
  private RoomRepository roomRepository;

  private SessionService sessionService;

  @BeforeEach
  public void setUp() {
    sessionRepository = Mockito.mock( SessionRepository.class );
    movieRepository = Mockito.mock( MovieRepository.class );
    roomRepository = Mockito.mock( RoomRepository.class );

    sessionService = new SessionService( sessionRepository, movieRepository, roomRepository );
  }

  @Test
  @DisplayName("Deve retornar um DTO quando encontrar um registro de Session")
  void findById_WhenRecordExists_ShouldReturnDTO() {
    var room = CinemaFactory.createRoomMock();
    var movie = CinemaFactory.createMovieMock();
    var session = CinemaFactory.createSessionMock( movie, room );

    when( sessionRepository.findById( 1L ) ).thenReturn( Optional.of( session ) );
    when( movieRepository.findById( 1L ) ).thenReturn( Optional.of( movie ) );
    when( roomRepository.findById( 1L ) ).thenReturn( Optional.of( room ) );

    //TODO: Corrigir teste utilizando o ZoneId de Sao Paulo
    var expectedResponse = SessionResponseDTO.builder()
      .id( 1L )
      .movieTitle( movie.getTitle() )
      .roomName( room.getName() )
      .startTime( session.getStartTime() )
      .endTime( session.getStartTime().plusHours( 2 ) )
      .build();

    SessionResponseDTO actualResponse = sessionService.findById( 1L );

    assertThat( actualResponse ).usingRecursiveComparison().isEqualTo( expectedResponse );
  }

  @Test
  @DisplayName("Deve jogar uma exception caso o registro de Session nao seja encontrado")
  void findById_WhenRecordDoesNotExist_ShouldThrowException() {
    when( movieRepository.findById( 1L ) ).thenReturn( Optional.empty() );

    try {
      sessionService.findById( 1L );
    } catch (Exception e) {
      assertThat( e ).isInstanceOf( jakarta.persistence.EntityNotFoundException.class )
        .hasMessage( "Sessão com o Id 1 não encontrada" );
    }
  }

  @Test
  @DisplayName("Deve retornar uma exception quando o Id do filme nao for encontrado")
  void createSession_WhenMovieIdDoesNotExist_ShouldThrowException() {
    var room = CinemaFactory.createRoomMock();
    when( roomRepository.findById( 1L ) ).thenReturn( Optional.of( room ) );
    when( movieRepository.findById( 1L ) ).thenReturn( Optional.empty() );

    try {
//      sessionService.save( createSession( null, room ) );
    } catch (Exception e) {
      assertThat( e ).isInstanceOf( jakarta.persistence.EntityNotFoundException.class )
        .hasMessage( "Filme com o Id 1 não encontrado" );
    }
  }
}
