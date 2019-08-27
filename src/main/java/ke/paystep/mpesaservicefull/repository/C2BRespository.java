package ke.paystep.mpesaservicefull.repository;

import ke.paystep.mpesaservicefull.model.C2BModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface C2BRespository extends CrudRepository<C2BModel, Long> {
}
