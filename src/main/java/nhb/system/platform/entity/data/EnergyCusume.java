package nhb.system.platform.entity.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "energy_cusume")
public class EnergyCusume {

	@Id
	private String id;

	@Field("device_id")
	private String deviceId;

	@Field("area_id")
	private String areaId;

	@Field("parent_id")
	private String parentId;

	@Field("top_area_id")
	private String topAreaId;

	@Field("resource_type")
	private String resourceType;

	@Field("cusume")
	private Double cusume;

	@Field("money_spent")
	private Double moneySpent;

	@Field("date")
	private String date;

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

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Double getCusume() {
		return cusume;
	}

	public void setCusume(Double cusume) {
		this.cusume = cusume;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTopAreaId() {
		return topAreaId;
	}

	public void setTopAreaId(String topAreaId) {
		this.topAreaId = topAreaId;
	}

	public Double getMoneySpent() {
		return moneySpent;
	}

	public void setMoneySpent(Double moneySpent) {
		this.moneySpent = moneySpent;
	}

}
