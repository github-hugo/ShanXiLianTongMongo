/** 
 * Project Name:nhb-platform 
 * File Name:DataWater.java 
 * Package Name:nhb.system.platform.entity.data 
 * Date:2017年9月21日下午1:46:38 
 * Copyright (c) 2017, lorisun@live.com All Rights Reserved. 
 * 
*/

package nhb.system.platform.entity.data;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @ClassName:DataWater
 * @Function: TODO ADD FUNCTION.
 * @Reason: TODO ADD REASON.
 * @Date: 2017年9月21日 下午1:46:38
 * @author xuyahui
 * @version
 * @since JDK 1.7
 * @see
 */
@Document(collection = "data_water")
public class DataWater {

	@Id
	private UUID id;

	@Field("device_id")
	private String deviceId;

	@Field("read_time")
	private Date readTime;

	@Field("consumption")
	private Double consumption;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public Double getConsumption() {
		return consumption;
	}

	public void setConsumption(Double consumption) {
		this.consumption = consumption;
	}

}
