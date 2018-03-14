package nhb.system.platform.api.device;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import nhb.system.platform.dataaccess.service.device.ReceiptDeviceService;
import nhb.system.platform.dataaccess.service.system.SysAreaService;
import nhb.system.platform.entity.device.ReceiptDevice;
import nhb.system.platform.request.device.ReceiptDeviceRequest;

@RestController
@RequestMapping("api/v1/device")
public class DeviceController {

	@Autowired
	private ReceiptDeviceService deviceService;

	@Autowired
	private SysAreaService sysAreaService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "保存设备", notes = "保存设备")
	@RequestMapping(value = "save", method = { RequestMethod.POST })
	@SystemLog(module = "DEVICE", methods = "DEVICE_ADD_DEVICE")
	public RestResultDto save(@RequestBody ReceiptDevice device) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			device.setId(device.getCollectorNo() + device.getMeterNo() + device.getCircuitNo());
			device = deviceService.saveOrUpdate(device);
			data = device;
			msg = "保存设备成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "设备保存失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "修改设备信息 ", notes = "修改设备信息")
	@RequestMapping(value = "update", method = { RequestMethod.POST })
	@SystemLog(module = "DEVICE", methods = "DEVICE_UPDATE_DEVICE")
	public RestResultDto update(@RequestBody ReceiptDevice device) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			device = deviceService.saveOrUpdate(device);
			data = device;
			msg = "设备修改成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "设备修改失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "根据电表id查询下属设备", notes = "根据电表id查询下属设备")
	@RequestMapping(value = "findListByMeterId", method = { RequestMethod.POST })
	public RestResultDto findListByMeterId(@RequestBody ReceiptDeviceRequest request) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			Map<String, Object> returnValue = Maps.newHashMap();
			List<ReceiptDevice> devices = Lists.newArrayList();
			Long total = 0L;
			Integer totolPage = 0;
			Sort sort = new Sort(Direction.DESC, "collectorNo");
			Pageable pageable = PageRequest.of(request.getPage() - 1, request.getRows(), sort);
			Page<ReceiptDevice> pageList = deviceService.findByMeterId(request.getMeterId(), pageable);
			if (pageList != null) {
				devices = pageList.getContent();
				total = pageList.getTotalElements();
				totolPage = pageList.getTotalPages();
			}
			returnValue.put("total", total);
			returnValue.put("totolPage", totolPage);
			returnValue.put("currPage", request.getPage());
			returnValue.put("rows", devices);
			data = returnValue;
			msg = "设备查询成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "采集器查询失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "删除设备", notes = "删除设备")
	@RequestMapping(value = "delete", method = { RequestMethod.POST })
	@SystemLog(module = "DEVICE", methods = "DEVICE_DELETE_DEVICE")
	public RestResultDto delete(@RequestBody ReceiptDeviceRequest request) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			String deviceId = request.getId();
			if (StringUtil.isNullOrEmpty(deviceId)) {
				throw new Exception("设备id不能为空！");
			}

			Boolean flag = sysAreaService.existsByDeviceId(deviceId);
			if (flag) {
				throw new Exception("请先解绑该设备与区域！");
			}

			deviceService.delete(deviceId);
			data = true;
			msg = "设备删除成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = false;
			msg = "设备删除失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

}
