package by.bntu.fitr.springtry.controller;

import by.bntu.fitr.springtry.config.TestConfig;
import by.bntu.fitr.springtry.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
class LotControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private Calendar calendar = Calendar.getInstance();
    private Lot lot1 = new Lot(1, "name1", "desc1", null, null, new BigDecimal("20.21"), null, new ArrayList<>());
    private Lot lot2 = new Lot(2, "name2", "desc2", null, null, new BigDecimal("20.45"), null, new ArrayList<>());
    private static final User seller = new User(2, "name2", "mail2@mail.ma", new BigDecimal("0.00"), UserRole.SELLER,
            "/img/default_image.png", false, "login2");
    private static final User buyer = new User(1, "name1", "mail1@mail.ma", new BigDecimal("0.00"), UserRole.BUYER,
            "/img/default_image.png", false, "login1");

    @BeforeEach
    void setUp() {
        calendar.set(2020, Calendar.JANUARY, 1, 0, 0, 0);
        Timestamp start = new Timestamp(calendar.getTime().getTime());
        start.setNanos(0);
        lot1.setStartTime(start);
        lot2.setStartTime(start);
        calendar.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        Timestamp finish = new Timestamp(calendar.getTime().getTime());
        finish.setNanos(0);
        lot1.setFinishTime(finish);
        lot2.setFinishTime(finish);
        User seller = new User(2, "name2", "mail2@mail.ma", new BigDecimal("0.00"), UserRole.SELLER,
                "/img/default_image.png", false, "login2");
        lot1.setSeller(seller);
        lot2.setSeller(seller);
        List<Bid> bidHistory = new ArrayList<>();
        lot2.setBidHistory(bidHistory);
        Bid bid1 = new Bid(1, buyer, new BigDecimal("20.45"), Status.LOSE);
        Bid bid2 = new Bid(2, buyer, new BigDecimal("21.50"), Status.WINING);
        bidHistory.add(bid1);
        bidHistory.add(bid2);
        lot1.setBidHistory(bidHistory);
    }

    @Test
    void toMainFindActive() throws Exception{
        mockMvc.perform(
                get("/main")
        ).andExpect(status().isOk())
                .andExpect(model().attribute("lot_list", Arrays.asList(lot1, lot2)));
    }

    @Test
    void toMainFindByName() throws Exception{
        mockMvc.perform(
                        get("/main").param("search", "milki")
                ).andExpect(status().isOk())
                .andExpect(model().attribute("lot_list", new ArrayList<>()));
    }

    @Test
    void addLot() throws Exception{
        mockMvc.perform(
                post("/new_lot")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }

    @Test
    void findLot() throws Exception {
        mockMvc.perform(
                        get("/lot/1")
                ).andExpect(status().isOk())
                .andExpect(model().attribute("lot", lot1));
    }

    @Test
    void toLotEdit() throws Exception {
        mockMvc.perform(
                get("/lot/edit")
        ).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/main"));
    }
}