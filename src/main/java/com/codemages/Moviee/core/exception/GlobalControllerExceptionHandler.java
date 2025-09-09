package com.codemages.Moviee.core.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @ExceptionHandler(value = NoResourceFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleNotFound(
    NoResourceFoundException ex
  ) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.NOT_FOUND, null );
  }

  @ResponseStatus(value = HttpStatus.FORBIDDEN)
  @ExceptionHandler({
    AuthorizationDeniedException.class,
    AccessDeniedException.class,
    ForbiddenOperationException.class
  })
  public ResponseEntity<ErrorResponse> handleForbidden(
    Throwable ex
  ) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.FORBIDDEN, null );
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
    EntityNotFoundException ex
  ) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.BAD_REQUEST, null );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
    MethodArgumentNotValidException ex
  ) {
    List<String> errors = ex.getBindingResult().getFieldErrors().stream()
      .map( error -> error.getField() + ": " + error.getDefaultMessage() )
      .toList();
    return ErrorResponse.create(
      "Houve uma falha ao validar os campos da requisição",
      HttpStatus.BAD_REQUEST,
      errors
    );
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleGenericException(
    Exception ex
  ) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null );
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    String customMessage = "A requisição JSON está malformada ou contém valores com " +
      "tipos/formatos inválidos.";
    Throwable cause = ex.getMostSpecificCause();

    if ( cause instanceof InvalidFormatException ) {
      if ( ((InvalidFormatException) cause).getTargetType().isEnum() ) {
        String enumValues = Arrays.stream( ((InvalidFormatException) cause).getTargetType()
            .getEnumConstants() )
          .map( Object::toString )
          .collect( Collectors.joining( ", " ) );

        customMessage =
          "Valor de enum inválido. Por favor, use um dos seguintes valores: [" + enumValues + "]";
      }
    }

    return ErrorResponse.create(
      customMessage,
      HttpStatus.BAD_REQUEST,
      Collections.singletonList( cause.getMessage() )
    );
  }
}
