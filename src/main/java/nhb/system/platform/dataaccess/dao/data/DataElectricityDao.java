/** 
 * Project Name:nhb-platform 
 * File Name:DataElectricityDao.java 
 * Package Name:nhb.system.platform.dataaccess.dao.data 
 * Date:2017年9月21日下午4:55:28 
 * Copyright (c) 2017, lorisun@live.com All Rights Reserved. 
 * 
*/

package nhb.system.platform.dataaccess.dao.data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nhb.system.platform.entity.data.DataElectricity;

/**
 * @ClassName:DataElectricityDao
 * @Function: TODO ADD FUNCTION.
 * @Reason: TODO ADD REASON.
 * @Date: 2017年9月21日 下午4:55:28
 * @author xuyahui
 * @version
 * @since JDK 1.7
 * @see
 */
@Repository
public interface DataElectricityDao extends MongoRepository<DataElectricity, UUID> {

	Page<DataElectricity> findByReadTimeBetweenAndDeviceId(Date startDate, Date endDate, String deviceId,
			Pageable pageable);

	Page<DataElectricity> findByReadTimeBetweenAndDeviceIdIn(Date startDate, Date endDate, List<String> deviceId,
			Pageable pageable);

	List<DataElectricity> findByReadTimeBetweenOrderByReadTimeDesc(Date startTime, Date endTime);

	List<DataElectricity> findByReadTimeBetweenAndDeviceIdInOrderByReadTimeDesc(Date startDate, Date endDate,
			List<String> deviceIds);

	void deleteByReadTimeLessThan(Date dateTime);

}
