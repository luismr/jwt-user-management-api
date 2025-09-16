package com.example.login.integration;

import com.example.login.util.JwtTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("integration")
class RestEndpointsIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        
        // Login to get JWT token for tests
        jwtToken = JwtTestUtil.loginAndGetToken(mockMvc, objectMapper, "admin", "test123");
    }

    @Test
    void testApiRootEndpoint() throws Exception {
        mockMvc.perform(get("/api")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.users").exists())
                .andExpect(jsonPath("$._links.clients").exists())
                .andExpect(jsonPath("$._links.roles").exists())
                .andExpect(jsonPath("$._links.userRoles").exists())
                .andExpect(jsonPath("$._links.clientRoles").exists())
                .andExpect(jsonPath("$._links.logsLogins").exists())
                .andExpect(jsonPath("$._links.profile").exists());
    }

    @Test
    void testGetAllUsersEndpoint() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.users").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testGetAllClientsEndpoint() throws Exception {
        mockMvc.perform(get("/api/clients")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.clients").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testGetAllRolesEndpoint() throws Exception {
        mockMvc.perform(get("/api/roles")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.roles").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testGetAllUserRolesEndpoint() throws Exception {
        mockMvc.perform(get("/api/user-roles")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.userRoles").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testGetAllClientRolesEndpoint() throws Exception {
        mockMvc.perform(get("/api/client-roles")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.clientRoles").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testGetAllLoginLogsEndpoint() throws Exception {
        mockMvc.perform(get("/api/logs-logins")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.logsLogins").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testPaginationParameters() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "2")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.size", is(2)))
                .andExpect(jsonPath("$.page.number", is(0)))
                .andExpect(jsonPath("$._embedded.users", hasSize(lessThanOrEqualTo(2))));
    }

    @Test
    void testSortingParameters() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/users")
                .param("sort", "username,asc")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.users").isArray());
    }

    @Test
    void testUsersSearchEndpoints() throws Exception {
        mockMvc.perform(get("/api/users/search")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links['by-username']").exists())
                .andExpect(jsonPath("$._links['by-email']").exists())
                .andExpect(jsonPath("$._links['by-client']").exists())
                .andExpect(jsonPath("$._links['by-status']").exists());
    }

    @Test
    void testClientsSearchEndpoints() throws Exception {
        mockMvc.perform(get("/api/clients/search")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links['by-external-id']").exists())
                .andExpect(jsonPath("$._links['by-name']").exists());
    }

    @Test
    void testRolesSearchEndpoints() throws Exception {
        mockMvc.perform(get("/api/roles/search")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links['by-description']").exists())
                .andExpect(jsonPath("$._links['by-internal']").exists())
                .andExpect(jsonPath("$._links['external']").exists())
                .andExpect(jsonPath("$._links['internal']").exists());
    }

    @Test
    void testUserRolesSearchEndpoints() throws Exception {
        mockMvc.perform(get("/api/user-roles/search")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links['by-user']").exists())
                .andExpect(jsonPath("$._links['by-client']").exists())
                .andExpect(jsonPath("$._links['by-role']").exists());
    }

    @Test
    void testClientRolesSearchEndpoints() throws Exception {
        mockMvc.perform(get("/api/client-roles/search")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links['by-client']").exists())
                .andExpect(jsonPath("$._links['by-role']").exists());
    }

    @Test
    void testLoginLogsSearchEndpoints() throws Exception {
        mockMvc.perform(get("/api/logs-logins/search")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links['by-user']").exists())
                .andExpect(jsonPath("$._links['by-type']").exists())
                .andExpect(jsonPath("$._links['recent']").exists());
    }

    @Test
    void testSpecificUserById() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test with user ID 1 (assuming it exists from the logs)
        mockMvc.perform(get("/api/users/1")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testSpecificClientById() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test with client ID 1 (assuming it exists from the logs)
        mockMvc.perform(get("/api/clients/1")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.externalId").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testSpecificRoleById() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test with role ID 1 (assuming it exists from the logs)
        mockMvc.perform(get("/api/roles/1")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.internal").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testSearchUserByUsername() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test searching for 'admin' user (from the logs)
        mockMvc.perform(get("/api/users/search/by-username")
                .param("username", "admin")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.username", is("admin")))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testSearchClientByExternalId() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test searching for client by external ID (from the logs)
        mockMvc.perform(get("/api/clients/search/by-external-id")
                .param("externalId", "SEARS-HOME-SERVICES")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.externalId", is("SEARS-HOME-SERVICES")))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testSearchRoleByDescription() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test searching for role by description (from the logs)
        mockMvc.perform(get("/api/roles/search/by-description")
                .param("description", "ROLE_ADMIN")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.description", is("ROLE_ADMIN")))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testNotFoundResource() throws Exception {
        mockMvc.perform(get("/api/users/999999")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testInvalidEndpoint() throws Exception {
        mockMvc.perform(get("/api/nonexistent")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testProfileEndpoint() throws Exception {
        mockMvc.perform(get("/api/profile")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void testContentNegotiation() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/users")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void testLargePage() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/users")
                .param("size", "100")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.page.size", is(100)));
    }

    @Test
    void testGetInternalRoles() throws Exception {
        mockMvc.perform(get("/api/roles/search/internal")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.roles").isArray());
    }

    @Test
    void testGetExternalRoles() throws Exception {
        mockMvc.perform(get("/api/roles/search/external")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.roles").isArray());
    }

    @Test
    void testLoginEndpoint() throws Exception {
        mockMvc.perform(get("/login")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testLogoutEndpoint() throws Exception {
        mockMvc.perform(get("/logout")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testSwaggerUiAccess() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk());
    }

    @Test
    void testApiDocsAccess() throws Exception {
        mockMvc.perform(get("/v3/api-docs")
                .header("Authorization", JwtTestUtil.createAuthHeader(jwtToken)))
                .andExpect(status().isOk());
    }

    @Test
    void testApiEndpointsRequireAuthentication() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        
        // Test without Authorization header - should return 401
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testApiEndpointsRequireAdminRole() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        
        // Test with invalid token - should return 401
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }
}