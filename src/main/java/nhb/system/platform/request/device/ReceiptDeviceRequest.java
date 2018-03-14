package nhb.system.platform.request.device;

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
public class ReceiptDeviceRequest extends PageRequest {

	@XmlElement(name = "id")
	@ApiModelProperty(value = "id", required = true, example = "201708080811")
	private String id;

	@XmlElement(name = "meterId")
	@ApiModelProperty(value = "meterId", required = true, example = "1")
	private String meterId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMeterId() {
		return meterId;
	}

	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

}
