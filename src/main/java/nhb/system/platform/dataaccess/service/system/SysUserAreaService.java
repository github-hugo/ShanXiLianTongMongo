package nhb.system.platform.dataaccess.service.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nhb.system.platform.dataaccess.dao.system.ISysUserAreaDao;
import nhb.system.platform.entity.system.SysUserArea;

@Service
public class SysUserAreaService {

    @Autowired
    private ISysUserAreaDao sysUserAreaDao;

    public SysUserArea save(SysUserArea sysUserArea) {
        return sysUserAreaDao.save(sysUserArea);
    }

    public List<SysUserArea> saveList(List<SysUserArea> userAreas) {
        return sysUserAreaDao.saveAll(userAreas);
    }

    public List<SysUserArea> findByUserId(String userId) {
        return sysUserAreaDao.findByUserId(userId);
    }

    public void deleteByUserId(String userId) {
        sysUserAreaDao.deleteByUserId(userId);
    }

    public List<SysUserArea> findByAreaIdIn(List<String> areaIds) {
        return sysUserAreaDao.findByAreaIdIn(areaIds);
    }

    public List<SysUserArea> findAll() {
        return sysUserAreaDao.findAll();
    }

    public List<SysUserArea> findByAreaId(String areaId) {
        return sysUserAreaDao.findByAreaId(areaId);
    }
}
