package com.codemages.Moviee.cinema.factory;

import com.codemages.Moviee.cinema.movie.constant.GenreEnum;
import com.codemages.Moviee.cinema.movie.constant.MovieRole;
import com.codemages.Moviee.cinema.movie.dto.GenreResponseDTO;
import com.codemages.Moviee.cinema.movie.entity.Genre;
import com.codemages.Moviee.cinema.movie.entity.Movie;
import com.codemages.Moviee.cinema.movie.entity.MovieCredit;
import com.codemages.Moviee.cinema.person.Person;
import com.codemages.Moviee.cinema.room.Room;
import com.codemages.Moviee.cinema.session.Session;
import com.codemages.Moviee.test.util.IdGenerator;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class CinemaFactory {
  private static final String MOVIE_CLASS_NAME = Movie.class.getTypeName();
  private static final String ROOM_CLASS_NAME = Room.class.getTypeName();
  private static final String SESSION_CLASS_NAME = Session.class.getTypeName();
  private static final String GENRE_CLASS_NAME = Genre.class.getTypeName();

  public static Movie createMovieInstance() {
    return Movie.builder()
      .title( "Forrest Gump" )
      .year( 1994 )
      .durationInMinutes( 142 )
      .rating( 8 )
      .build();
  }

  public static MovieCredit createMovieCredit(MovieRole role) {
    return MovieCredit.builder().role( role ).build();
  }

  public static Person createPersonInstance() {
    return Person.builder()
      .artisticName( "Tom Hanks" )
      .realName( "Thomas Jeffrey Hanks" )
      .dob( ZonedDateTime.of( 1956, 7, 9, 0, 0, 0, 0, ZoneOffset.UTC ) )
      .about( "An American actor and filmmaker." )
      .build();
  }

  public static Movie createMovieMock() {
    return Movie.builder()
      .id( IdGenerator.nextId( MOVIE_CLASS_NAME ) )
      .title( "Inception" )
      .durationInMinutes( 148 )
      .description( "A mind-bending SCI-FI" )
      .genre( new Genre( 1L, GenreEnum.ACTION ) )
      .genre( new Genre( 2L, GenreEnum.DRAMA ) )
      .rating( 8 )
      .build();
  }

  public static Room createRoomMock() {
    return Room.builder().id( IdGenerator.nextId( ROOM_CLASS_NAME ) ).name( "Room 1" ).build();
  }

  public static Session createSessionMock(Movie movie, Room room) {
    var now = java.time.ZonedDateTime.now();
    var twoHoursAhead = now.plusHours( 2 );

    return Session.builder()
      .id( IdGenerator.nextId( SESSION_CLASS_NAME ) )
      .movie( movie )
      .room( room )
      .startTime( now )
      .endTime( twoHoursAhead )
      .build();
  }

  public static GenreResponseDTO createGenreResponseDTO() {
    return new GenreResponseDTO(
      IdGenerator.nextId( GENRE_CLASS_NAME ),
      "ACTION",
      "Action films usually include high energy, big-budget physical stunts and chases."
    );
  }
}
