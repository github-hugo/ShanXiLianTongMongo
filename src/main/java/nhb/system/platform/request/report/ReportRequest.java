package nhb.system.platform.request.report;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import nhb.system.platform.base.page.PageRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel
public class ReportRequest extends PageRequest {

	@XmlElement(name = "id") // 区域id，一个
	@ApiModelProperty(value = "id", required = true, example = "1")
	private String id;

	@XmlElement(name = "areaId") // 区域id,一个或多个
	@ApiModelProperty(value = "areaId", required = false, example = "[1,2,3]")
	private List<String> areaId;

	@XmlElement(name = "userId") // 区域id，一个
	@ApiModelProperty(value = "userId", required = true, example = "1")
	private String userId;

	@XmlElement(name = "deviceId")
	@ApiModelProperty(value = "deviceId", required = false, example = "C17051601M170502")
	private String deviceId;

	@XmlElement(name = "resourceType")
	@ApiModelProperty(value = "resourceType", required = true, example = "area")
	private String resourceType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getAreaId() {
		return areaId;
	}

	public void setAreaId(List<String> areaId) {
		this.areaId = areaId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
