package nhb.system.platform.entity.system;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * @author Administrator
 * @description 用户菜单权限
 * @date 2017-11-03
 */
@Document(collection = "sys_user_menu")
public class SysUserMenu {

    @Id
    @Field("user_id")
    private String userId;

    @Field("menu")
    private List<Integer> menu;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Integer> getMenu() {
        return menu;
    }

    public void setMenu(List<Integer> menu) {
        this.menu = menu;
    }
}
