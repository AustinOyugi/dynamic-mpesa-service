package ke.paystep.mpesaservicefull.repository;

import ke.paystep.mpesaservicefull.model.C2BModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface C2BRespository extends CrudRepository<C2BModel, Long>
{
    @Query("SELECT COUNT(u.id) from C2BModel u where u.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    Optional<C2BModel> getByTransID(String transId);
}
