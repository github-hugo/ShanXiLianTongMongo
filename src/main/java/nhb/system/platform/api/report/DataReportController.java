package nhb.system.platform.api.report;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nhb.system.platform.dataaccess.service.common.CommonService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nhb.utils.nhb_utils.common.DateUtil;
import com.nhb.utils.nhb_utils.common.RestResultDto;
import com.nhb.utils.nhb_utils.common.StringUtil;

import io.swagger.annotations.ApiOperation;
import nhb.system.platform.dataaccess.service.data.DataElectricityService;
import nhb.system.platform.dataaccess.service.data.DataWaterService;
import nhb.system.platform.dataaccess.service.system.SysAreaService;
import nhb.system.platform.dataaccess.service.system.SysUserService;
import nhb.system.platform.dataaccess.service.system.UnitRateService;
import nhb.system.platform.entity.data.DataElectricity;
import nhb.system.platform.entity.data.DataWater;
import nhb.system.platform.entity.system.SysArea;
import nhb.system.platform.entity.system.SysUser;
import nhb.system.platform.entity.system.UnitRate;
import nhb.system.platform.enums.SysAreaTypeEnum;
import nhb.system.platform.enums.SysUserRoleEnum;
import nhb.system.platform.request.report.ReportRequest;

@RestController
@RequestMapping("api/v1/data/report")
public class DataReportController {

    @Autowired
    private SysAreaService sysAreaService;

    @Autowired
    private DataElectricityService dataElectricityService;

    @Autowired
    private DataWaterService dataWaterService;

    @Autowired
    private UnitRateService unitRateService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CommonService commonService;

