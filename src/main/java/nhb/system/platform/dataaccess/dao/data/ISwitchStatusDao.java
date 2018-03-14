package nhb.system.platform.dataaccess.dao.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.data.SwitchStatus;

@Repository
public interface ISwitchStatusDao extends MongoRepository<SwitchStatus, String> {

}
