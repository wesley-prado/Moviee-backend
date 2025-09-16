package com.codemages.Moviee.auth.client;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

import static com.codemages.Moviee.auth.client.constant.RegexConstant.URL_PATTERN;

@Entity
@Table(name = "clients", schema = "auth")
@Data
public class Client {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(unique = true, nullable = false, length = 100)
  private UUID id;

  @Column(nullable = false, unique = true, length = 100)
  @Size(min = 5, max = 100)
  private String clientId;
  @Column(nullable = false)
  @Size(min = 8, max = 255)
  private String clientSecret;
  @Column(nullable = false)
  @Pattern(regexp = URL_PATTERN, message = "Invalid URL")
  private String redirectUri;
  @Column(nullable = false, length = 100)
  @Size(min = 5, max = 255)
  private String clientName;
}
