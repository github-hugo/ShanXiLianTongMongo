package nhb.system.platform.dataaccess.service.system;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nhb.system.platform.dataaccess.dao.system.UserCodeMapDao;
import nhb.system.platform.entity.system.UserCodeMap;

@Service
public class UserCodeMapService {

	@Autowired
	private UserCodeMapDao userCodeMapDao;

	public UserCodeMap findById(String userCode) {
		if (userCodeMapDao.findById(userCode).equals(Optional.empty())) {
			return null;
		}
		return userCodeMapDao.findById(userCode).get();
	}

	public UserCodeMap save(UserCodeMap map) {
		return userCodeMapDao.save(map);
	}

}
