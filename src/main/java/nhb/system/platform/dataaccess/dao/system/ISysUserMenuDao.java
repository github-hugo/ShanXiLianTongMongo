package nhb.system.platform.dataaccess.dao.system;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.system.SysUserMenu;

@Repository
public interface ISysUserMenuDao extends MongoRepository<SysUserMenu, String>{
	
}
