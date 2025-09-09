package com.codemages.Moviee.core.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ErrorResponse extends RepresentationModel<ErrorResponse> {
  private final int status;
  private final String message;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-3")
  private final LocalDateTime timestamp;
  private final List<String> errors;

  public ErrorResponse(
    int status, String message, LocalDateTime timestamp,
    List<String> errors
  ) {
    this.status = status;
    this.message = message;
    this.timestamp = timestamp;
    this.errors = errors;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + status;
    result = prime * result + ((message == null) ? 0 : message.hashCode());
    result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if ( this == obj )
      return true;
    if ( !super.equals( obj ) )
      return false;
    if ( getClass() != obj.getClass() )
      return false;
    ErrorResponse other = (ErrorResponse) obj;
    if ( status != other.status )
      return false;
    if ( message == null ) {
      if ( other.message != null )
        return false;
    } else if ( !message.equals( other.message ) )
      return false;
    if ( timestamp == null ) {
      return other.timestamp == null;
    } else return timestamp.equals( other.timestamp );
  }

  public static ResponseEntity<ErrorResponse> create(
    String message,
    HttpStatus status,
    List<String> errors
  ) {
    var error = new ErrorResponse(
      status.value(), message,
      LocalDateTime.now(), errors
    );
    error.add( Link.of(
      "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/" + status.value(), "about" ) );
    error.add( Link.of( "https://httpstatuses.com/" + status.value(), "reference" ) );

    return ResponseEntity.status( status )
      .contentType( MediaTypes.HAL_JSON )
      .body( error );
  }
}