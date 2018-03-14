package nhb.system.platform.dataaccess.service.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import nhb.system.platform.dataaccess.dao.device.IReceiptMeterDao;
import nhb.system.platform.entity.device.ReceiptMeter;

import java.util.List;

@Service
public class ReceiptMeterService {

    @Autowired
    private IReceiptMeterDao receiptMeterDao;

    /**
     * @return Page<ReceiptMeter>
     * @Title: findByCollectorId
     * @Description: 根据采集器id查询其下属所有的电表
     */
    public Page<ReceiptMeter> findByCollectorId(String collectorId, Pageable pageable) {
        return receiptMeterDao.findByCollectorId(collectorId, pageable);
    }

    public ReceiptMeter saveOrUpdate(ReceiptMeter receiptMeter) {
        return receiptMeterDao.save(receiptMeter);
    }

    public void delete(String id) {
        receiptMeterDao.deleteById(id);
    }

    public List<ReceiptMeter> findByCollectorIdIn(List<String> collectorIds) {
        return receiptMeterDao.findByCollectorIdIn(collectorIds);
    }
}
