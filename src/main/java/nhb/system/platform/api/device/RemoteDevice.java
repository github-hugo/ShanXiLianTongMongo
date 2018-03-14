package nhb.system.platform.api.device;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nhb.utils.nhb_utils.common.RestResultDto;
import com.nhb.utils.nhb_utils.common.StringUtil;

import io.swagger.annotations.ApiOperation;
import nhb.system.platform.api.inface.SystemLog;
import nhb.system.platform.dataaccess.service.data.SwitchStatusService;
import nhb.system.platform.dataaccess.service.system.SysAreaService;
import nhb.system.platform.dataaccess.service.system.SysUserAreaService;
import nhb.system.platform.dataaccess.service.system.SysUserService;
import nhb.system.platform.entity.data.SwitchStatus;
import nhb.system.platform.entity.system.SysArea;
import nhb.system.platform.entity.system.SysUser;
import nhb.system.platform.entity.system.SysUserArea;
import nhb.system.platform.enums.SwitchStatusEnum;
import nhb.system.platform.enums.SysAreaTypeEnum;
import nhb.system.platform.enums.SysUserRoleEnum;
import nhb.system.platform.feign.RemoteService;
import nhb.system.platform.request.device.ControlRequest;
import nhb.system.platform.request.system.SysUserRequest;
import nhb.system.platform.websocket.WebSocketServer;
import nhb.system.platform.websocket.response.WebSockerResponse;
import nhb.system.platform.websocket.response.WebSocketTypeEnum;

