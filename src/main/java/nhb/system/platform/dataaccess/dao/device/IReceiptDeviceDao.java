package nhb.system.platform.dataaccess.dao.device;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.device.ReceiptDevice;

import java.util.List;

/**
 * 
 * @ClassName: IDeviceDao
 * @Description: 设备Dao接口
 * @author XS guo
 * @date 2017年9月20日 上午10:54:00
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Repository
public interface IReceiptDeviceDao extends MongoRepository<ReceiptDevice, String> {

	Page<ReceiptDevice> findByMeterId(String meterId, Pageable pageable);

    List<ReceiptDevice> findByMeterIdIn(List<String> meterIds);
}
