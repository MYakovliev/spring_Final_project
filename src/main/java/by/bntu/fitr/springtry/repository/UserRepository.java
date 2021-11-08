package by.bntu.fitr.springtry.repository;

import by.bntu.fitr.springtry.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

    List<User> findByNameLike(String name, Pageable pageable);
}