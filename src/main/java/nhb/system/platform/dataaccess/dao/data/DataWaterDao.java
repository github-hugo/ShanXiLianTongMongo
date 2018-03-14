/** 
 * Project Name:nhb-platform 
 * File Name:DataWaterDao.java 
 * Package Name:nhb.system.platform.dataaccess.dao.data 
 * Date:2017年9月22日下午5:26:04 
 * Copyright (c) 2017, xyh@newhongbo.com All Rights Reserved. 
 * 
*/

package nhb.system.platform.dataaccess.dao.data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import nhb.system.platform.entity.data.DataWater;

/**
 * @ClassName:DataWaterDao
 * @Function: TODO ADD FUNCTION.
 * @Reason: TODO ADD REASON.
 * @Date: 2017年9月22日 下午5:26:04
 * @author Administrator
 * @version
 * @since JDK 1.8
 * @see
 */
public interface DataWaterDao extends MongoRepository<DataWater, UUID> {

	Page<DataWater> findByReadTimeBetweenAndDeviceId(Date startDate, Date endDate, String deviceId, Pageable pageable);

	Page<DataWater> findByReadTimeBetweenAndDeviceIdIn(Date startDate, Date endDate, List<String> deviceId,
			Pageable pageable);

	List<DataWater> findByReadTimeBetweenOrderByReadTimeDesc(Date startTime, Date endTime);

	List<DataWater> findByReadTimeBetweenAndDeviceIdInOrderByReadTimeDesc(Date startDate, Date endDate,
			List<String> deviceIds);

}
