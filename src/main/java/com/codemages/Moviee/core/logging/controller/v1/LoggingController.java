package com.codemages.Moviee.core.logging.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoggingController {
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/api/v1/logging")
  public ResponseEntity<String> logging() {
    log.trace( "A TRACE Message" );
    log.debug( "A DEBUG Message" );
    log.info( "An INFO Message" );
    log.warn( "A WARN Message" );
    log.error( "An ERROR Message" );

    return ResponseEntity.ok( "Howdy! Check out the Logs to see the output..." );
  }
}
