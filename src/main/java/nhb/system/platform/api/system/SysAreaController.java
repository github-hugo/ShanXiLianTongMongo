package nhb.system.platform.api.system;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nhb.utils.nhb_utils.common.RestResultDto;
import com.nhb.utils.nhb_utils.common.StringUtil;
import io.swagger.annotations.ApiOperation;
import nhb.system.platform.api.inface.SystemLog;
import nhb.system.platform.dataaccess.service.system.SysAreaService;
import nhb.system.platform.dataaccess.service.system.SysUserAreaService;
import nhb.system.platform.dataaccess.service.system.SysUserService;
import nhb.system.platform.entity.system.SysArea;
import nhb.system.platform.entity.system.SysUser;
import nhb.system.platform.entity.system.SysUserArea;
import nhb.system.platform.enums.SysAreaTypeEnum;
import nhb.system.platform.enums.SysUserRoleEnum;
import nhb.system.platform.request.system.SysAreaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author XS guo
 * @ClassName: SysAreaController
 * @Description: 区域信息Api
 * @date 2017年9月21日 下午8:47:56
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("api/v1/sys/area")
public class SysAreaController {

    @Autowired
    private SysAreaService sysAreaService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserAreaService sysUserAreaService;

    /**
     * @return RestResultDto
     * @Title: save
     * @Description: 保存区域信息
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "区域信息保存", notes = "区域信息保存")
    @RequestMapping(value = "save", method = {RequestMethod.POST})
    @SystemLog(module = "SYSTEM", methods = "SYSTEM_ADD_AREA")
    public RestResultDto save(@RequestBody SysArea sysArea) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {
            Date nowTIme = new Date();
            sysArea.setCreateTime(nowTIme);
            if (sysArea.getParentId() == null) {
                sysArea.setParentId("0");
                sysArea.setTopAreaId("0");
            }
            SysUser sysUser = sysUserService.findById(sysArea.getManagerId());
            if (!sysUser.getRole().equals(SysUserRoleEnum.ADMIN.getKey())) {
                sysArea.setManagerId(sysUser.getParentId());
            }

            SysArea area = sysAreaService.save(sysArea);

            //如果 不是管理员，则自动绑定该用户和区域
            if (!sysUser.getRole().equals(SysUserRoleEnum.ADMIN.getKey())) {
                SysUserArea sysUserArea = new SysUserArea();
                sysUserArea.setUserId(sysUser.getId());
                sysUserArea.setAreaId(area.getId());
                sysUserAreaService.save(sysUserArea);
            }
            msg = "区域信息保存成功！";
            data = area;
        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = null;
            msg = "区域信息保存失败！";
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
     * @Title: bindDevice
     * @Description: 绑定设备
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "区域-设备绑定", notes = "区域-设备绑定")
    @RequestMapping(value = "bindDevice", method = {RequestMethod.POST})
    @SystemLog(module = "SYSTEM", methods = "SYSTEM_BIND_AREADEVICE")
    public RestResultDto bindDevice(@RequestBody SysAreaRequest request) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {

            String areaId = request.getId();
            String deviceId = request.getDeviceId();
            if (null == areaId) {
                throw new Exception("区域id不能为空！");
            }
            if (StringUtil.isNullOrEmpty(deviceId)) {
                throw new Exception("要绑定的设备不能为空");
            }
            SysArea sysArea = sysAreaService.findById(areaId);
            if (null != sysArea) {
                sysArea.setDeviceId(deviceId);
                sysAreaService.save(sysArea);
                msg = "绑定设备成功！";
                data = sysArea;
            }

        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = null;
            msg = "绑定设备失败！";
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
     * @Title: findList
     * @Description: 根据用户id查询下属区域列表
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "查询用户名下区域列表", notes = "查询用户名下区域列表")
    @RequestMapping(value = "findList", method = {RequestMethod.POST})
    public RestResultDto findList(@RequestBody SysAreaRequest request) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {
            List<SysArea> sysAreas = Lists.newArrayList();
            String ownerId = request.getOwnerId();
            if (StringUtil.isNullOrEmpty(ownerId)) {
                throw new Exception("用户id不能为空！");
            }
            SysUser sysUser = sysUserService.findById(ownerId);
            if (sysUser.getRole().equals(SysUserRoleEnum.SUPERADMIN.getKey())) {
                sysAreas = sysAreaService.findAll();
            } else if (sysUser.getRole().equals(SysUserRoleEnum.ADMIN.getKey())) {// 管理员
                sysAreas = sysAreaService.findByManagerId(ownerId);
            } else {
                List<SysUserArea> userAreas = sysUserAreaService.findByUserId(ownerId);
                if (!CollectionUtils.isEmpty(userAreas)) {
                    List<String> areaIds = Lists.newArrayList();
                    for (SysUserArea userArea : userAreas) {
                        areaIds.add(userArea.getAreaId());
                    }
                    sysAreas = sysAreaService.findByIds(areaIds);
                }
            }
            data = sysAreas;
            msg = "查询区域列表成功！";
        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = null;
            msg = "查询区域列表失败！";
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
     * @Title: delete
     * @Description:删除区域
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "删除区域", notes = "删除区域")
    @RequestMapping(value = "delete", method = {RequestMethod.POST})
    @SystemLog(module = "SYSTEM", methods = "SYSTEM_DELETE_AREA")
    public RestResultDto delete(@RequestBody SysAreaRequest request) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {
            String areaId = request.getId();
            if (null == areaId) {
                throw new Exception("区域id不能为空！");
            }
            List<SysArea> sysAreas = sysAreaService.findByParentId(areaId);
            if (!CollectionUtils.isEmpty(sysAreas)) {
                throw new Exception("请先删除下属节点区域！");
            }

            List<SysUserArea> sysUserAreas = sysUserAreaService.findByAreaId(areaId);
            if (!CollectionUtils.isEmpty(sysUserAreas)) {
                throw new Exception("请先解除该区域与用户的绑定！");
            }

            sysAreaService.deleteById(areaId);
            data = true;
            msg = "删除成功！";
        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = false;
            msg = "删除失败失败！";
        } finally {
            resultDto.setData(data);
            resultDto.setException(exception);
            resultDto.setMsg(msg);
            resultDto.setResult(result);
        }
        return resultDto;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "获取区域类型", notes = "获取区域类型")
    @RequestMapping(value = "getSysAreaType", method = {RequestMethod.POST})
    public RestResultDto getSysAreaType() {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {
            List<Object> returnValue = Lists.newArrayList();
            Map<String, Object> mapValue = null;
            for (SysAreaTypeEnum value : SysAreaTypeEnum.values()) {
                mapValue = Maps.newHashMap();
                mapValue.put("key", value.getKey());
                mapValue.put("value", value.getValue());
                returnValue.add(mapValue);
            }
            data = returnValue;
            msg = "获取区域类型成功！";
        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = false;
            msg = "获取区域类型失败！";
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
     * @Title: findBySiteTypeAndUserId
     * @Description: 根据站址类型和userI的查询站址
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "根据用户id和站址类型获取区域信息", notes = "参数：")
    @RequestMapping(value = "findBySiteTypeAndUserId", method = {RequestMethod.POST})
    public RestResultDto findBySiteTypeAndUserId(@RequestBody SysAreaRequest request) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {

            Map<String, Object> returnValue = Maps.newHashMap();
            Long total = 0L;
            Integer totolPage = 0;
            String ownerId = request.getOwnerId();
            String siteType = request.getSiteType();

            List<SysArea> areas = Lists.newArrayList();
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getRows());

            SysUser sysUser = sysUserService.findById(ownerId);
            Page<SysArea> pageList = null;
            if (sysUser.getRole().equals(SysUserRoleEnum.ADMIN.getKey())) {
                // 管理员
                pageList = sysAreaService.findByManagerIdAndSiteType(ownerId, siteType, pageable);
            } else {
                List<SysUserArea> sysUserAreas = sysUserAreaService.findByUserId(ownerId);
                List<String> areaIds = Lists.newArrayList();
                if (!CollectionUtils.isEmpty(sysUserAreas)) {
                    for (SysUserArea userArea : sysUserAreas) {
                        areaIds.add(userArea.getAreaId());
                    }
                }
                if (!CollectionUtils.isEmpty(areaIds)) {
                    pageList = sysAreaService.findByIdInAndSiteType(areaIds, siteType, pageable);
                }
            }

            if (null != pageList) {
                areas = pageList.getContent();
                total = pageList.getTotalElements();
                totolPage = pageList.getTotalPages();
            }

            returnValue.put("total", total);
            returnValue.put("totolPage", totolPage);
            returnValue.put("currPage", request.getPage());
            returnValue.put("rows", areas);

            data = returnValue;
            msg = "查询站址信息成功！";

        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = null;
            msg = "查询站址信息失败！";
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
     * @Title: findAreaBasicInfo
     * @Description: 查询区域的基本信息
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "查询区域的基本信息", notes = "参数：areaId")
    @RequestMapping(value = "findAreaBasicInfo", method = {RequestMethod.POST})
    public RestResultDto findAreaBasicInfo(@RequestBody SysAreaRequest request) {
        RestResultDto resultDto = new RestResultDto();
        Integer result = RestResultDto.RESULT_SUCC;
        String msg = null;
        Object data = null;
        String exception = null;
        try {

            String areaId = request.getId();
            if (!StringUtil.isNullOrEmpty(areaId)) {
                throw new Exception("要查询的区域循序不能未空");
            }

            SysArea sysArea = sysAreaService.findById(areaId);
            data = sysArea;
            msg = "查询成功！";

        } catch (Exception e) {
            result = RestResultDto.RESULT_FAIL;
            exception = e.getMessage();
            data = null;
            msg = "查询失败！";
        } finally {
            resultDto.setData(data);
            resultDto.setException(exception);
            resultDto.setMsg(msg);
            resultDto.setResult(result);
        }
        return resultDto;
    }

}
