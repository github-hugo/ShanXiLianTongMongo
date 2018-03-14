package nhb.system.platform.dataaccess.service.device;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nhb.system.platform.dataaccess.dao.device.IUtilProtocolTypeDao;
import nhb.system.platform.entity.device.UtilProtocolType;

@Service
public class UtilProtocolTypeService {

	@Autowired
	private IUtilProtocolTypeDao utilProtocolTypeDao;

	public List<UtilProtocolType> findAll() {
		return utilProtocolTypeDao.findAll();
	}

}
