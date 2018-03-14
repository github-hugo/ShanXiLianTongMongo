package nhb.system.platform.request.system;

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
public class SysAreaRequest extends PageRequest {

	@XmlElement(name = "id")
	@ApiModelProperty(value = "id", required = true, example = "11")
	private String id;

	@XmlElement(name = "deviceId")
	@ApiModelProperty(value = "deviceId", required = true, example = "201709200111")
	private String deviceId;

	@XmlElement(name = "ownerId")
	@ApiModelProperty(value = "ownerId", required = true, example = "6d70b512-3dc4-40f1-b4bb-e35e78e4921a")
	private String ownerId;

	@XmlElement(name = "siteType")
	@ApiModelProperty(value = "siteType", required = true, example = "TXJ")
	private String siteType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

}
