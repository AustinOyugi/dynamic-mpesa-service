package ke.paystep.mpesaservicefull.repository;

import ke.paystep.mpesaservicefull.model.Role;
import ke.paystep.mpesaservicefull.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Austin Oyugi on 19/8/2019.
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    Optional<Role> findByName(RoleName roleName);
}
