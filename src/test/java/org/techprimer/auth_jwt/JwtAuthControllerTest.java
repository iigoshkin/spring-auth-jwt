package org.techprimer.auth_jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Before
  public void setUp(){
    SecurityContextHolder.clearContext();
  }

  @Test
  public void GenerateJwtForUser() throws Exception {
    mockMvc.perform(post("/api/token")
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"password\": \"admin123\", \"username\": \"admin\"}")
    )
    .andExpect(status().isOk())
    .andExpect(content().string("token"));
  }
}
