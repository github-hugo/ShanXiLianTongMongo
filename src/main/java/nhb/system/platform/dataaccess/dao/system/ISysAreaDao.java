package nhb.system.platform.dataaccess.dao.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.system.SysArea;

/**
 * 
 * @ClassName: ISysAreaDao
 * @Description: 区域Dao接口
 * @author XS guo
 * @date 2017年9月20日 上午10:51:04
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Repository
public interface ISysAreaDao extends MongoRepository<SysArea, String> {

	List<SysArea> findByParentId(String areaId);

	Page<SysArea> findByManagerIdAndIsCtrl(String userId, boolean isCtrl, Pageable pageable);

	Page<SysArea> findByIdInAndIsCtrl(String[] areaIds, boolean isCtrl, Pageable pageable);

	List<SysArea> findByManagerId(String userId);

	Page<SysArea> findByManagerIdAndSiteType(String ownerId, String siteType, Pageable pageable);

	Page<SysArea> findByIdInAndSiteType(List<String> areaIds, String siteType, Pageable pageable);

	List<SysArea> findByDeviceIdIn(List<String> deviceIds);

	List<SysArea> findByDeviceIdIsNotNull();

	Boolean existsByDeviceId(String deviceId);
}
