package org.techprimer.auth_jwt.services;

import lombok.Data;

@Data
public class User {
  private final String username;
  private final String password;
  private final String[] roles;

  public User(String username, String password, String...roles) {
    this.username = username;
    this.password = password;
    this.roles = roles;
  }
}
