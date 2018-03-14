package nhb.system.platform.dataaccess.service.system;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import nhb.system.platform.dataaccess.dao.system.ISysAreaDao;
import nhb.system.platform.entity.system.SysArea;

@Service
public class SysAreaService {

    @Autowired
    private ISysAreaDao sysAreaDao;

    /**
     * @return SysArea
     * @Title: save
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public SysArea save(SysArea sysArea) {
        return sysAreaDao.save(sysArea);
    }

    /**
     * @return SysArea
     * @Title: findById
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public SysArea findById(String areaId) {
        if (sysAreaDao.findById(areaId).equals(Optional.empty())) {
            return null;
        }
        return sysAreaDao.findById(areaId).get();
    }

    public List<SysArea> findByParentId(String areaId) {
        return sysAreaDao.findByParentId(areaId);
    }

    public void deleteById(String areaId) {
        sysAreaDao.deleteById(areaId);
    }

    public Page<SysArea> findByManagerIdAndIsCtrl(String userId, boolean isCtrl, Pageable pageable) {
        return sysAreaDao.findByManagerIdAndIsCtrl(userId, isCtrl, pageable);
    }

    public List<SysArea> findByIds(List<String> areaIds) {
        return Lists.newArrayList(sysAreaDao.findAllById(areaIds));
    }

    public Page<SysArea> findByIdsAndIsCtrl(String[] areaIds, boolean isCtrl, Pageable pageable) {
        return sysAreaDao.findByIdInAndIsCtrl(areaIds, isCtrl, pageable);
    }

    public List<SysArea> findAll() {
        return sysAreaDao.findAll();
    }

    public List<SysArea> findByManagerId(String userId) {
        return sysAreaDao.findByManagerId(userId);
    }

    public SysArea get(String id) {
        return sysAreaDao.findById(id).get();
    }

    public Page<SysArea> findByManagerIdAndSiteType(String ownerId, String siteType, Pageable pageable) {
        return sysAreaDao.findByManagerIdAndSiteType(ownerId, siteType, pageable);
    }

    public Page<SysArea> findByIdInAndSiteType(List<String> areaIds, String siteType, Pageable pageable) {
        return sysAreaDao.findByIdInAndSiteType(areaIds, siteType, pageable);
    }

    public List<SysArea> findByDeviceIdIn(List<String> deviceIds) {
        return sysAreaDao.findByDeviceIdIn(deviceIds);
    }

    public List<SysArea> findByDeviceIdIsNotNull() {
        return sysAreaDao.findByDeviceIdIsNotNull();
    }

    public Boolean existsByDeviceId(String deviceId) {
        return sysAreaDao.existsByDeviceId(deviceId);
    }
}
