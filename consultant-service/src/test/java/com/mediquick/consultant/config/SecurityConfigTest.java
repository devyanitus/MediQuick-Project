package com.mediquick.consultant.config;

import com.mediquick.consultant.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityConfigTest.TestController.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @RestController
    static class TestController {

        @GetMapping("/consultants/home.html")
        public ResponseEntity<String> home() {
            return ResponseEntity.ok("home");
        }

        @GetMapping("/consultants/site.css")
        public ResponseEntity<String> css() {
            return ResponseEntity.ok("css");
        }

        @GetMapping("/consultants/app.js")
        public ResponseEntity<String> js() {
            return ResponseEntity.ok("js");
        }

        @GetMapping("/consultants/images/logo.png")
        public ResponseEntity<String> image() {
            return ResponseEntity.ok("image");
        }

        @GetMapping("/consultants/profile")
        public ResponseEntity<String> protectedGet() {
            return ResponseEntity.ok("protected");
        }

        @PostMapping("/consultants/profile")
        public ResponseEntity<String> protectedPost() {
            return ResponseEntity.ok("protected-post");
        }

        @GetMapping("/public/ping")
        public ResponseEntity<String> publicPing() {
            return ResponseEntity.ok("public");
        }
    }

    @Test
    void shouldAllowHomeHtmlWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/consultants/home.html"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAllowCssWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/consultants/site.css"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAllowJsWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/consultants/app.js"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAllowImagesWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/consultants/images/logo.png"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void shouldAllowProtectedEndpointWithAuthentication() throws Exception {
        mockMvc.perform(get("/consultants/profile"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAllowAnyOtherRequestWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/public/ping"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void shouldAllowPostWithoutCsrfBecauseCsrfIsDisabled() throws Exception {
        mockMvc.perform(post("/consultants/profile"))
                .andExpect(status().isOk());
    }
}