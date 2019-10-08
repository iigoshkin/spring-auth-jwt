package org.techprimer.auth_jwt.controller;

import java.security.Principal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
  @RequestMapping({ "/hello" })
  public String firstPage(Principal principal) {
    System.out.println("username:"+principal.getName());
    return "Hello World";
  }
}
