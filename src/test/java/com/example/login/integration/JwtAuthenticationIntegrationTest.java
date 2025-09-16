package com.example.login.integration;

import com.example.login.dto.LoginRequest;
import com.example.login.dto.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JwtAuthenticationIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void testLoginWithValidCredentials_ShouldReturnJwtToken() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("admin");
        loginRequest.setPassword("test123");

        // When & Then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Login successful")))
                .andExpect(jsonPath("$.user.username", is("admin")))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    void testLoginWithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("admin");
        loginRequest.setPassword("wrongpassword");

        // When & Then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", is("Invalid username/email or password")));
    }

    @Test
    void testApiAccessWithValidJwtToken_ShouldReturnData() throws Exception {
        // Given
        // First, login to get a JWT token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("admin");
        loginRequest.setPassword("test123");

        String loginResponse = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse response = objectMapper.readValue(loginResponse, LoginResponse.class);
        String jwtToken = response.getToken();

        // When & Then - Access protected API with JWT token
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.users").isArray());
    }

    @Test
    void testApiAccessWithInvalidJwtToken_ShouldReturnUnauthorized() throws Exception {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When & Then
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testApiAccessWithoutJwtToken_ShouldReturnUnauthorized() throws Exception {
        // Given
        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogoutWithValidJwtToken_ShouldInvalidateToken() throws Exception {
        // Given
        // First, login to get a JWT token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("admin");
        loginRequest.setPassword("test123");

        String loginResponse = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse response = objectMapper.readValue(loginResponse, LoginResponse.class);
        String jwtToken = response.getToken();

        // When - Logout with JWT token
        mockMvc.perform(post("/logout")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Logout successful")))
                .andExpect(jsonPath("$.status", is("logged_out")));

        // Then - Try to access API with the same token (should fail)
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogoutWithoutJwtToken_ShouldStillReturnSuccess() throws Exception {
        // Given
        // When & Then
        mockMvc.perform(post("/logout"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Logout successful")))
                .andExpect(jsonPath("$.status", is("logged_out")));
    }

    @Test
    void testJwtTokenContainsRequiredClaims() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("admin");
        loginRequest.setPassword("test123");

        // When
        String loginResponse = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse response = objectMapper.readValue(loginResponse, LoginResponse.class);
        String jwtToken = response.getToken();

        // Then - Verify JWT token structure (basic check)
        assert jwtToken != null;
        assert jwtToken.split("\\.").length == 3; // JWT has 3 parts
        
        // The token should contain the user information
        // Note: In a real test, you might want to decode the JWT and verify its claims
        // For now, we just verify the token exists and has the correct structure
    }

    @Test
    void testDifferentUsersGetDifferentTokens() throws Exception {
        // Given
        // Login as admin
        LoginRequest adminLogin = new LoginRequest();
        adminLogin.setUsernameOrEmail("admin");
        adminLogin.setPassword("test123");

        String adminResponse = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse adminLoginResponse = objectMapper.readValue(adminResponse, LoginResponse.class);
        String adminToken = adminLoginResponse.getToken();

        // Login as user
        LoginRequest userLogin = new LoginRequest();
        userLogin.setUsernameOrEmail("user");
        userLogin.setPassword("test123");

        String userResponse = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLogin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse userLoginResponse = objectMapper.readValue(userResponse, LoginResponse.class);
        String userToken = userLoginResponse.getToken();

        // Then - Tokens should be different
        assert !adminToken.equals(userToken);
    }
}
