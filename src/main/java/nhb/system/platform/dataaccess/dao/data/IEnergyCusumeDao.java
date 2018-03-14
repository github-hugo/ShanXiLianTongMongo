package nhb.system.platform.dataaccess.dao.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.data.EnergyCusume;

@Repository
public interface IEnergyCusumeDao extends MongoRepository<EnergyCusume, Long> {

	List<EnergyCusume> findByDeviceIdInAndDateBetween(List<String> deviceIds, String startDate, String endDate);

	List<EnergyCusume> findByDeviceIdAndDateBetween(String deviceId, String startDate, String endDate);

	void deleteByDateBetweenAndDeviceIdIn(String startTime, String endTime, List<String> deviceIds);
}
