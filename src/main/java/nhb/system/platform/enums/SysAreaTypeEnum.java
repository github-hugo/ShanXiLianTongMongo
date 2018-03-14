package nhb.system.platform.enums;

import com.nhb.utils.nhb_utils.common.StringUtil;

public enum SysAreaTypeEnum {

	WATER("WATER", "水"), ELECTRICITY("ELECTRICITY", "电"), GAS("GAS", "气"), HOT("HOT", "热"), CTRL("CTRL",
			"控制"), AREA("AREA", "区域");

	private SysAreaTypeEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}

	private String key;

	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @Title: getValueByKey
	 * @Description: (根据key获取value)
	 * @return String
	 * @author XS guo
	 * @date 2017-9-17 上午10:32:59
	 */
	public static String getValueByKey(String key) {
		if (!StringUtil.isNullOrEmpty(key)) {
			for (SysAreaTypeEnum e : SysAreaTypeEnum.values()) {
				if (e.getKey().equals(key)) {
					return e.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * @Title: getKeyByValue
	 * @Description: (根据value获取key)
	 * @return String author XS guo
	 * @date 2017-9-17 上午10:32:59
	 */
	public static String getKeyByValue(String value) {
		if (!StringUtil.isNullOrEmpty(value)) {
			for (SysAreaTypeEnum e : SysAreaTypeEnum.values()) {
				if (e.getValue().equals(value)) {
					return e.getKey();
				}
			}
		}
		return null;
	}

}
