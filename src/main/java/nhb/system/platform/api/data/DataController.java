/**
 * Project Name:nhb-platform
 * File Name:DataController.java
 * Package Name:nhb.system.platform.api.data
 * Date:2017年9月21日下午2:45:18
 * Copyright (c) 2017, lorisun@live.com All Rights Reserved.
 */

package nhb.system.platform.api.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

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
import com.nhb.utils.nhb_utils.common.DateUtil;
import com.nhb.utils.nhb_utils.common.RestResultDto;
import com.nhb.utils.nhb_utils.common.StringUtil;

import io.swagger.annotations.ApiOperation;
import nhb.system.platform.dataaccess.service.data.DataElectricityService;
import nhb.system.platform.dataaccess.service.data.DataWaterService;
import nhb.system.platform.dataaccess.service.system.SysAreaService;
import nhb.system.platform.dto.ExportDataElectricity;
import nhb.system.platform.entity.data.DataElectricity;
import nhb.system.platform.entity.system.SysArea;
import nhb.system.platform.enums.SysAreaTypeEnum;
import nhb.system.platform.request.data.DataRequest;
import nhb.system.platform.util.FileUtil;
import nhb.system.platform.util.JsonMapper;

/**
 * @author xuyahui
 * @ClassName:DataController
 * @Function: TODO ADD FUNCTION.
 * @Reason: TODO ADD REASON.
 * @Date: 2017年9月21日 下午2:45:18
 * @see
 * @since JDK 1.8
 */
@RestController
@RequestMapping("api/v1/data")
public class DataController {

	@Autowired
	private DataElectricityService dataElectricityService;

	@Autowired
	private DataWaterService dataWaterService;
	
	@Autowired
	private SysAreaService sysAreaService;

	@SuppressWarnings({ "rawtypes", "unchecked" }) // 单点查询、多点查询、电表明细
	@ApiOperation(value = "单点查询、多点查询、电表明细", notes = "传List<String> deviceIds,")
	@RequestMapping(value = "findByDeviceIdsAndTime", method = { RequestMethod.POST })
	public RestResultDto findByDeviceIdAndTime(@RequestBody DataRequest dataRequest) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			if (StringUtil.isNullOrEmpty(dataRequest.getDeviceType())
					|| StringUtil.isNullOrEmpty(dataRequest.getStartTime())
					|| StringUtil.isNullOrEmpty(dataRequest.getEndTime()) || (dataRequest.getDeviceIds() == null)) {
				throw new Exception("必要参数不能为空！");
			}

			Map<String, Object> returnValue = Maps.newHashMap();
			List dataList = Lists.newArrayList();
			Long total = 0L;
			Integer totolPage = 0;
			Sort sort = new Sort(Direction.DESC, "readTime");
			Pageable pageable = PageRequest.of(dataRequest.getPage() - 1, dataRequest.getRows(), sort);
			Page pageList = null;

			Date startDate = DateUtil.parse(dataRequest.getStartTime(), DateUtil.DATETIME_FORMAT);
			Date endDate = DateUtil.parse(dataRequest.getEndTime(), DateUtil.DATETIME_FORMAT);

			if ((SysAreaTypeEnum.ELECTRICITY.getKey()).equals(dataRequest.getDeviceType())) {
				pageList = dataElectricityService.findByReadTimeBetweenAndDeviceIdIn(startDate, endDate,
						dataRequest.getDeviceIds(), pageable);
			} else if ((SysAreaTypeEnum.WATER.getKey()).equals(dataRequest.getDeviceType())) {
				pageList = dataWaterService.findByReadTimeBetweenAndDeviceIdIn(startDate, endDate,
						dataRequest.getDeviceIds(), pageable);
			}

