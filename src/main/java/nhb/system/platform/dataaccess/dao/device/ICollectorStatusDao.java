package nhb.system.platform.dataaccess.dao.device;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.device.CollectorStatus;

@Repository
public interface ICollectorStatusDao extends MongoRepository<CollectorStatus, Long> {

	List<CollectorStatus> findByCollectorIdIn(String[] collectors);

}
