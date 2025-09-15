package com.codemages.Moviee.auth.client.exception;

import com.codemages.Moviee.core.exception.ErrorResponse;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

public class ClientExceptionHandler {
  @ExceptionHandler(ClientNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleClientNotFoundException(ClientNotFoundException e) {
    var error = new ErrorResponse(
      HttpStatus.NOT_FOUND.value(), e.getMessage(),
      LocalDateTime.now(), null
    );
    error.add( Link.of( "https://my-api-docs.com/errors/client-not-found", "about" ) );

    return ResponseEntity.status( HttpStatus.NOT_FOUND )
      .contentType( MediaTypes.HAL_JSON )
      .body( error );
  }
}
