package by.bntu.fitr.springtry.repository;

import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface LotRepository extends PagingAndSortingRepository<Lot, Long> {
    Page<Lot> findByNameLike(String name, Pageable pageable);

    Page<Lot> findByBuyer(User buyer, Pageable pageable);

    Page<Lot> findBySeller(User seller, Pageable pageable);

    Page<Lot> findByFinishTimeAfter(Timestamp now, Pageable pageable);
}
