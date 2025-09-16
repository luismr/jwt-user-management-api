package com.example.login.util;

import com.example.login.dto.LoginRequest;
import com.example.login.dto.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Utility class for JWT testing operations.
 */
public class JwtTestUtil {

    /**
     * Login and get JWT token for testing.
     *
     * @param mockMvc MockMvc instance
     * @param objectMapper ObjectMapper instance
     * @param username Username or email
     * @param password Password
     * @return JWT token string
     * @throws Exception if login fails
     */
    public static String loginAndGetToken(MockMvc mockMvc, ObjectMapper objectMapper, 
                                        String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail(username);
        loginRequest.setPassword(password);

        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(responseContent, LoginResponse.class);
        
        if (!loginResponse.isSuccess()) {
            throw new RuntimeException("Login failed: " + loginResponse.getMessage());
        }
        
        return loginResponse.getToken();
    }

    /**
     * Create Authorization header value with Bearer token.
     *
     * @param token JWT token
     * @return Authorization header value
     */
    public static String createAuthHeader(String token) {
        return "Bearer " + token;
    }
}
