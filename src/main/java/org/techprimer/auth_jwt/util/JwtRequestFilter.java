package org.techprimer.auth_jwt.util;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
  private final UserDetailsService userDetailsService;
  private final JwtTokenUtil jwtTokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    final String requestToken = request.getHeader("Authorization");
    String username = null;
    String jwtToken  = null;

    if(requestToken != null && requestToken.startsWith("Bearer ")){
      jwtToken = requestToken.substring(7);
      try{
        username = jwtTokenUtil.getUsername(jwtToken);
      } catch(IllegalArgumentException e){
        System.out.println("Unable to get JWT Token");
      } catch(ExpiredJwtException e){
        System.out.println("JWT Token has expired");
      }
    } else {
      System.out.println("JWT Token does not begin with Bearer String");
    }

    if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
      UserDetails userDetails  = userDetailsService.loadUserByUsername(username);
      if(jwtTokenUtil.validateToken(jwtToken, userDetails)){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }

    chain.doFilter(request, response);
  }
}
