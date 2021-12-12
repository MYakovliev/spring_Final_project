package by.bntu.fitr.springtry.controller;

import by.bntu.fitr.springtry.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void banUser() throws Exception {
        mockMvc.perform(
                get("/ban/1")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }

    @Test
    void unbanUser() throws Exception {
        mockMvc.perform(
                get("/unban/1")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }

    @Test
    void toAdmin() throws Exception {
        mockMvc.perform(
                get("/admin")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }

    @Test
    void submitWinner() throws Exception {
        mockMvc.perform(
                get("/lot/1/submit")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }
}