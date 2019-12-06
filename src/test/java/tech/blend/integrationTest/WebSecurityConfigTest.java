package tech.blend.integrationTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tech.blend.Application;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This test class verify the valid login and url access with valid role.
 * Problem statement:
 * "/login": This url is accessible by every user with any role (ADMIN or USER).
 * "/home": This url is accessible by valid user with ADMIN role.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/testdb",
        "spring.datasource.username=",
        "spring.datasource.password=",
        "authentication.type=IN_MEMORY",
        "inMemory.csv.path=/Users/rameshkatiyar/IdeaProjects/app-one/src/test/java/tech/blend/testFiles/TestUserDetails.csv"
})
public class WebSecurityConfigTest {

    private final String LOGIN_PAGE_URL = "/login";
    private final String HOME_PAGE_URL = "/home";
    private final String ROLE_ADMIN = "ADMIN";
    private final String ROLE_USER = "USER";
    private final String USERNAME = "username";
    private final String PASSWORD = "pwd123";

    @Autowired
    private WebApplicationContext applicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLE_USER)
    @Test
    public void testLoginPageAccessibleByNormalUserRole() throws Exception {
        mockMvc.perform(get(LOGIN_PAGE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLE_ADMIN)
    @Test
    public void testLoginPageAccessibleByAdminRole() throws Exception {
        mockMvc.perform(get(LOGIN_PAGE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLE_ADMIN)
    @Test
    public void testHomePageAccessibleByAdminRole() throws Exception {
        mockMvc.perform(get(HOME_PAGE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLE_USER)
    @Test
    public void testHomePageIsNotAccessibleByNormalUserRole() throws Exception {
        mockMvc.perform(get(HOME_PAGE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
