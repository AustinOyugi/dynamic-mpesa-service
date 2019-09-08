package ke.paystep.mpesaservicefull.repository;

import ke.paystep.mpesaservicefull.model.StkPushModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StkPushRepository extends CrudRepository<StkPushModel, Long>
{
    Optional<StkPushModel> getByCheckoutRequestID(String checkoutRequestID);

    @Query("SELECT COUNT(u.id) from STK_Push u where u.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
}
