package org.study.wreview.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.study.wreview.config.WebSecurityConfig;
import org.study.wreview.services.PersonService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebSecurityConfig.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PersonService personService;

    @MockBean
    PasswordEncoder passwordEncoder;


    @Test
    public void loginPageTest() throws Exception {
        mockMvc
                .perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void registrationPageTest() throws Exception {
        mockMvc
                .perform(get("/registration").with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    public void accessDeniedTest() throws Exception {
        mockMvc
                .perform(get("/reviews"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void correctLoginTest() throws Exception {
        mockMvc
                .perform(formLogin()
                        .loginProcessingUrl("/process_login")
                        .user("user")
                        .password("1"))
                .andExpect(authenticated()
                        .withUsername("user")
                        .withRoles("USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}