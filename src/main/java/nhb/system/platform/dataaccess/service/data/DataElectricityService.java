/** 
 * Project Name:nhb-platform 
 * File Name:DataElectricityService.java 
 * Package Name:nhb.system.platform.dataaccess.service.data 
 * Date:2017年9月21日下午4:53:20 
 * Copyright (c) 2017, lorisun@live.com All Rights Reserved. 
 * 
*/

package nhb.system.platform.dataaccess.service.data;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import nhb.system.platform.dataaccess.dao.data.DataElectricityDao;
import nhb.system.platform.entity.data.DataElectricity;

/**
 * @ClassName:DataElectricityService
 * @Function: TODO ADD FUNCTION.
 * @Reason: TODO ADD REASON.
 * @Date: 2017年9月21日 下午4:53:20
 * @author Administrator
 * @version
 * @since JDK 1.7
 * @see
 */
@Service
public class DataElectricityService {

	@Autowired
	private DataElectricityDao dataElectricityDao;

	public Page<DataElectricity> findByReadTimeBetweenAndDeviceId(Date startDate, Date endDate, String deviceId,
			Pageable pageable) {
		return dataElectricityDao.findByReadTimeBetweenAndDeviceId(startDate, endDate, deviceId, pageable);
	}

	public Page<DataElectricity> findByReadTimeBetweenAndDeviceIdIn(Date startDate, Date endDate, List<String> deviceId,
			Pageable pageable) {
		return dataElectricityDao.findByReadTimeBetweenAndDeviceIdIn(startDate, endDate, deviceId, pageable);
	}

	public List<DataElectricity> findByReadTimeBetweenOrderByReadTimeDesc(Date startTime, Date endTime) {
		return dataElectricityDao.findByReadTimeBetweenOrderByReadTimeDesc(startTime, endTime);
	}

	public List<DataElectricity> findByReadTimeBetweenAndDeviceIdInOrderByReadTimeDesc(Date startDate, Date endDate,
			List<String> deviceIds) {
		return dataElectricityDao.findByReadTimeBetweenAndDeviceIdInOrderByReadTimeDesc(startDate, endDate, deviceIds);
	}

	public void deleteByReadTimeLessThan(Date dateTime) {
		dataElectricityDao.deleteByReadTimeLessThan(dateTime);
	}

}
