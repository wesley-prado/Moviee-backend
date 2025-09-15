package com.codemages.Moviee.cinema.room.exception;

import com.codemages.Moviee.core.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoomExceptionHandler {

  @ExceptionHandler(DuplicateRoomException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateRoomException(DuplicateRoomException ex) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.CONFLICT, null );
  }
}
