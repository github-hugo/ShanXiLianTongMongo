package nhb.system.platform.dataaccess.service.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nhb.system.platform.dataaccess.service.system.SysAreaService;
import nhb.system.platform.entity.data.DataElectricity;
import nhb.system.platform.entity.data.DataWater;
import nhb.system.platform.entity.data.EnergyCusume;
import nhb.system.platform.entity.system.SysArea;
import nhb.system.platform.enums.SysAreaTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/22.
 */
@Service
public class CommonService {

    @Autowired
    private SysAreaService sysAreaService;

    // 递归获取当前节点 下属所有的设备节点
    public List<String> treeAreaList(String areaId, List<String> deviceIdsChild) {
        List<SysArea> sysAreas = sysAreaService.findByParentId(areaId);
        if (!CollectionUtils.isEmpty(sysAreas)) {
            for (SysArea area : sysAreas) {
                if (!area.getResourceType().equals(SysAreaTypeEnum.AREA.getKey())) {
                    deviceIdsChild.add(area.getDeviceId());
                } else {
                    treeAreaList(area.getId(), deviceIdsChild);
                }
            }
        }
        return deviceIdsChild;
    }

    public Map<String, List<EnergyCusume>> cacheEnergy(List<EnergyCusume> cusumesList) {
        Map<String, List<EnergyCusume>> dataMap = Maps.newHashMap();
        List<EnergyCusume> cusumes = null;
        if (!CollectionUtils.isEmpty(cusumesList)) {
            for (EnergyCusume entity : cusumesList) {
                if (dataMap.containsKey(entity.getDeviceId())) {
                    cusumes = dataMap.get(entity.getDeviceId());
                } else {
                    cusumes = Lists.newArrayList();
                }
                cusumes.add(entity);
                dataMap.put(entity.getDeviceId(), cusumes);
            }
        }
        return dataMap;
    }

    public Map<String, List<DataElectricity>> cacheDataElectricity(List<DataElectricity> dataElectricities) {
        // 对历史数据进行分组 key-deviceId, value-list
        Map<String, List<DataElectricity>> dataMap = Maps.newHashMap();
        List<DataElectricity> electricities = null;
        if (!CollectionUtils.isEmpty(dataElectricities)) {
            for (DataElectricity entity : dataElectricities) {
                if (dataMap.containsKey(entity.getDeviceId())) {
                    electricities = dataMap.get(entity.getDeviceId());
                } else {
                    electricities = Lists.newArrayList();
                }
                electricities.add(entity);
                dataMap.put(entity.getDeviceId(), electricities);
            }
        }
        return dataMap;
    }

    public Map<String, List<DataWater>> cacheDataWater(List<DataWater> dataWaters) {
        Map<String, List<DataWater>> dataWaterMap = Maps.newHashMap();
        List<DataWater> waters = null;
        if (!CollectionUtils.isEmpty(dataWaters)) {
            for (DataWater water : dataWaters) {
                if (dataWaterMap.containsKey(water.getDeviceId())) {
                    waters = dataWaterMap.get(water.getDeviceId());
                } else {
                    waters = Lists.newArrayList();
                }
                waters.add(water);
                dataWaterMap.put(water.getDeviceId(), waters);
            }
        }
        return dataWaterMap;
    }

}
