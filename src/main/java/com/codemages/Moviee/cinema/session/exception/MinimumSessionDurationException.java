package com.codemages.Moviee.cinema.session.exception;

public class MinimumSessionDurationException extends RuntimeException {
  public MinimumSessionDurationException(String message) {
    super( message );
  }
}
