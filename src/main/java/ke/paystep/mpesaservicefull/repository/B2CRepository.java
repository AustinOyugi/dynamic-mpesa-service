package ke.paystep.mpesaservicefull.repository;

import ke.paystep.mpesaservicefull.model.B2CModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface B2CRepository extends CrudRepository<B2CModel ,Long>
{
    @Query("SELECT COUNT(u.id) from B2CModel u where u.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    Optional<B2CModel> findByConversationID(String conversationID);

}
