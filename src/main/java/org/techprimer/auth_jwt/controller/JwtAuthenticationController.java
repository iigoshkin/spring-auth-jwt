package org.techprimer.auth_jwt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.techprimer.auth_jwt.services.JwtUserDetailsService;
import org.techprimer.auth_jwt.util.JwtRequest;
import org.techprimer.auth_jwt.util.JwtResponse;
import org.techprimer.auth_jwt.util.JwtTokenUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class JwtAuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final JwtUserDetailsService userDetailsService;

  @RequestMapping(value = "/token", method = RequestMethod.POST)
  public ResponseEntity<JwtResponse> createAuthToken(@RequestBody JwtRequest request)
      throws Exception {
    authenticate(request.getUsername(), request.getPassword());
    final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
    final String token = jwtTokenUtil.generateToken(userDetails);

    return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername()));
  }

  private void authenticate(String username, String password) throws Exception {
    try{
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }catch(DisabledException e){
      throw new Exception("USER_DISABLED", e);
    }catch(BadCredentialsException e){
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }

}
