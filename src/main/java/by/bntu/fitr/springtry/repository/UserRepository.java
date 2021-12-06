package by.bntu.fitr.springtry.repository;

import by.bntu.fitr.springtry.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

    Page<User> findByNameLike(String name, Pageable pageable);
}