			if (pageList != null) {
				Map<String, Object> mapValue = null;
				for (Object obj : pageList.getContent()) {
					mapValue = Maps.newHashMap();
					mapValue = JsonMapper.fromJsonString(JsonMapper.toJsonString(obj), Map.class);
					if ((SysAreaTypeEnum.ELECTRICITY.getKey()).equals(dataRequest.getDeviceType())) {
						mapValue.put("energy", mapValue.get("kwh"));
					}
					mapValue.put("readTime",
							DateUtil.format(new Date(Long.valueOf(String.valueOf(mapValue.get("readTime"))))));
					dataList.add(mapValue);
				}
				total = pageList.getTotalElements();
				totolPage = pageList.getTotalPages();
			}
			returnValue.put("total", total);
			returnValue.put("totolPage", totolPage);
			returnValue.put("currPage", dataRequest.getPage());
			returnValue.put("rows", dataList);
			data = returnValue;
			msg = "查询成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			msg = "查询失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}
	
	
	@ApiOperation(value = "数据导出", 
			notes = "新页面打开网址(page=0且rows=0导出全部;page=1且rows=10导出当前页面10条数据)：http://192.168.1.91:8092/api/v1/data/exportData?areaIds=5a0c1a146b2f070001110d0c,8,9&startTime=2017-11-14&endTime=2017-11-17&page=1&rows=10")
	@RequestMapping(value = "exportData",method={RequestMethod.GET})// 
	public void exportData(@QueryParam("areaIds") String areaIds, @QueryParam("startTime") String startTime,
			@QueryParam("endTime") String endTime,@QueryParam("page") String page,@QueryParam("rows") String rows,HttpServletResponse response) {
		try {
			if(StringUtil.isNullOrEmpty(areaIds)){
				throw new Exception("areaIds不能为空!");
			}
			if(StringUtil.isNullOrEmpty(startTime) || StringUtil.isNullOrEmpty(endTime)){
				throw new Exception("日期条件不能为空!");
			}
			String[] areaIdArray = areaIds.split(",");
			List<String> areaIdList = new ArrayList<String>(Arrays.asList(areaIdArray));
			
			// 获取areaList和deviceIds
			List<SysArea> areaList = sysAreaService.findByIds(areaIdList);
			List<String> deviceIds = Lists.newArrayList();
			Map<String,String> deviceAreaMap = Maps.newHashMap();
			for(SysArea sysArea : areaList){
				if(StringUtil.isNullOrEmpty(sysArea.getDeviceId())){
					continue;
				}
				deviceIds.add(sysArea.getDeviceId());
				SysArea pArea = sysAreaService.findById(sysArea.getParentId());
				SysArea ppArea = sysAreaService.findById(pArea.getParentId());
				SysArea topArea = sysAreaService.findById(sysArea.getTopAreaId());
				deviceAreaMap.put(sysArea.getDeviceId(), topArea.getName()+","+ppArea.getName()+","+sysArea.getName());
			}
			
			// 查询数据
			Date startDate = DateUtil.parse(startTime + " 00:00:00",DateUtil.DATETIME_FORMAT);
			Date endDate = DateUtil.parse(endTime + " 23:59:59",DateUtil.DATETIME_FORMAT);
			List<DataElectricity> list = Lists.newArrayList();
			if("0".equals(page) && "0".equals(rows)){// 导出全部
				list = dataElectricityService.findByReadTimeBetweenAndDeviceIdInOrderByReadTimeDesc(startDate,endDate,deviceIds);
			}else{// 导出当前页
				Sort sort = new Sort(Direction.DESC, "readTime");
				Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(rows), sort);
				Page<DataElectricity> pageList = dataElectricityService.findByReadTimeBetweenAndDeviceIdIn(startDate, endDate,
						deviceIds, pageable);
				list.addAll(pageList.getContent());
			}
			
			// 清洗数据
			List<ExportDataElectricity> exportList = Lists.newArrayList();
			String[] areaNameArray;
			ExportDataElectricity exportDataElectricity;
			for(DataElectricity dataElectricity:list){
				exportDataElectricity = new ExportDataElectricity();
				areaNameArray = deviceAreaMap.get(dataElectricity.getDeviceId()).split(",");
				exportDataElectricity.setCity(areaNameArray[0]);
				exportDataElectricity.setBaseName(areaNameArray[1]);
				exportDataElectricity.setDeviceName(areaNameArray[2]);
				for(Field eField : dataElectricity.getClass().getDeclaredFields()){
					eField.setAccessible(true);
					for(Field exportField : exportDataElectricity.getClass().getDeclaredFields()){
						exportField.setAccessible(true);
						if(exportField.getName().equals(eField.getName())){
							exportField.set(exportDataElectricity, eField.get(dataElectricity));
						}
					}
				}
				exportList.add(exportDataElectricity);
			}
			
			String currenTime = DateUtil.format(new Date(),DateUtil.DATE_FORMAT);
			FileUtil.exportExcel(exportList, "历史数据", "数据一览", ExportDataElectricity.class, "历史数据"+currenTime+".xls", response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
