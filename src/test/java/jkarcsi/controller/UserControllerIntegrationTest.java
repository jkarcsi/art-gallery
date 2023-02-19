package jkarcsi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import jkarcsi.dto.user.AppUserRole;
import jkarcsi.dto.user.UserData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public static final String USERS_BASE_PATH = "/users";
    public static final String GALLERY_BASE_PATH = "/artworks";
    public static final String LOGIN_PATH = "signin";
    public static final String SIGNUP_PATH = "signup";
    public static final String REFRESH_PATH = "refresh";
    public static final String WHOAMI_PATH = "me";
    public static final String TEST_CLIENT_USER = "user1@email.com";
    public static final String TEST_PASSWORD = "password";
    public static final String NEW_TEST_USER = "user3@email.com";
    public static final String NEWER_TEST_USER = "user4@email.com";
    public static final String SIMPLE_ARTWORK_ID = "129884";

    @Test
    void test_login_appropriateData_ok() throws Exception {
        mockMvc.perform(post(String.format("%s/%s", USERS_BASE_PATH, LOGIN_PATH)).param("username", TEST_CLIENT_USER)
                .param("password", TEST_PASSWORD)).andExpect(status().isOk());

    }

    @Test
    void test_login_withoutUsernameOrPassword_forbidden() throws Exception {
        mockMvc.perform(get(String.format("%s/%s", USERS_BASE_PATH, LOGIN_PATH)).param("Username", TEST_CLIENT_USER))
                .andExpect(status().isForbidden());
        mockMvc.perform(get(String.format("%s/%s", USERS_BASE_PATH, LOGIN_PATH)).param("Username", TEST_CLIENT_USER))
                .andExpect(status().isForbidden());
    }

    @Test
    void test_login_withWrongCredentials_unprocessableEntity() throws Exception {
        mockMvc.perform(post(String.format("%s/%s", USERS_BASE_PATH, LOGIN_PATH)).param("username", "user@email")
                .param("password", TEST_PASSWORD)).andExpect(status().isUnprocessableEntity());
        mockMvc.perform(post(String.format("%s/%s", USERS_BASE_PATH, LOGIN_PATH)).param("username", TEST_CLIENT_USER)
                .param("password", "pass")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void test_signup_appropriateData_ok() throws Exception {
        UserData userData = new UserData().setUsername(NEW_TEST_USER).setPassword(TEST_PASSWORD)
                .setAppUserRoles(new ArrayList<>(Collections.singletonList(AppUserRole.ROLE_CLIENT)));

        mockMvc.perform(post(String.format("%s/%s", USERS_BASE_PATH, SIGNUP_PATH)).content(
                        objectMapper.writeValueAsString(userData)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test_signup_withoutUsernameOrPassword_ok() throws Exception {
        UserData signup = new UserData().setUsername(NEWER_TEST_USER)
                .setAppUserRoles(new ArrayList<>(Collections.singletonList(AppUserRole.ROLE_CLIENT)));
        mockMvc.perform(post(String.format("%s/%s", USERS_BASE_PATH, SIGNUP_PATH)).content(
                        objectMapper.writeValueAsString(signup)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        UserData anotherSignup = new UserData().setPassword(TEST_PASSWORD)
                .setAppUserRoles(new ArrayList<>(Collections.singletonList(AppUserRole.ROLE_CLIENT)));
        mockMvc.perform(post(String.format("%s/%s", USERS_BASE_PATH, SIGNUP_PATH)).content(
                        objectMapper.writeValueAsString(anotherSignup)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void test_signup_usernameAlreadyTaken_ok() throws Exception {
        UserData userData = new UserData().setUsername(NEW_TEST_USER).setPassword(TEST_PASSWORD)
                .setAppUserRoles(new ArrayList<>(Collections.singletonList(AppUserRole.ROLE_CLIENT)));
        mockMvc.perform(post(String.format("%s/%s", USERS_BASE_PATH, SIGNUP_PATH)).content(
                        objectMapper.writeValueAsString(userData)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(post(String.format("%s/%s", USERS_BASE_PATH, SIGNUP_PATH)).content(
                        objectMapper.writeValueAsString(userData)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Nested
    @WithMockUser(roles = { "ADMIN", "CLIENT" })
    class UserControllerWithAnyRoleTest {

        @Test
        void test_delete_newUser_ok() throws Exception {
            UserData userData = new UserData().setUsername(NEWER_TEST_USER).setPassword(TEST_PASSWORD)
                    .setAppUserRoles(new ArrayList<>(Collections.singletonList(AppUserRole.ROLE_CLIENT)));
            mockMvc.perform(post(String.format("%s/%s", USERS_BASE_PATH, SIGNUP_PATH)).content(
                            objectMapper.writeValueAsString(userData)).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            mockMvc.perform(delete(String.format("%s/%s", USERS_BASE_PATH, NEWER_TEST_USER))).andExpect(status().isOk());
        }

        @Test
        void test_listOwnedArtworksByUser_newUser_ok() throws Exception {
            UserData userData = new UserData().setUsername(NEW_TEST_USER).setPassword(TEST_PASSWORD)
                    .setAppUserRoles(new ArrayList<>(Collections.singletonList(AppUserRole.ROLE_CLIENT)));

            mockMvc.perform(post(String.format("%s/%s", USERS_BASE_PATH, SIGNUP_PATH)).content(
                            objectMapper.writeValueAsString(userData)).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            mockMvc.perform(get(String.format("%s/%s", USERS_BASE_PATH, NEW_TEST_USER))).andExpect(status().isOk());

        }

        @Test
        void test_whoami_ok() throws Exception {
            final MvcResult mvcResult = mockMvc.perform(
                    post(String.format("%s/%s", USERS_BASE_PATH, LOGIN_PATH)).param("username", TEST_CLIENT_USER)
                            .param("password", TEST_PASSWORD)).andExpect(status().isOk()).andReturn();
            final String token = String.format("Bearer %s", mvcResult.getResponse().getContentAsString());
            final MockHttpServletRequest request = mvcResult.getRequest();

            mockMvc.perform(get(String.format("%s/%s", USERS_BASE_PATH, WHOAMI_PATH)).header("Authorization", token)
                    .param("req", String.valueOf(request))).andExpect(status().isOk());
        }

        @Test
        void test_refresh_ok() throws Exception {
            final MvcResult mvcResult = mockMvc.perform(
                    post(String.format("%s/%s", USERS_BASE_PATH, LOGIN_PATH)).param("username", TEST_CLIENT_USER)
                            .param("password", TEST_PASSWORD)).andExpect(status().isOk()).andReturn();
            final String token = String.format("Bearer %s", mvcResult.getResponse().getContentAsString());
            final MockHttpServletRequest request = mvcResult.getRequest();

            mockMvc.perform(get(String.format("%s/%s", USERS_BASE_PATH, REFRESH_PATH)).header("Authorization", token)
                    .param("req", String.valueOf(request))).andExpect(status().isOk());
        }
    }

    @Nested
    @WithMockUser(roles = "CLIENT")
    class UserControllerWithClientRoleTest {

        @Test
        void test_delete_newUser_forbidden() throws Exception {
            UserData userData = new UserData().setUsername(NEWER_TEST_USER).setPassword(TEST_PASSWORD)
                    .setAppUserRoles(new ArrayList<>(Collections.singletonList(AppUserRole.ROLE_CLIENT)));
            mockMvc.perform(post(String.format("%s/%s", USERS_BASE_PATH, SIGNUP_PATH)).content(
                            objectMapper.writeValueAsString(userData)).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            mockMvc.perform(delete(String.format("%s/%s", USERS_BASE_PATH, NEWER_TEST_USER))).andExpect(status().isForbidden());
        }

        @Test
        void test_listOwnedArtworksByUser_anyUser_forbidden() throws Exception {
            mockMvc.perform(get(String.format("%s/%s", USERS_BASE_PATH, NEW_TEST_USER))).andExpect(status().isForbidden());

        }

    }

    @Nested
    @WithMockUser(roles = "ADMIN")
    class UserControllerWithAdminRoleTest {

        @Test
        void test_purchaseArtwork_appropriateData_ok() throws Exception {
            mockMvc.perform(post(String.format("%s/%s", GALLERY_BASE_PATH, SIMPLE_ARTWORK_ID))).andExpect(status().isForbidden());
        }

    }

}