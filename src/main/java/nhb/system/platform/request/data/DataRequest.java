/** 
 * Project Name:nhb-platform 
 * File Name:DataRequest.java 
 * Package Name:nhb.system.platform.request.data 
 * Date:2017年9月21日下午4:03:59 
 * Copyright (c) 2017, lorisun@live.com All Rights Reserved. 
 * 
*/

package nhb.system.platform.request.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import nhb.system.platform.base.page.PageRequest;

/**
 * @ClassName:DataRequest
 * @Function: ADD FUNCTION.
 * @Reason: ADD REASON.
 * @Date: 2017年9月21日 下午4:03:59
 * @author xuyahui
 * @version
 * @since JDK 1.7
 * @see
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel
public class DataRequest extends PageRequest {

	@XmlElement(name = "deviceIds")
	@ApiModelProperty(value = "deviceIds", required = true, example = "[226,228]")
	private List<String> deviceIds;

	@XmlElement(name = "electricityType")
	@ApiModelProperty(value = "electricityType", required = true, example = "3Phase")
	private String electricityType;

	@XmlElement(name = "deviceType")
	@ApiModelProperty(value = "deviceType", required = true, example = "water")
	private String deviceType;

	public String getElectricityType() {
		return electricityType;
	}

	public void setElectricityType(String electricityType) {
		this.electricityType = electricityType;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public List<String> getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(List<String> deviceIds) {
		this.deviceIds = deviceIds;
	}

}
