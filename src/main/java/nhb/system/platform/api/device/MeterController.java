package nhb.system.platform.api.device;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nhb.utils.nhb_utils.common.RestResultDto;

import io.swagger.annotations.ApiOperation;
import nhb.system.platform.api.inface.SystemLog;
import nhb.system.platform.dataaccess.service.device.ReceiptDeviceService;
import nhb.system.platform.dataaccess.service.device.ReceiptMeterService;
import nhb.system.platform.dataaccess.service.device.UtilProtocolTypeService;
import nhb.system.platform.entity.device.ReceiptDevice;
import nhb.system.platform.entity.device.ReceiptMeter;
import nhb.system.platform.entity.device.UtilProtocolType;
import nhb.system.platform.request.device.ReceiptMeterRequest;

@RestController
@RequestMapping("api/v1/meter")
public class MeterController {

	@Autowired
	private ReceiptMeterService receiptMeterService;

	@Autowired
	private ReceiptDeviceService receiptDeviceService;

	@Autowired
	private UtilProtocolTypeService utilProtocolTypeService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "新增电表", notes = "新增电表")
	@RequestMapping(value = "save", method = { RequestMethod.POST })
	@SystemLog(module = "DEVICE", methods = "DEVICE_ADD_METER")
	public RestResultDto save(@RequestBody ReceiptMeter receiptMeter) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			if (receiptMeter.getCollectorId() == null) {
				throw new Exception("采集器id不能为空！");
			}
			receiptMeter = receiptMeterService.saveOrUpdate(receiptMeter);
			data = receiptMeter;
			msg = "电表保存成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "电表保存失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

	@ApiOperation(value = "修改电表", notes = "修改电表")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "update", method = { RequestMethod.POST })
	@SystemLog(module = "DEVICE", methods = "DEVICE_UPDATE_METER")
	public RestResultDto update(@RequestBody ReceiptMeter receiptMeter) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			if (receiptMeter.getCollectorId() == null) {
				throw new Exception("采集器id不能为空！");
			}
			receiptMeter = receiptMeterService.saveOrUpdate(receiptMeter);
			data = receiptMeter;
			msg = "电表修改成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "采集器修改失败！";
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
	 * @Title: findListByCollectorId
	 * @Description: 根据id查询用户
	 * @return RestResultDto
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "根据采集器id查询下属电表", notes = "根据采集器id查询下属电表")
	@RequestMapping(value = "findListByCollectorId", method = { RequestMethod.POST })
	public RestResultDto findListByCollectorId(@RequestBody ReceiptMeterRequest request) throws Exception {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			Map<String, Object> returnValue = Maps.newHashMap();
			List<ReceiptMeter> meters = Lists.newArrayList();
			Long total = 0L;
			Integer totolPage = 0;
			Sort sort = new Sort(Direction.DESC, "meterNo");
			Pageable pageable = PageRequest.of(request.getPage() - 1, request.getRows(), sort);
			Page<ReceiptMeter> pageList = receiptMeterService.findByCollectorId(request.getCollectorId(), pageable);
			if (pageList != null) {
				meters = pageList.getContent();
				total = pageList.getTotalElements();
				totolPage = pageList.getTotalPages();
			}
			returnValue.put("total", total);
			returnValue.put("totolPage", totolPage);
			returnValue.put("currPage", request.getPage());
			returnValue.put("rows", meters);
			data = returnValue;
			msg = "电表查询成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "电表查询失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "删除电表", notes = "删除电表")
	@RequestMapping(value = "delete", method = { RequestMethod.POST })
	@SystemLog(module = "DEVICE", methods = "DEVICE_DELETE_METER")
	public RestResultDto delete(@RequestBody ReceiptMeterRequest request) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			String meterId = request.getId();
			if (null == meterId) {
				throw new Exception("meterId不能为空！");
			}
			List<ReceiptDevice> devices = receiptDeviceService.findByMeterId(meterId, null).getContent();
			if (!CollectionUtils.isEmpty(devices)) {
				throw new Exception("请先删除下属设备！");
			}
			receiptMeterService.delete(request.getId());
			data = true;
			msg = "电表删除成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = false;
			msg = "电表删除失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "获取协议类型", notes = "获取协议类型")
	@RequestMapping(value = "findProtocolTypeList", method = { RequestMethod.POST })
	public RestResultDto findProtocolTypeList() {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			List<UtilProtocolType> list = utilProtocolTypeService.findAll();
			data = list;
			msg = "获取协议类型成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = false;
			msg = "获取协议类型失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

}
