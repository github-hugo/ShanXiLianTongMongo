package nhb.system.platform.dataaccess.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nhb.system.platform.dataaccess.dao.system.ISysUserMenuDao;
import nhb.system.platform.entity.system.SysUserMenu;

import java.util.Optional;

@Service
public class SysUserMenuService {
	
	@Autowired
	private ISysUserMenuDao sysUserMenuDao;
	
	public SysUserMenu findByUserId(String userId){
		if (sysUserMenuDao.findById(userId).equals(Optional.empty())) {
			return null;
		}
		return sysUserMenuDao.findById(userId).get();
	}
	
	public SysUserMenu saveOrUpdate(SysUserMenu sysUserMenu){
		return sysUserMenuDao.save(sysUserMenu);
	}
	
}
