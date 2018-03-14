package nhb.system.platform.api.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.nhb.utils.nhb_utils.common.RestResultDto;
import com.nhb.utils.nhb_utils.common.StringUtil;

import io.swagger.annotations.ApiOperation;
import nhb.system.platform.api.inface.SystemLog;
import nhb.system.platform.dataaccess.service.system.SysUserAreaService;
import nhb.system.platform.entity.system.SysUserArea;
import nhb.system.platform.request.system.SysUserAreaRequest;

/**
 * 
 * @ClassName: SysUserAreaController
 * @Description: 用户-区域绑定关系
 * @author XS guo
 * @date 2017年9月21日 下午8:45:10
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("api/v1/sys/user/area")
public class SysUserAreaController {

	@Autowired
	private SysUserAreaService sysUserAreaService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "绑定用户-区域", notes = "绑定用户-区域")
	@RequestMapping(value = "save", method = { RequestMethod.POST })
	@SystemLog(module = "SYSTEM", methods = "SYSTEM_BIND_USERAREA")
	public RestResultDto save(@RequestBody SysUserAreaRequest request) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			String userId = request.getUserId();
			if (StringUtil.isNullOrEmpty(userId)) {
				throw new Exception("用户id不能为空！");
			}
			// 先删除
			sysUserAreaService.deleteByUserId(userId);
			List<String> areaIds = request.getAreaIds();
			List<SysUserArea> userAreas = Lists.newArrayList();
			SysUserArea userArea = null;
			if (!CollectionUtils.isEmpty(areaIds)) {
				for (String areaId : areaIds) {
					userArea = new SysUserArea();
					userArea.setUserId(userId);
					userArea.setAreaId(areaId);
					userAreas.add(userArea);
				}
				sysUserAreaService.saveList(userAreas);
			}
			data = true;
			msg = "用户绑定区域成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = false;
			msg = "用户绑定区域失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "根据用户id查询其绑定的区域", notes = "根据用户id查询其绑定的区域")
	@RequestMapping(value = "findByUserId", method = { RequestMethod.POST })
	public RestResultDto findByUserId(@RequestBody SysUserAreaRequest request) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			String userId = request.getUserId();

			List<String> areaIds = Lists.newArrayList(); 
			List<SysUserArea> areas = sysUserAreaService.findByUserId(userId);
			if(!CollectionUtils.isEmpty(areas)) {
				for(SysUserArea area : areas) {
					areaIds.add(area.getAreaId());
				}
			}

			data = areaIds;
			msg = "查询区域成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = false;
			msg = "查询区域失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

}
