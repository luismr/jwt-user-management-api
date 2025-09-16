package com.example.login.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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

    @Test
    @WithMockUser(roles = "ADMIN")
    void testApiRootEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api"))
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
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsersEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.users").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllClientsEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.clients").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllRolesEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.roles").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUserRolesEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/user-roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.userRoles").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllClientRolesEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/client-roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.clientRoles").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllLoginLogsEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/logs-logins"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.logsLogins").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPaginationParameters() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.size", is(2)))
                .andExpect(jsonPath("$.page.number", is(0)))
                .andExpect(jsonPath("$._embedded.users", hasSize(lessThanOrEqualTo(2))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSortingParameters() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/users")
                .param("sort", "username,asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.users").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUsersSearchEndpoints() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/users/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links['by-username']").exists())
                .andExpect(jsonPath("$._links['by-email']").exists())
                .andExpect(jsonPath("$._links['by-client']").exists())
                .andExpect(jsonPath("$._links['by-status']").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testClientsSearchEndpoints() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/clients/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links['by-external-id']").exists())
                .andExpect(jsonPath("$._links['by-name']").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRolesSearchEndpoints() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/roles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links['by-description']").exists())
                .andExpect(jsonPath("$._links['by-internal']").exists())
                .andExpect(jsonPath("$._links['external']").exists())
                .andExpect(jsonPath("$._links['internal']").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUserRolesSearchEndpoints() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/user-roles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links['by-user']").exists())
                .andExpect(jsonPath("$._links['by-client']").exists())
                .andExpect(jsonPath("$._links['by-role']").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testClientRolesSearchEndpoints() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/client-roles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links['by-client']").exists())
                .andExpect(jsonPath("$._links['by-role']").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testLoginLogsSearchEndpoints() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/logs-logins/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links['by-user']").exists())
                .andExpect(jsonPath("$._links['by-type']").exists())
                .andExpect(jsonPath("$._links['recent']").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSpecificUserById() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test with user ID 1 (assuming it exists from the logs)
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSpecificClientById() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test with client ID 1 (assuming it exists from the logs)
        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.externalId").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSpecificRoleById() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test with role ID 1 (assuming it exists from the logs)
        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.internal").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSearchUserByUsername() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test searching for 'admin' user (from the logs)
        mockMvc.perform(get("/api/users/search/by-username")
                .param("username", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.username", is("admin")))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSearchClientByExternalId() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test searching for client by external ID (from the logs)
        mockMvc.perform(get("/api/clients/search/by-external-id")
                .param("externalId", "SEARS-HOME-SERVICES"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.externalId", is("SEARS-HOME-SERVICES")))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSearchRoleByDescription() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test searching for role by description (from the logs)
        mockMvc.perform(get("/api/roles/search/by-description")
                .param("description", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.description", is("ROLE_ADMIN")))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testNotFoundResource() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/users/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testInvalidEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testProfileEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testContentNegotiation() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/users")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testLargePage() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/users")
                .param("size", "100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.page.size", is(100)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetInternalRoles() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/roles/search/internal"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.roles").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetExternalRoles() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/api/roles/search/external"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.roles").isArray());
    }

    @Test
    void testLoginEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testLogoutEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/logout"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testSwaggerUiAccess() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void testApiDocsAccess() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    void testApiEndpointsRequireAuthentication() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testApiEndpointsRequireAdminRole() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }
}