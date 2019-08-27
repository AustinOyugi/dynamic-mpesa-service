package ke.paystep.mpesaservicefull.repository;

import ke.paystep.mpesaservicefull.model.B2CModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface B2CRepository extends CrudRepository<B2CModel ,Long> {
}
