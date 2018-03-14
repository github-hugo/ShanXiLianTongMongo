package nhb.system.platform.dataaccess.service.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import nhb.system.platform.dataaccess.dao.system.ISysUserCollectorDao;
import nhb.system.platform.entity.system.SysUserCollector;

@Service
public class SysUserCollectorService {

    @Autowired
    private ISysUserCollectorDao sysUserCollectorDao;

    public SysUserCollector save(SysUserCollector sysUserCollector) {
        return sysUserCollectorDao.save(sysUserCollector);
    }

    public List<SysUserCollector> saveList(List<SysUserCollector> sysUserCollector) {
        return sysUserCollectorDao.saveAll(sysUserCollector);
    }

    public Page<SysUserCollector> findByUserId(String userId, Pageable pageable) {
        return sysUserCollectorDao.findByUserId(userId, pageable);
    }

    public void deleteByUserId(String userId) {
        sysUserCollectorDao.deleteByUserId(userId);
    }

    public List<SysUserCollector> findByUserId(String userId) {
        return sysUserCollectorDao.findByUserId(userId);
    }

    public List<SysUserCollector> findByCollectorId(String collectorId) {
        return sysUserCollectorDao.findByCollectorId(collectorId);
    }
}
