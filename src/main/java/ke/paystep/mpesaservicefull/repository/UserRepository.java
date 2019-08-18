package ke.paystep.mpesaservicefull.repository;

import ke.paystep.mpesaservicefull.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
