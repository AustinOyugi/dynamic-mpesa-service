package ke.paystep.mpesaservicefull.repository;

import ke.paystep.mpesaservicefull.model.Users;
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
public interface UserRepository extends JpaRepository<Users, Long>
{
    Optional<Users> findByEmailAddress(String email);
    Optional<Users> findByUserNameOrEmailAddress(String username, String email);
    List<Users> findByIdIn(List<Long> userIds);
    Optional<Users> findByUserName(String username);
    Boolean existsByUserName(String username);
    Boolean existsByEmailAddress(String email);
}