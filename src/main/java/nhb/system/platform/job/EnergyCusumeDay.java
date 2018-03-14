package nhb.system.platform.job;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nhb.system.platform.dataaccess.service.common.CommonService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nhb.utils.nhb_utils.common.DateUtil;
import com.nhb.utils.nhb_utils.common.StringUtil;

import nhb.system.platform.dataaccess.service.data.DataElectricityService;
import nhb.system.platform.dataaccess.service.data.DataWaterService;
import nhb.system.platform.dataaccess.service.data.EnergyCusumeService;
import nhb.system.platform.dataaccess.service.device.ReceiptDeviceService;
import nhb.system.platform.dataaccess.service.system.SysAreaService;
import nhb.system.platform.dataaccess.service.system.UnitRateService;
import nhb.system.platform.entity.data.DataElectricity;
import nhb.system.platform.entity.data.DataWater;
import nhb.system.platform.entity.data.EnergyCusume;
import nhb.system.platform.entity.device.ReceiptDevice;
import nhb.system.platform.entity.system.SysArea;
import nhb.system.platform.entity.system.UnitRate;
import nhb.system.platform.enums.SysAreaTypeEnum;
import nhb.system.platform.util.DateTimeUtils;

@Component
public class EnergyCusumeDay {

    @Autowired
    private SysAreaService sysAreaService;

    @Autowired
    private DataElectricityService dataElectricityService;

    @Autowired
    private DataWaterService dataWaterService;

    @Autowired
    private EnergyCusumeService energyCusumeService;

    @Autowired
    private UnitRateService unitRateService;

    @Autowired
    private ReceiptDeviceService receiptDeviceService;

    @Autowired
    private CommonService commonService;

    /**
     * @return void
     * @throws Exception
     * @Title: calcEnergyCusumeDay
     * @Description: 计算日能耗数据
     */
    @Scheduled(cron = "0 10 0 * * ?")
    public void calcEnergyCusumeDayLast() throws Exception {
        // 获取前一天时间（yyyy-mm-dd 00:00:00 - yyyy-mm-dd 23:59:59）
        Map<String, String> dateTimeRange = DateTimeUtils.getDateRange();
        // 昨天的日期
        String beforeDate = DateTimeUtils.getBeforeDate();
        // 开始结束时间
        Date startTime = DateUtil.parse(dateTimeRange.get("beginTime"));
        Date endTime = DateUtil.parse(dateTimeRange.get("endTime"));
        List<ReceiptDevice> devices = receiptDeviceService.findAll();
        List<String> deviceIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(devices)) {
            for (ReceiptDevice device : devices) {
                deviceIds.add(device.getId());
            }
        }
        energyCusumeService.deleteByDateBetweenAndDeviceIdIn(DateUtil.format(startTime), DateUtil.format(endTime),
                deviceIds);
        calcEnergyCusumeDay(startTime, endTime, beforeDate, deviceIds);
    }

    public void calcEnergyCusumeDay(Date startTime, Date endTime, String beforeDate, List<String> deviceIds)
            throws Exception {
        // 查询昨天所有电能数据
        List<DataElectricity> dataElectricities = dataElectricityService
                .findByReadTimeBetweenAndDeviceIdInOrderByReadTimeDesc(startTime, endTime, deviceIds);

        // 对电能数据进行分组
        Map<String, List<DataElectricity>> dataMap = commonService.cacheDataElectricity(dataElectricities);

        // 遍历key
        List<String> keys = Lists.newArrayList();
        if (!MapUtils.isEmpty(dataMap)) {
            Set<String> keySet = dataMap.keySet();
            for (String key : keySet) {
                keys.add(key);
            }
        }

        // 水
        List<DataWater> dataWaters = dataWaterService.findByReadTimeBetweenOrderByReadTimeDesc(startTime, endTime);
        Map<String, List<DataWater>> dataWaterMap = commonService.cacheDataWater(dataWaters);

        if (!MapUtils.isEmpty(dataWaterMap)) {
            Set<String> keySet = dataWaterMap.keySet();
            for (String key : keySet) {
                keys.add(key);
            }
        }

        // 获取所有区域信息 用于获取资源类型
        List<SysArea> sysAreas = sysAreaService.findAll();
        Map<String, SysArea> mapVlaue = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(sysAreas)) {
            for (SysArea area : sysAreas) {
                if (StringUtil.isNullOrEmpty(area.getDeviceId())) {
                    continue;
                }
                mapVlaue.put(area.getDeviceId(), area);
            }
        }

        List<UnitRate> rates = unitRateService.findByStartDateLessThanEqualAndEndDateGreaterThan(beforeDate,
                beforeDate);
        Map<String, UnitRate> rateMap = Maps.newHashMap();
        for (UnitRate rate : rates) {
            rateMap.put(rate.getAreaId(), rate);
        }

        // 数据保存
        List<EnergyCusume> cusumes = Lists.newArrayList();
        EnergyCusume cusume = null;
        Double cusumeDay = (double) 0;
        List<DataElectricity> list = null;
        List<DataWater> waterList = null;
        Double rate = (double) 0;
        for (String key : keys) {
            cusume = new EnergyCusume();
            if (mapVlaue.containsKey(key)) {
                if (mapVlaue.get(key).getResourceType().equals(SysAreaTypeEnum.ELECTRICITY.getKey())) {// 电
                    list = dataMap.get(key);
                    cusumeDay = list.get(0).getKwh() - list.get(list.size() - 1).getKwh();
                    rate = rateMap.get(mapVlaue.get(key).getTopAreaId()).getElectricity();
                } else if (mapVlaue.get(key).getResourceType().equals(SysAreaTypeEnum.WATER.getKey())) {// 水
                    waterList = dataWaterMap.get(key);
                    cusumeDay = waterList.get(0).getConsumption()
                            - waterList.get(waterList.size() - 1).getConsumption();
                    rate = rateMap.get(mapVlaue.get(key).getTopAreaId()).getWater();
                }
                cusume.setCusume((double) Math.round(cusumeDay * 100) / 100);
                cusume.setDeviceId(key);
                cusume.setDate(beforeDate + " 12:00:00");
                cusume.setResourceType(mapVlaue.get(key).getResourceType());
                cusume.setAreaId(mapVlaue.get(key).getId());
                cusume.setParentId(mapVlaue.get(key).getParentId());
                cusume.setTopAreaId(mapVlaue.get(key).getTopAreaId());
                cusume.setMoneySpent(cusume.getCusume() * rate);
                cusumes.add(cusume);
            }
        }

        if (!CollectionUtils.isEmpty(cusumes)) {
            energyCusumeService.saveList(cusumes);
        }

    }

}
