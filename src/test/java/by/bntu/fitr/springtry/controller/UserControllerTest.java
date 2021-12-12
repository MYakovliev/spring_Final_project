package by.bntu.fitr.springtry.controller;

import by.bntu.fitr.springtry.config.TestConfig;
import by.bntu.fitr.springtry.entity.*;
import by.bntu.fitr.springtry.util.JspPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private static final User seller = new User(2, "name2", "mail2@mail.ma", new BigDecimal("0.00"), UserRole.SELLER,
            "/img/default_image.png", false, "login2");
    private static final User buyer = new User(1, "name1", "mail1@mail.ma", new BigDecimal("0.00"), UserRole.BUYER,
            "/img/default_image.png", false, "login1");

    @BeforeEach
    void setUp() {
    }

    @Test
    void toLogin() throws Exception {
        mockMvc.perform(get("/login")
        ).andExpect(status().isOk()).andExpect(view().name("login"));
    }

    @Test
    void toRegistration() throws Exception {
        mockMvc.perform(get("/signup")
        ).andExpect(status().isOk()).andExpect(view().name(JspPath.REGISTRATION));
    }

    @Test
    void doLogin() throws Exception {
        mockMvc.perform(post("/do_signin").param("login", buyer.getLogin())
                .param("password", "password")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }

    @Test
    void doRegister() throws Exception {
        mockMvc.perform(post("/do_signup")
                .param("login", "login")
                .param("password", "password")
                .param("mail", buyer.getMail())
                .param("name", "name")
                .param("role", "buyer")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }

    @Test
    void doChangeUserData() throws Exception {
        mockMvc.perform(
                post("/do_change_user_data")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }

    @Test
    void doChangeUserPassword() throws Exception {
        mockMvc.perform(
                post("/do_change_user_password")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }

    @Test
    void doPay() throws Exception {
        mockMvc.perform(
                post("/do_pay")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }

    @Test
    void makeBid() throws Exception {
        mockMvc.perform(
                post("/make_bid")
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    void logout() throws Exception {
        mockMvc.perform(
                post("/logout")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    void changeLanguage() throws Exception {
        mockMvc.perform(
                get("/to_ban").param("lang", "ru")
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    void toBan() throws Exception {
        mockMvc.perform(
                get("/to_ban")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }

    @Test
    void toPayment() throws Exception {
        mockMvc.perform(
                get("/pay")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }

    @Test
    void toProfile() throws Exception {
        mockMvc.perform(
                get("/user/1")
        ).andExpect(status().isOk()).andExpect(model().attribute("user", buyer));
    }

    @Test
    void toUserEdit() throws Exception {
        mockMvc.perform(
                get("/user/edit")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }
}