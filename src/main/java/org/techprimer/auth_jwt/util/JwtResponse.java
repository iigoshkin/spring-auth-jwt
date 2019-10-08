package org.techprimer.auth_jwt.util;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class JwtResponse {
  private final String token;
  private final String username;
}
