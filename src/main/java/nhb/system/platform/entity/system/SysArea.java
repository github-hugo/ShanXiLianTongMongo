package nhb.system.platform.entity.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "sys_area")
public class SysArea {

    @Id
    private String id;

    /**
     * 区域层级结构
     */
    @Field("parent_id")
    private String parentId;

    /**
     * 最顶级区域
     */
    @Field("top_area_id")
    private String topAreaId;

    /**
     * 名称
     */
    @Field("name")
    private String name;

    /**
     * 对应的设备id
     */
    @Field("device_id")
    private String deviceId;

    /**
     * 创建时间
     */
    @Field("create_time")
    private Date createTime;

    /**
     * 区域类型：水电气热等信息
     */
    @Field("resource_type")
    private String resourceType;

    /**
     * 该设备是否可以远程控制,true表示支持远程控制，false表示不支持
     */
    @Field("is_ctrl")
    private boolean isCtrl;

    /**
     * 管理员的账号
     */
    @Field("manager_id")
    private String managerId;

    /**
     * 站点类型
     */
    @Field("site_type")
    private String siteType;

    /**
     * 基本参数 json 格式
     */
    @Field("params")
    private List<Map<String, String>> params;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public boolean isCtrl() {
        return isCtrl;
    }

    public void setCtrl(boolean isCtrl) {
        this.isCtrl = isCtrl;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public List<Map<String, String>> getParams() {
        return params;
    }

    public void setParams(List<Map<String, String>> params) {
        this.params = params;
    }

}
