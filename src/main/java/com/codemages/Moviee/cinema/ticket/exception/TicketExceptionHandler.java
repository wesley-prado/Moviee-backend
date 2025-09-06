package com.codemages.Moviee.cinema.ticket.exception;

import com.codemages.Moviee.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TicketExceptionHandler {
  @ExceptionHandler(SeatAlreadyBookedException.class)
  public ResponseEntity<ErrorResponse> handleSeatAlreadyBookedException(SeatAlreadyBookedException ex) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.CONFLICT, null );
  }
}
