package nhb.system.platform.dataaccess.service.device;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import nhb.system.platform.dataaccess.dao.device.IReceiptCollectorDao;
import nhb.system.platform.entity.device.ReceiptCollector;

@Service
public class ReceiptCollectorService {

    @Autowired
    private IReceiptCollectorDao receiptCollectorDao;

    /**
     * @return ReceiptCollector
     * @Title: save
     * @Description: 新增或修改
     */
    public ReceiptCollector saveOrUpdate(ReceiptCollector collector) {
        return receiptCollectorDao.save(collector);
    }

    /**
     * @return void
     * @Title: delete
     * @Description: 删除
     */
    public void delete(String collectorId) {
        receiptCollectorDao.deleteById(collectorId);
    }

    /**
     * @return Page<ReceiptCollector>
     * @Title: findByUserId
     * @Description: 根据userId查询其下属所有的采集器
     */
    public Page<ReceiptCollector> findByManagerId(String managerId, Pageable pageable) {
        return receiptCollectorDao.findByManagerId(managerId, pageable);
    }

    public ReceiptCollector findByCollectorNo(String collectorNo) {
        return receiptCollectorDao.findByCollectorNo(collectorNo);
    }

    public List<ReceiptCollector> findByIds(List<String> collectorIds) {
        return Lists.newArrayList(receiptCollectorDao.findAllById(collectorIds));
    }

    public Page<ReceiptCollector> findAll(Pageable pageable) {
        return receiptCollectorDao.findAll(pageable);
    }

    public List<ReceiptCollector> findAll() {
        return receiptCollectorDao.findAll();
    }

    public Page<ReceiptCollector> findByManagerIdIsNull(Pageable pageable) {
        return receiptCollectorDao.findByManagerIdIsNull(pageable);
    }

    public long totalCounts() {
        return receiptCollectorDao.count();
    }

    public long countSbyManagerId(String managerId) {
        ReceiptCollector collector = new ReceiptCollector();
        collector.setManagerId(managerId);
        Example<ReceiptCollector> example = Example.of(collector);
        return receiptCollectorDao.count(example);
    }

    public List<ReceiptCollector> findAllByManagerId(String userId) {
        return receiptCollectorDao.findByManagerId(userId);
    }
}
