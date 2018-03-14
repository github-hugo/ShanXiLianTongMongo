package nhb.system.platform.entity.system;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 
 * @ClassName: SysUserArea
 * @Description: 用户-区域 对应关系
 * @author XS guo
 * @date 2017年9月21日 上午8:51:53
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Document(collection = "sys_user_area")
public class SysUserArea {

	@Field("user_id")
	private String userId;

	@Field("area_id")
	private String areaId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

}
