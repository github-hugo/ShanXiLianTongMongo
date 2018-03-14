/** 
 * Project Name:nhb-platform 
 * File Name:DataWaterService.java 
 * Package Name:nhb.system.platform.dataaccess.service.data 
 * Date:2017年9月22日下午5:24:19 
 * Copyright (c) 2017, xyh@newhongbo.com All Rights Reserved. 
 * 
*/

package nhb.system.platform.dataaccess.service.data;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import nhb.system.platform.dataaccess.dao.data.DataWaterDao;
import nhb.system.platform.entity.data.DataWater;

/**
 * @ClassName:DataWaterService
 * @Function: TODO ADD FUNCTION.
 * @Reason: TODO ADD REASON.
 * @Date: 2017年9月22日 下午5:24:19
 * @author Administrator
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
public class DataWaterService {
	@Autowired
	private DataWaterDao dateWaterDao;

	public Page<DataWater> findByReadTimeBetweenAndDeviceId(Date startDate, Date endDate, String deviceId,
			Pageable pageable) {
		return dateWaterDao.findByReadTimeBetweenAndDeviceId(startDate, endDate, deviceId, pageable);
	}

	public Page<DataWater> findByReadTimeBetweenAndDeviceIdIn(Date startDate, Date endDate, List<String> deviceId,
			Pageable pageable) {
		return dateWaterDao.findByReadTimeBetweenAndDeviceIdIn(startDate, endDate, deviceId, pageable);
	}

	public List<DataWater> findByReadTimeBetweenOrderByReadTimeDesc(Date startTime, Date endTime) {
		return dateWaterDao.findByReadTimeBetweenOrderByReadTimeDesc(startTime, endTime);
	}

	public List<DataWater> findByReadTimeBetweenAndDeviceIdInOrderByReadTimeDesc(Date startDate, Date endDate,
			List<String> deviceIds)
	{
		return dateWaterDao.findByReadTimeBetweenAndDeviceIdInOrderByReadTimeDesc(startDate,endDate,deviceIds);
	}
}
