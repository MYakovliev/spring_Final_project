package by.bntu.fitr.springtry.repository;

import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface LotRepository extends PagingAndSortingRepository<Lot, Long> {
    Page<Lot> findByNameLike(String name, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT DISTINCT lots.* FROM lots INNER JOIN bid_history ON id_lot=idlots WHERE id_buyer=:buyer")
    Page<Lot> findByBuyer(long buyer, Pageable pageable);

    Page<Lot> findBySeller(User seller, Pageable pageable);

    Page<Lot> findByFinishTimeAfter(Timestamp now, Pageable pageable);
}
