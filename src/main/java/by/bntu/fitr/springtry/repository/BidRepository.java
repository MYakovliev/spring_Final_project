package by.bntu.fitr.springtry.repository;

import by.bntu.fitr.springtry.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByIdLot(long idLot);
}