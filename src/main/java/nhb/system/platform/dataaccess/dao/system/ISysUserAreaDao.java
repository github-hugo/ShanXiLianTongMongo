package nhb.system.platform.dataaccess.dao.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.system.SysUserArea;

/**
 * 
 * @ClassName: ISysUserDao
 * @Description: User Dao层
 * @author XS guo
 * @date 2017年9月15日 下午4:43:14
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Repository
public interface ISysUserAreaDao extends MongoRepository<SysUserArea, String> {

	/**
	 * 
	 * @Title: findByUserId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return List<SysUserArea>
	 */
	List<SysUserArea> findByUserId(String userId);

	/**
	 * 
	 * @Title: deleteByUserId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return void
	 */
	void deleteByUserId(String userId);

	Page<SysUserArea> findByUserId(String userId, Pageable pageable);

	List<SysUserArea> findByAreaIdIn(List<String> areaIds);

	List<SysUserArea> findByAreaId(String areaId);
}
