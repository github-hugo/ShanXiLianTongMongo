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
import nhb.system.platform.dataaccess.service.system.SysUserCollectorService;
import nhb.system.platform.entity.system.SysUserCollector;
import nhb.system.platform.request.system.SysUserCollectorRequest;

/**
 * 
 * @ClassName: SysUserCollectorController
 * @Description: 用户-采集器配置接口
 * @author XS guo
 * @date 2017年9月21日 下午8:41:24
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("api/v1/sys/collector")
public class SysUserCollectorController {

	@Autowired
	private SysUserCollectorService sysUserCollectorService;

	/**
	 * 
	 * @Title: save
	 * @Description: 保存绑定的用户和采集器
	 * @return RestResultDto
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "绑定用户和采集器", notes = "绑定用户和采集器")
	@RequestMapping(value = "save", method = { RequestMethod.POST })
	@SystemLog(module = "SYSTEM", methods = "SYSTEM_BIND_USERCOLLECTOR")
	public RestResultDto save(@RequestBody SysUserCollectorRequest request) {
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
			// 先进行删除
			sysUserCollectorService.deleteByUserId(userId);
			List<String> collectorIds = request.getCollectorIds();
			List<SysUserCollector> userCollectors = Lists.newArrayList();
			SysUserCollector userCollector = null;
			if (!CollectionUtils.isEmpty(collectorIds)) {
				for (String collectorId : collectorIds) {
					userCollector = new SysUserCollector();
					userCollector.setUserId(userId);
					userCollector.setCollectorId(collectorId);
					userCollectors.add(userCollector);
				}
				// 保存list
				sysUserCollectorService.saveList(userCollectors);
			}
			data = true;
			msg = "用户绑定采集器成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = false;
			msg = "用户绑定采集器失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

	/**
	 * 
	 * @Title: findByUserId
	 * @Description: 根据用户查询下属采集器
	 * @return RestResultDto
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "根据用户查询下属采集器", notes = "根据用户查询下属采集器")
	@RequestMapping(value = "findByUserId", method = { RequestMethod.POST })
	public RestResultDto findByUserId(@RequestBody SysUserCollectorRequest request) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			String userId = request.getUserId();
			List<String> collectorIds = Lists.newArrayList();

			List<SysUserCollector> collectors = sysUserCollectorService.findByUserId(userId);

			if (!CollectionUtils.isEmpty(collectors)) {
				for (SysUserCollector collector : collectors) {
					collectorIds.add(collector.getCollectorId());
				}
			}

			data = collectorIds;
			msg = "查询采集器成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = false;
			msg = "查询采集器失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

}