/**
 * @author XS guo
 * @ClassName: RemoteDevice
 * @Description: 远程控制接口
 * @date 2017年9月21日 下午8:36:26
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("api/v1/remote")
public class RemoteDevice {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysAreaService sysAreaService;

	@Autowired
	private SysUserAreaService sysUserAreaService;

	@Autowired
	private SwitchStatusService switchStatusService;

	@Autowired
	private RemoteService remoteService;

	/**
	 * 用于存储Device 和 user对应关系
	 */
	Map<String, String> deviceUserMap = Maps.newHashMap();

	WebSockerResponse webSockerResponse;

	/**
	 * @return RestResultDto
	 * @Title: findRemoteDevices
	 * @Description: 根据用户id查询其下属可进行远程控制的设备列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "远程控制列表", notes = "必传：id")
	@RequestMapping(value = "findRemoteDevices", method = { RequestMethod.POST })
	public RestResultDto findRemoteDevices(@RequestBody SysUserRequest request) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			String userId = request.getId();
			if (StringUtil.isNullOrEmpty(userId)) {
				throw new Exception("用户id不能为空！");
			}
			// 总条数
			Long total = 0L;
			// 页码总数
			Integer totalPage = 0;
			// 查询当前用户
			SysUser sysUser = sysUserService.findById(userId);
			if (null == sysUser) {
				throw new Exception("当前用户为空！");
			}
			// 用于存储查询到有效的设备列表
			List<SysArea> sysAreas = Lists.newArrayList();
			Pageable pageable = PageRequest.of(request.getPage() - 1, request.getRows());
			if (sysUser.getRole().equals(SysUserRoleEnum.ADMIN.getKey())) { // 当当前用户为管理员账号
				// 当前账号，类型为可控制的区域，分页
				Page<SysArea> pageList = sysAreaService.findByManagerIdAndIsCtrl(userId, true, pageable);
				if (null != pageList) {
					// 赋值
					sysAreas = pageList.getContent();
					total = pageList.getTotalElements();
					totalPage = pageList.getTotalPages();
				}
			} else { // 用户
				// 查询当前用户绑定的区域
				List<SysUserArea> userAreas = sysUserAreaService.findByUserId(userId);
				if (null != userAreas) {
					String[] areaIds = new String[userAreas.size()];
					for (int i = 0; i < userAreas.size(); i++) {
						areaIds[i] = userAreas.get(i).getAreaId();
					}
					// 查询 用户下属区域可控制的设备列表
					Page<SysArea> pageList = sysAreaService.findByIdsAndIsCtrl(areaIds, true, pageable);
					if (null != pageList) {
						sysAreas = pageList.getContent();
						total = pageList.getTotalElements();
						totalPage = pageList.getTotalPages();
					}
				}
			}
			// 返回结果
			Map<String, Object> returnValue = Maps.newHashMap();
			List<Object> ListValue = Lists.newArrayList();
			Map<String, Object> mapValue = null;

			List<String> deviceIds = Lists.newArrayList();
			if (!CollectionUtils.isEmpty(sysAreas)) {
				for (SysArea area : sysAreas) {
					deviceIds.add(area.getDeviceId());
				}
				// 查询开关状态
				List<SwitchStatus> switchStatus = Lists.newArrayList();
				if (!CollectionUtils.isEmpty(deviceIds)) {
					switchStatus = switchStatusService.findByIds(deviceIds);
				}
				for (SysArea area : sysAreas) {
					mapValue = Maps.newHashMap();
					mapValue.put("deviceId", area.getDeviceId());
					mapValue.put("name", area.getName());
					mapValue.put("type", area.getResourceType());
					mapValue.put("typeName", SysAreaTypeEnum.getValueByKey(area.getResourceType()));
					mapValue.put("status", SwitchStatusEnum.NONE.getKey());
					mapValue.put("statusName", SwitchStatusEnum.NONE.getValue());
					if (!CollectionUtils.isEmpty(switchStatus)) {
						for (SwitchStatus status : switchStatus) {
							if (status.getDeviceId().equals(area.getDeviceId())) {
								mapValue.put("status", status.getStatus());
								mapValue.put("statusName", SwitchStatusEnum.getValueByKey(status.getStatus()));
							}
						}
					}
					ListValue.add(mapValue);
				}
			}
			returnValue.put("total", total);
			returnValue.put("totolPage", totalPage);
			returnValue.put("currPage", request.getPage());
			returnValue.put("rows", ListValue);
			data = returnValue;
			msg = "获取远程控制设备列表成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "获取远程控制设备列表失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

	/**
	 * @return RestResultDto
	 * @Title: findRemoteDevices
	 * @Description: 根据用户id查询其下属可进行远程控制的设备列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "远程控制", notes = "必传：deviceId，userId，type")
	@RequestMapping(value = "control", method = { RequestMethod.POST })
	@SystemLog(module = "DEVICE", methods = "DEVICE_REMOTE_CONTROL")
	public RestResultDto control(@RequestBody ControlRequest request) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			String userId = request.getUserId();
			String deviceId = request.getDeviceId();
			String type = request.getType();
			if (StringUtil.isNullOrEmpty(type) || StringUtil.isNullOrEmpty(deviceId)
					|| StringUtil.isNullOrEmpty(userId)) {
				throw new Exception("必要参数不能为空！");
			}
			// 保存map
			deviceUserMap.put(deviceId, userId);

			// 调用采集软件接口
			Map<String, Object> param = Maps.newHashMap();
			param.put("deviceId", deviceId);
			param.put("type", type);

			RestResultDto dto = remoteService.remoteDevice(param);
			result = dto.getResult();
			data = dto.getData();
			msg = dto.getMsg();
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = false;
			msg = "远程控制命令下发失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

	/**
	 * @return void
	 * @Title: pushMessageToClient
	 * @Description: 远程控制返回结果
	 */
	@ApiOperation(value = "远程控制回调", notes = "采集软件远程控制回调")
	@RequestMapping(value = "push", method = { RequestMethod.POST })
	public void pushMessageToClient(ControlRequest request) {
		String userId = deviceUserMap.get(request.getDeviceId());
		webSockerResponse = new WebSockerResponse();
		webSockerResponse.setInfoType(WebSocketTypeEnum.REMOTE_CONTROL.getKey());
		webSockerResponse.setData(request);
		WebSocketServer.pushMessageToUser(userId, webSockerResponse);

		SwitchStatus switchStatus = new SwitchStatus();
		switchStatus.setDeviceId(request.getDeviceId());
		switchStatus.setReadTime(new Date());
		switchStatus.setStatus(request.getType());
		// 控制之后，更新表格中的状态
		switchStatusService.saveOrUpdate(switchStatus);
	}

}
