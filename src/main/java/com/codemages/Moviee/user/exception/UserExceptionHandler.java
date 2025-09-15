package com.codemages.Moviee.user.exception;

import com.codemages.Moviee.core.exception.ErrorResponse;
import com.codemages.Moviee.user.controller.v1.PrivateUserController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = PrivateUserController.class)
public class UserExceptionHandler {
  @ExceptionHandler(UserException.class)
  public ResponseEntity<ErrorResponse> handleUserException(UserException ex) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.BAD_REQUEST, null );
  }


  @ExceptionHandler(DuplicateUserException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateUserException(
    DuplicateUserException ex
  ) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.CONFLICT, null );
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(
    UserNotFoundException ex
  ) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.NOT_FOUND, null );
  }

  @ExceptionHandler(UserNotCreatedException.class)
  public ResponseEntity<ErrorResponse> handleUserNotCreatedException(
    UserNotCreatedException ex
  ) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.BAD_REQUEST, null );
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
    DataIntegrityViolationException ex
  ) {
    return ErrorResponse.create( ex.getMessage(), HttpStatus.BAD_REQUEST, null );
  }
}
