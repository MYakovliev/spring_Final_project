package by.bntu.fitr.springtry.repository;

import by.bntu.fitr.springtry.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
}