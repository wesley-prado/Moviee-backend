package com.codemages.Moviee.cinema.session.exception;

import com.codemages.Moviee.core.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SessionExceptionHandler {

  @ExceptionHandler(SessionDatetimeOverlapException.class)
  public ResponseEntity<ErrorResponse> handleSessionDatetimeOverlapException(
    SessionDatetimeOverlapException ex
  ) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.CONFLICT, null );
  }

  @ExceptionHandler(MinimumSessionDurationException.class)
  public ResponseEntity<ErrorResponse> handleMinimumSessionDurationException(
    MinimumSessionDurationException ex
  ) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.BAD_REQUEST, null );
  }
}
