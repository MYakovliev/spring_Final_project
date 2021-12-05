package by.bntu.fitr.springtry.repository;

import by.bntu.fitr.springtry.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
class BidRepositoryTest {
    @Autowired
    private BidRepository bidRepository;

    @Test
    void findByLotId() {
        System.out.println(bidRepository.findByIdLot(1));
    }
}
