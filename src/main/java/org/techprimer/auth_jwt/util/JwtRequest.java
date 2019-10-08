package org.techprimer.auth_jwt.util;

import lombok.Data;

@Data
public class JwtRequest {
  private String username;
  private String password;

}
