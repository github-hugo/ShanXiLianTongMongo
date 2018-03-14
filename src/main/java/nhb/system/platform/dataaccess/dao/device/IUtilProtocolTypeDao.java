package nhb.system.platform.dataaccess.dao.device;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.device.UtilProtocolType;

@Repository
public interface IUtilProtocolTypeDao extends MongoRepository<UtilProtocolType, Integer> {

}
