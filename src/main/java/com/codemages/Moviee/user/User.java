package com.codemages.Moviee.user;

import com.codemages.Moviee.user.constant.DocumentType;
import com.codemages.Moviee.user.constant.Role;
import com.codemages.Moviee.user.constant.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "auth")
@Data
@Relation(value = "user", collectionRelation = "users")
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(nullable = false, unique = true, length = 20)
  private String username;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private String password;
  @Column(unique = true, length = 20)
  private String document;

  @Enumerated(EnumType.STRING)
  @Column(length = 10)
  private DocumentType documentType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 25)
  private Role role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 25)
  private UserStatus status;

  public User() {}

  public User(
    UUID id, String username, String email, String password,
    Role role, String document, DocumentType documentType
  ) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.role = role;
    this.status = UserStatus.ACTIVE;
    this.document = document;
    this.documentType = documentType;
  }
}
