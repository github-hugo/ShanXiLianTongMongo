package nhb.system.platform.request.device;

import javax.xml.bind.annotation.XmlElement;

import io.swagger.annotations.ApiModelProperty;
import nhb.system.platform.base.page.PageRequest;

public class ReceiptMeterRequest extends PageRequest {

	@XmlElement(name = "id")
	@ApiModelProperty(value = "id", required = true, example = "1")
	private String id;

	@XmlElement(name = "collectorId")
	@ApiModelProperty(value = "collectorId", required = true, example = "1")
	private String collectorId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(String collectorId) {
		this.collectorId = collectorId;
	}

}
