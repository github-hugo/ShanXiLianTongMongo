package nhb.system.platform.dataaccess.dao.system;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.system.UserCodeMap;

@Repository
public interface UserCodeMapDao extends MongoRepository<UserCodeMap, String> {

}
