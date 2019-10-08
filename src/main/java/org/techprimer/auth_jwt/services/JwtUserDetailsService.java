package org.techprimer.auth_jwt.services;

import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsService implements UserDetailsService {
  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    User user = findUserbyUername(s);

    UserBuilder builder = null;
    if(user != null){
      builder = org.springframework.security.core.userdetails.User.withUsername(s);
      builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));
      builder.roles(user.getRoles());

      UserDetails details = builder.build();

      return details;
    }
    throw new UsernameNotFoundException("User not fount");
  }

  private User findUserbyUername(String username) {
    if(username.equalsIgnoreCase("admin")) {
      return new User(username, "admin123","ADMIN");
    }
    return null;
  }
}
