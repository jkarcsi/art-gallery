package jkarcsi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@WithMockUser(roles = "CLIENT")
@TestWithResources
class GalleryControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @GivenTextResource("txt/ArtworkPageRawString.txt")
    String artworkPageString;

    public static final String GALLERY_BASE_PATH = "/artworks";
    public static final String USERS_BASE_PATH = "/users";
    public static final String SIMPLE_ARTWORK_ID = "129884";
    public static final String MISSING_ARTWORK_ID = "99999999";
    public static final String PAGE_LIMIT = "5";
    public static final String TEST_CLIENT_USER = "user1@email.com";

    @Test
    void test_retrieveArtwork_appropriateData_ok() throws Exception {
        mockMvc.perform(get(String.format("%s/%s", GALLERY_BASE_PATH, SIMPLE_ARTWORK_ID))).andExpect(status().isOk());
    }

    @Test
    void test_retrieveArtwork_missingArtwork_notFound() throws Exception {
        mockMvc.perform(get(String.format("%s/%s", GALLERY_BASE_PATH, MISSING_ARTWORK_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_retrievePaginatedArtworks_appropriateData_ok() throws Exception {
        mockMvc.perform(get(GALLERY_BASE_PATH).param("page", "1").param("limit", PAGE_LIMIT))
                .andExpect(status().isOk());
    }

    @Test
    void test_purchaseArtwork_appropriateData_ok() throws Exception {
        mockMvc.perform(post(String.format("%s/%s", GALLERY_BASE_PATH, SIMPLE_ARTWORK_ID))).andExpect(status().isOk());
    }

    @Test
    void test_purchaseArtwork_tryToPurchaseTwice_forbidden() throws Exception {
        mockMvc.perform(post(String.format("%s/%s", GALLERY_BASE_PATH, SIMPLE_ARTWORK_ID)))
                .andExpect(status().isOk()).andReturn();
        mockMvc.perform(post(String.format("%s/%s", GALLERY_BASE_PATH, SIMPLE_ARTWORK_ID)))
                .andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void test_listOwnedArtworksByUser_withClientRole_forbidden() throws Exception {
        mockMvc.perform(get(String.format("%s/%s", USERS_BASE_PATH, TEST_CLIENT_USER))).andExpect(status().isForbidden()).andReturn();
    }

}