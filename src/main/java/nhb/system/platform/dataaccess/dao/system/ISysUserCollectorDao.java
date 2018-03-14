package nhb.system.platform.dataaccess.dao.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.system.SysUserCollector;

@Repository
public interface ISysUserCollectorDao extends MongoRepository<SysUserCollector, String> {

	Page<SysUserCollector> findByUserId(String userId, Pageable pageable);

	void deleteByUserId(String userId);

	List<SysUserCollector> findByUserId(String userId);

	List<SysUserCollector> findByCollectorId(String collectorId);
}
