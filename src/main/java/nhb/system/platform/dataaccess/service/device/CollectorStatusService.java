package nhb.system.platform.dataaccess.service.device;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nhb.system.platform.dataaccess.dao.device.ICollectorStatusDao;
import nhb.system.platform.entity.device.CollectorStatus;

@Service
public class CollectorStatusService {
	@Autowired
	private ICollectorStatusDao collectorStatusDao;
	
	public List<CollectorStatus> findByCollectorIdIn(String[] collectors) {
		return collectorStatusDao.findByCollectorIdIn(collectors);
	}
}
