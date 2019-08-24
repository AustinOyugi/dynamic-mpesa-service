package ke.paystep.mpesaservicefull.repository;

import ke.paystep.mpesaservicefull.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Austin Oyugi on 17/8/2019.
 */

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByEmailAddress(String email);
    Optional<User> findByUserNameOrEmailAddress(String username, String email);
    List<User> findByIdIn(List<Long> userIds);
    Optional<User> findByUserName(String username);
    Boolean existsByUserName(String username);
    Boolean existsByEmailAddress(String email);
}
