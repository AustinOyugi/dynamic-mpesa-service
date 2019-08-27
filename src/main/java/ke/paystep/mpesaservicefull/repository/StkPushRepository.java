package ke.paystep.mpesaservicefull.repository;

import ke.paystep.mpesaservicefull.model.StkPushModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StkPushRepository extends CrudRepository<StkPushModel, Long>
{
    Optional<StkPushModel> getByCheckoutRequestID(String checkoutRequestID);
}
