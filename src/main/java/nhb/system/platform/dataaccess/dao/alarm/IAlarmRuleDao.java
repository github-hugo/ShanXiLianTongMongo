package nhb.system.platform.dataaccess.dao.alarm;

import nhb.system.platform.entity.alarm.AlarmRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/11/7.
 */
@Repository
public interface IAlarmRuleDao extends MongoRepository<AlarmRule, String> {
}
