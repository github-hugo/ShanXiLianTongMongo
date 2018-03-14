package nhb.system.platform.dataaccess.service.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import nhb.system.platform.dataaccess.dao.data.ISwitchStatusDao;
import nhb.system.platform.entity.data.SwitchStatus;

@Service
public class SwitchStatusService {
	@Autowired
	private ISwitchStatusDao switchStatusDao;

	public List<SwitchStatus> findByIds(List<String> deviceIds) {
		Iterable<SwitchStatus> geted = switchStatusDao.findAllById(deviceIds);
		List<SwitchStatus> list = Lists.newArrayList(geted);
		// geted.forEach(single -> {list.add(single);});
		return list;
	}

	public void saveOrUpdate(SwitchStatus switchStatus) {
		switchStatusDao.save(switchStatus);
	}

}