    /**
     * @return RestResultDto
     * @Title: totalEnergy
     * @Description: 能耗信息
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "能耗信息", notes = "根据区域查询能耗信息")
    @RequestMapping(value = "totalEnergy", method = {RequestMethod.POST})
    public RestResultDto totalEnergy(@RequestBody ReportRequest request) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {
            List<String> areaIds = request.getAreaId();

            Date startDate = DateUtil.parse(request.getStartTime(), DateUtil.DATETIME_FORMAT);
            Date endDate = DateUtil.parse(request.getEndTime(), DateUtil.DATETIME_FORMAT);
            // 查询所有要显示的区域信息
            List<SysArea> sysAreas = sysAreaService.findByIds(areaIds);
            // 用于存储需要查询的DeviceId
            List<String> deviceIds = Lists.newArrayList();
            // 每一个区域所对应的deviceIds
            Map<String, List<String>> mapValue = Maps.newHashMap();
            List<String> deviceIdInArea = null;
            // 保存 id 和 name 对应的map
            Map<String, String> areIdForName = Maps.newHashMap();
            List<String> deviceIdsChild = null;
            for (SysArea area : sysAreas) {
                deviceIdInArea = Lists.newArrayList();
                if (!area.getResourceType().equals(SysAreaTypeEnum.AREA.getKey())) {// 直接绑定设备的区域
                    if (!StringUtil.isNullOrEmpty(area.getDeviceId())) {
                        deviceIds.add(area.getDeviceId());
                        deviceIdInArea.add(area.getDeviceId());
                    }
                } else { // 如果是区域信息
                    deviceIdsChild = Lists.newArrayList();
                    List<String> childDeviceIds = commonService.treeAreaList(area.getId(), deviceIdsChild);
                    if (!CollectionUtils.isEmpty(childDeviceIds)) {
                        deviceIds.addAll(childDeviceIds);
                        deviceIdInArea.addAll(childDeviceIds);
                    }
                }
                mapValue.put(area.getId(), deviceIdInArea);
                areIdForName.put(area.getId(), area.getName());
            }

            // 所有电的的历史数据
            List<DataElectricity> dataElectricities = dataElectricityService
                    .findByReadTimeBetweenAndDeviceIdInOrderByReadTimeDesc(startDate, endDate, deviceIds);
            // 对历史数据进行分组 key-deviceId, value-list
            Map<String, List<DataElectricity>> dataMap = commonService.cacheDataElectricity(dataElectricities);

            // 水 的数据 进行分组
            List<DataWater> dataWaters = dataWaterService
                    .findByReadTimeBetweenAndDeviceIdInOrderByReadTimeDesc(startDate, endDate, deviceIds);
            Map<String, List<DataWater>> dataWaterMap = commonService.cacheDataWater(dataWaters);

            // 遍历key
            List<String> dataWaterDeviceIds = Lists.newArrayList();
            if (!MapUtils.isEmpty(dataWaterMap)) {
                Set<String> waterIds = dataWaterMap.keySet();
                for (String key : waterIds) {
                    dataWaterDeviceIds.add(key);
                }

            }

            // 遍历key
            List<String> dataDeviceIds = Lists.newArrayList();
            if (!MapUtils.isEmpty(dataMap)) {
                Set<String> dataIds = dataMap.keySet();
                for (String key : dataIds) {
                    dataDeviceIds.add(key);
                }
            }

            // 遍历所有 要显示的 区域信息key
            Set<String> areaIdsKey = mapValue.keySet();
            List<String> areaIdsA = Lists.newArrayList();
            for (String key : areaIdsKey) {
                areaIdsA.add(key);
            }

            Map<String, Object> map = null;
            List<Object> returnValue = Lists.newArrayList();
            Double totalElec = (double) 0;
            Double totalWater = (double) 0;
            List<String> listMap = null;
            // 遍历区域
            for (String key : areaIdsA) {
                map = Maps.newHashMap();
                totalElec = (double) 0;
                totalWater = (double) 0;
                listMap = mapValue.get(key);
                if (!CollectionUtils.isEmpty(dataDeviceIds)) {
                    for (String dev : dataDeviceIds) {
                        if (listMap.contains(dev)) {
                            totalElec = totalElec + (dataMap.get(dev).get(0).getKwh()
                                    - dataMap.get(dev).get(dataMap.get(dev).size() - 1).getKwh());
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(dataWaterDeviceIds)) {
                    for (String waterId : dataWaterDeviceIds) {
                        if (listMap.contains(waterId)) {
                            totalWater = totalWater + (dataWaterMap.get(waterId).get(0).getConsumption() - dataWaterMap
                                    .get(waterId).get(dataWaterMap.get(waterId).size() - 1).getConsumption());
                        }
                    }
                }
                map.put("areaId", key);
                map.put("name", areIdForName.get(key));
                map.put("totalElec", totalElec);
                map.put("totalWater", totalWater);
                returnValue.add(map);
            }
            data = returnValue;
            msg = "查询成功！";

        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = null;
            msg = "能耗信息失败！";
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
     * @Title: totalEnergyMoney
     * @Description: 能耗信息
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "费用信息", notes = "根据区域查询能耗信息")
    @RequestMapping(value = "totalEnergyMoney", method = {RequestMethod.POST})
    public RestResultDto totalEnergyMoney(@RequestBody ReportRequest request) {
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
            SysUser sysUser = sysUserService.findById(userId);
            if (null == sysUser) {
                throw new Exception("当前用户不存在！");
            }
            String unitId = null;
            if (sysUser.getRole().equals(SysUserRoleEnum.ADMIN.getKey())) {
                unitId = sysUser.getId();
            } else {
                unitId = sysUser.getParentId();
            }

            UnitRate unitRate = unitRateService.findById(unitId);
            if (null == unitRate) {
                throw new Exception("当前系统未设置费率！");
            }

            RestResultDto restDto = totalEnergy(request);
            List<Object> returnValue = Lists.newArrayList();
            Map<String, Object> mapValue = Maps.newHashMap();
            Map<String, Object> map = null;
            if (restDto.getResult() == 0) {
                List<Object> list = (List<Object>) restDto.getData();
                if (!CollectionUtils.isEmpty(list)) {
                    for (Object obj : list) {
                        mapValue = Maps.newHashMap();
                        map = (Map<String, Object>) obj;
                        mapValue.put("areaId", map.get("areaId"));
                        mapValue.put("name", map.get("name"));
                        mapValue.put("elecMoney", (double) Math.round(
                                (Double.parseDouble(String.valueOf(map.get("totalElec"))) * unitRate.getElectricity())
                                        * 100)
                                / 100);
                        mapValue.put("waterMoney", (double) Math.round(
                                (Double.parseDouble(String.valueOf(map.get("totalWater"))) * unitRate.getWater()) * 100)
                                / 100);
                        returnValue.add(mapValue);
                    }
                }
                data = returnValue;
                msg = "费用报表查询成功！";
            } else {
                result = RestResultDto.RESULT_FAIL;
                exception = restDto.getException();
                data = null;
                msg = "费用报表查询失败！";
            }

        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = null;
            msg = "费用报表查询失败！";
        } finally {
            resultDto.setData(data);
            resultDto.setException(exception);
            resultDto.setMsg(msg);
            resultDto.setResult(result);
        }
        return resultDto;
    }

}
