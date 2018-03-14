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
public class SysUserRequest extends PageRequest {

    @XmlElement(name = "id")
    @ApiModelProperty(value = "id", required = true, example = "111")
    private String id;

    @XmlElement(name = "name")
    @ApiModelProperty(value = "name", required = true, example = "青春")
    private String name;

    @XmlElement(name = "role")
    @ApiModelProperty(value = "role", required = true, example = "ADMIN")
    private String role;

    @XmlElement(name = "parentId")
    @ApiModelProperty(value = "parentId", required = true, example = "111")
    private String parentId;

    @XmlElement(name = "oldPass")
    @ApiModelProperty(value = "oldPass", required = true, example = "111")
    private String oldPass;

    @XmlElement(name = "newPass")
    @ApiModelProperty(value = "newPass", required = true, example = "111")
    private String newPass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

}
