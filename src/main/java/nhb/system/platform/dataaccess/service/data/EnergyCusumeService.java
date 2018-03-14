package nhb.system.platform.dataaccess.service.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nhb.system.platform.dataaccess.dao.data.IEnergyCusumeDao;
import nhb.system.platform.entity.data.EnergyCusume;

@Service
public class EnergyCusumeService {

	@Autowired
	private IEnergyCusumeDao energyCusumeDao;

	public void saveList(List<EnergyCusume> list) {
		energyCusumeDao.saveAll(list);
	}

	public List<EnergyCusume> findByDeviceIdInAndDateBetween(List<String> deviceIds, String startDate, String endDate) {
		return energyCusumeDao.findByDeviceIdInAndDateBetween(deviceIds, startDate, endDate);
	}

	public List<EnergyCusume> findByDeviceIdAndDateBetween(String deviceId, String startDate, String endDate) {
		return energyCusumeDao.findByDeviceIdAndDateBetween(deviceId, startDate, endDate);
	}

	public void deleteByDateBetweenAndDeviceIdIn(String startTime, String endTime, List<String> deviceIds) {
		energyCusumeDao.deleteByDateBetweenAndDeviceIdIn(startTime, endTime, deviceIds);
	}
}
