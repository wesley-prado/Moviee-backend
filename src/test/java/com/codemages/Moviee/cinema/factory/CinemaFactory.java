package com.codemages.Moviee.cinema.factory;

import com.codemages.Moviee.cinema.movie.Movie;
import com.codemages.Moviee.cinema.movie.MovieCredit;
import com.codemages.Moviee.cinema.movie.constant.Genre;
import com.codemages.Moviee.cinema.movie.constant.MovieRole;
import com.codemages.Moviee.cinema.movie.test.util.IdGenerator;
import com.codemages.Moviee.cinema.person.Person;
import com.codemages.Moviee.cinema.room.Room;
import com.codemages.Moviee.cinema.session.Session;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class CinemaFactory {
  private static final String MOVIE_CLASS_NAME = Movie.class.getTypeName();
  private static final String ROOM_CLASS_NAME = Room.class.getTypeName();
  private static final String SESSION_CLASS_NAME = Session.class.getTypeName();

  public static Movie createMovie() {
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

  public static Person createPerson() {
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
      .genres( List.of( Genre.SCI_FI, Genre.ACTION, Genre.DRAMA ) )
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
}
