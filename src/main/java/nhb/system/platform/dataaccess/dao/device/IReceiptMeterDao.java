package nhb.system.platform.dataaccess.dao.device;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.device.ReceiptMeter;

import java.util.List;

/**
 * 
 * @ClassName: IReceiptCollectorDao
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author XS guo
 * @date 2017年9月19日 下午2:14:39
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Repository
public interface IReceiptMeterDao extends MongoRepository<ReceiptMeter, String> {

	Page<ReceiptMeter> findByCollectorId(String collectorId, Pageable pageable);

    List<ReceiptMeter> findByCollectorIdIn(List<String> collectorIds);
}
