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
import nhb.system.platform.dataaccess.service.device.CollectorStatusService;
import nhb.system.platform.dataaccess.service.device.ReceiptCollectorService;
import nhb.system.platform.dataaccess.service.device.ReceiptMeterService;
import nhb.system.platform.dataaccess.service.system.SysUserCollectorService;
import nhb.system.platform.dataaccess.service.system.SysUserService;
import nhb.system.platform.entity.device.CollectorStatus;
import nhb.system.platform.entity.device.ReceiptCollector;
import nhb.system.platform.entity.device.ReceiptMeter;
import nhb.system.platform.entity.system.SysUser;
import nhb.system.platform.entity.system.SysUserCollector;
import nhb.system.platform.enums.CollectorTypeEnum;
import nhb.system.platform.enums.SysUserRoleEnum;
import nhb.system.platform.request.device.ReceiptCollectorRequest;

@RestController
@RequestMapping("api/v1/collector")
public class CollectorController {

    @Autowired
    private ReceiptCollectorService receiptCollectorService;

    @Autowired
    private ReceiptMeterService receiptMeterService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserCollectorService sysUserCollectorService;

    @Autowired
    private CollectorStatusService collectorStatusService;

    /**
     * @return RestResultDto
     * @Title: save
     * @Description: 新增采集器
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "新增采集器", notes = "新增采集器")
    @RequestMapping(value = "save", method = {RequestMethod.POST})
    @SystemLog(module = "DEVICE", methods = "DEVICE_ADD_COLLECTOR")
    public RestResultDto save(@RequestBody ReceiptCollector receiptCollector) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {
            if (StringUtil.isNullOrEmpty(receiptCollector.getManagerId())) {
                throw new Exception("用户id不能为空！");
            }
            String collectorNo = receiptCollector.getCollectorNo();
            if (StringUtil.isNullOrEmpty(collectorNo)) {
                throw new Exception("采集器编号不能为空！");
            }
            ReceiptCollector collector = receiptCollectorService.findByCollectorNo(collectorNo);
            if (null != collector) {
                throw new Exception("已存在编号为" + collectorNo + "的采集器！");
            }
            SysUser sysUser = sysUserService.findById(receiptCollector.getManagerId());
            if (!sysUser.getRole().equals(SysUserRoleEnum.ADMIN.getKey())) {
                receiptCollector.setManagerId(sysUser.getParentId());
            }
            receiptCollector = receiptCollectorService.saveOrUpdate(receiptCollector);
            if (!sysUser.getRole().equals(SysUserRoleEnum.ADMIN.getKey())) {
                SysUserCollector sysUserCollector = new SysUserCollector();
                sysUserCollector.setUserId(sysUser.getId());
                sysUserCollector.setCollectorId(receiptCollector.getId());
                sysUserCollectorService.save(sysUserCollector);
            }
            data = receiptCollector;
            msg = "保存采集器成功！";
        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = null;
            msg = "保存采集器失败！";
        } finally {
            resultDto.setData(data);
            resultDto.setException(exception);
            resultDto.setMsg(msg);
            resultDto.setResult(result);
        }
        return resultDto;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "修改采集器", notes = "修改采集器")
    @RequestMapping(value = "update", method = {RequestMethod.POST})
    @SystemLog(module = "DEVICE", methods = "DEVICE_UPDATE_COLLECTOR")
    public RestResultDto update(@RequestBody ReceiptCollector receiptCollector) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {
            receiptCollector = receiptCollectorService.saveOrUpdate(receiptCollector);
            data = receiptCollector;
            msg = "采集器修改成功！";
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
     * @return RestResultDto
     * @Title: findListByUserId
     * @Description: 查询采集器列表
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "查询设备列表", notes = "用户id")
    @RequestMapping(value = "findListByUserId", method = {RequestMethod.POST})
    public RestResultDto findListByUserId(@RequestBody ReceiptCollectorRequest request) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {
            Map<String, Object> returnValue = Maps.newHashMap();
            List<ReceiptCollector> collectors = Lists.newArrayList();
            Long total = 0L;
            Integer totolPage = 0;

            String ownerId = request.getOwnerId();
            if (StringUtil.isNullOrEmpty(ownerId)) {
                throw new Exception("用户id不能为空！");
            }
            Pageable pageable = null;
            if (request.getPage() != null && request.getRows() != null) {
                pageable = PageRequest.of(request.getPage() - 1, request.getRows());
            }
            SysUser sysUser = sysUserService.findById(ownerId);
            if (sysUser.getRole().equals(SysUserRoleEnum.SUPERADMIN.getKey())) {
                Page<ReceiptCollector> pageList = receiptCollectorService.findAll(pageable);
                if (pageList != null) {
                    collectors = pageList.getContent();
                    total = pageList.getTotalElements();
                    totolPage = pageList.getTotalPages();
                }
            } else if (sysUser.getRole().equals(SysUserRoleEnum.ADMIN.getKey())) {
                Page<ReceiptCollector> pageList = receiptCollectorService.findByManagerId(ownerId, pageable);
                if (pageList != null) {
                    collectors = pageList.getContent();
                    total = pageList.getTotalElements();
                    totolPage = pageList.getTotalPages();
                }
            } else {
                Page<SysUserCollector> userCollectors = sysUserCollectorService.findByUserId(ownerId, pageable);
                List<String> collectorIds = Lists.newArrayList();
                if (null != userCollectors) {
                    for (SysUserCollector collector : userCollectors) {
                        collectorIds.add(collector.getCollectorId());
                    }
                    String[] array = new String[collectorIds.size()];
                    for (int i = 0; i < collectorIds.size(); i++) {
                        array[i] = collectorIds.get(i);
                    }
                    collectors = receiptCollectorService.findByIds(collectorIds);
                    total = userCollectors.getTotalElements();
                    totolPage = userCollectors.getTotalPages();
                }
            }
            if (request.getPage() != null && request.getRows() != null) {
                returnValue.put("totolPage", totolPage);
                returnValue.put("currPage", request.getPage());
            }
            returnValue.put("total", total);
            returnValue.put("rows", collectors);
            data = returnValue;
            msg = "采集器查询成功！";
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "删除采集器", notes = "删除采集器")
    @RequestMapping(value = "delete", method = {RequestMethod.POST})
    @SystemLog(module = "DEVICE", methods = "DEVICE_DELETE_COLLECTOR")
    public RestResultDto delete(@RequestBody ReceiptCollectorRequest request) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {
            String collectorId = request.getId();
            if (null == collectorId) {
                throw new Exception("采集器id不能为空！");
            }
            List<ReceiptMeter> meters = receiptMeterService.findByCollectorId(collectorId, null).getContent();
            if (!CollectionUtils.isEmpty(meters)) {
                throw new Exception("请先删除采集器下电表！");
            }
            List<SysUserCollector> collectors = sysUserCollectorService.findByCollectorId(collectorId);
            if (!CollectionUtils.isEmpty(collectors)) {
                throw new Exception("请先解除该采集器和其挂接的用户！");
            }
            receiptCollectorService.delete(request.getId());
            data = true;
            msg = "采集器删除成功！";
        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = false;
            msg = "保存采删除失败！";
        } finally {
            resultDto.setData(data);
            resultDto.setException(exception);
            resultDto.setMsg(msg);
            resultDto.setResult(result);
        }
        return resultDto;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "getCollectorType", method = {RequestMethod.POST})
    public RestResultDto getCollectorType() {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {
            List<Object> returnValue = Lists.newArrayList();
            Map<String, Object> mapValue = null;
            for (CollectorTypeEnum value : CollectorTypeEnum.values()) {
                mapValue = Maps.newHashMap();
                mapValue.put("key", value.getKey());
                mapValue.put("value", value.getValue());
                returnValue.add(mapValue);
            }
            data = returnValue;
            msg = "获取采集器类型成功！";
        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = false;
            msg = "获取采集器类型失败！";
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
     * @Title: deviceOnlineCounts
     * @Description: 设备在线个数
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "设备在线个数", notes = "用户id")
    @RequestMapping(value = "deviceOnlineCounts", method = {RequestMethod.POST})
    public RestResultDto deviceOnlineCounts(@RequestBody ReceiptCollectorRequest request) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {
            // 用户id
            String ownerId = request.getOwnerId();
            if (StringUtil.isNullOrEmpty(ownerId)) {
                throw new Exception("用户id不能为空！");
            }
            SysUser sysUser = sysUserService.findById(ownerId);
            if (null == sysUser) {
                throw new Exception("当前用户为空！");
            }
            String[] collectorIds = null;
            long deviceOnlineCount = 0L;
            if (sysUser.getRole().equals(SysUserRoleEnum.SUPERADMIN.getKey())) {
                List<ReceiptCollector> collectors = receiptCollectorService.findAll();
                if (!CollectionUtils.isEmpty(collectors)) {
                    collectorIds = new String[collectors.size()];
                    for (int i = 0; i < collectors.size(); i++) {
                        collectorIds[i] = collectors.get(i).getId();
                    }
                }
            } else if (sysUser.getRole().equals(SysUserRoleEnum.ADMIN.getKey())) {
                List<ReceiptCollector> collectors = receiptCollectorService.findByManagerId(sysUser.getId(), null)
                        .getContent();
                if (!CollectionUtils.isEmpty(collectors)) {
                    collectorIds = new String[collectors.size()];
                    for (int i = 0; i < collectors.size(); i++) {
                        collectorIds[i] = collectors.get(i).getId();
                    }
                }
            } else {
                Page<SysUserCollector> pageList = sysUserCollectorService.findByUserId(ownerId, null);
                if (null != pageList) {
                    List<SysUserCollector> userCollectors = pageList.getContent();
                    collectorIds = new String[userCollectors.size()];
                    for (int i = 0; i < userCollectors.size(); i++) {
                        collectorIds[i] = userCollectors.get(i).getCollectorId();
                    }
                }
            }

            Map<String, Object> returnValue = Maps.newHashMap();
            returnValue.put("deviceTotalCount", 0);

            if (null != collectorIds) {
                returnValue.put("deviceTotalCount", collectorIds.length);
                List<CollectorStatus> statusList = collectorStatusService.findByCollectorIdIn(collectorIds);
                if (!CollectionUtils.isEmpty(statusList)) {
                    for (CollectorStatus status : statusList) {
                        if (new Date().getTime() - status.getActiveTime().getTime() < 10 * 60 * 1000) {
                            deviceOnlineCount++;
                        }
                    }
                }
            }
            returnValue.put("deviceOnlineCount", deviceOnlineCount);
            data = returnValue;
            msg = "获取在线设备个数成功！";
        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = 0;
            msg = "获取在线设备个数失败！";
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
     * @Title: unbindCollector
     * @Description: 设查询未绑定管理员的采集器
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "查询未绑定管理员的采集器", notes = "传超级管理员的id")
    @RequestMapping(value = "unbindCollector", method = {RequestMethod.POST})
    public RestResultDto unbindCollector(@RequestBody ReceiptCollectorRequest request) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {
            Map<String, Object> returnValue = Maps.newHashMap();
            Integer totolPage = 0;
            Long total = 0L;
            List<ReceiptCollector> collectors = Lists.newArrayList();
            String userId = request.getOwnerId();
            if (null == userId) {
                throw new Exception("用户id不能为空！");
            }
            SysUser sysUser = sysUserService.findById(userId);
            if (null == sysUser || !sysUser.getRole().equals(SysUserRoleEnum.SUPERADMIN.getKey())) {
                throw new Exception("当前用户没有权限！");
            }
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getRows());
            Page<ReceiptCollector> pageList = receiptCollectorService.findByManagerIdIsNull(pageable);
            if (null != pageList) {
                collectors = pageList.getContent();
                total = pageList.getTotalElements();
                totolPage = pageList.getTotalPages();
            }
            returnValue.put("total", total);
            returnValue.put("totolPage", totolPage);
            returnValue.put("currPage", request.getPage());
            returnValue.put("rows", collectors);

            data = returnValue;
            msg = "获取为绑定的采集器成功！";

        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = 0;
            msg = "获取为绑定的采集器失败！";
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
     * @Title: bindCollector
     * @Description: 绑定管理员和采集器
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "绑定管理员和采集器", notes = "传采集器id和管理员")
    @RequestMapping(value = "bindCollector", method = {RequestMethod.POST})
    @SystemLog(module = "DEVICE", methods = "DEVICE_BIND_MANAGERCOLLECTOR")
    public RestResultDto bindCollector(@RequestBody ReceiptCollectorRequest request) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {

            List<String> collectorIds = request.getIds();
            String ownerId = request.getOwnerId();

            List<ReceiptCollector> collectors = receiptCollectorService.findByIds(collectorIds);
            if (CollectionUtils.isEmpty(collectors)) {
                for (ReceiptCollector collector : collectors) {
                    collector.setManagerId(ownerId);
                    receiptCollectorService.saveOrUpdate(collector);
                }
            }

            data = true;
            msg = "绑定成功！";

        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = 0;
            msg = "绑定失败！";
        } finally {
            resultDto.setData(data);
            resultDto.setException(exception);
            resultDto.setMsg(msg);
            resultDto.setResult(result);
        }
        return resultDto;
    }

}
