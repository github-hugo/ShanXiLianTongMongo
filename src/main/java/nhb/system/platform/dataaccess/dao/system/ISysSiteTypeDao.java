package nhb.system.platform.dataaccess.dao.system;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.system.SysSiteType;

@Repository
public interface ISysSiteTypeDao extends MongoRepository<SysSiteType, String> {

	List<SysSiteType> findByUserId(String userId);

	List<SysSiteType> findBySiteCodeAndUserId(String siteTypeCode, String userId);

}
