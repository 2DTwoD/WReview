package org.study.wreview.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.study.wreview.config.WebSecurityConfig;
import org.study.wreview.services.PersonService;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Import(WebSecurityConfig.class)
@WebMvcTest(MainController.class)
class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PersonService personService;

    @Test
    void testMainPage() throws Exception {
        mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("persons"))
                .andExpect(model().attribute("persons", Collections.EMPTY_LIST))
                .andExpect(model().attribute("persons", Matchers.notNullValue()));
    }
}