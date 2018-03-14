package nhb.system.platform.api.system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nhb.system.platform.api.inface.SystemLog;
import nhb.system.platform.dataaccess.service.system.SysUserAreaService;
import nhb.system.platform.dataaccess.service.system.SysUserCollectorService;
import nhb.system.platform.dataaccess.service.system.SysUserService;
import nhb.system.platform.dataaccess.service.system.UserCodeMapService;
import nhb.system.platform.entity.system.SysUser;
import nhb.system.platform.entity.system.SysUserArea;
import nhb.system.platform.entity.system.SysUserCollector;
import nhb.system.platform.entity.system.UserCodeMap;
import nhb.system.platform.request.system.SysUserRequest;
import nhb.system.platform.util.MD5;
import nhb.system.platform.util.RedisUtils;

@RestController
@RequestMapping("api/v1/sys/user")
@Api(value = "系统用户")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysUserAreaService sysUserAreaService;

	@Autowired
	private SysUserCollectorService sysUserCollectorService;

	@Autowired
	private UserCodeMapService userCodeMapService;

	/**
	 * @return RestResultDto
	 * @Title: save
	 * @Description: 保存用户信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "设置用户管理员编号", notes = "")
	@RequestMapping(value = "bindUserCode", method = { RequestMethod.POST })
	public RestResultDto bindUserCode(@RequestBody UserCodeMap request) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			request = userCodeMapService.save(request);
			data = request;
			msg = "保存成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "保存失败！";
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
	 * @Title: save
	 * @Description: 保存用户信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "登出系统", notes = "用户名，密码")
	@RequestMapping(value = "logout", method = { RequestMethod.POST })
	@SystemLog(module = "SYSTEM", methods = "SYSTEM_LOGOUT")
	public RestResultDto logout(@RequestBody SysUser sysUser) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {

			data = true;
			msg = "退出成功！";

		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "登陆失败！";
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
	 * @Title: save
	 * @Description: 保存用户信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "登陆系统", notes = "用户名，密码")
	@RequestMapping(value = "login", method = { RequestMethod.POST })
	@SystemLog(module = "SYSTEM", methods = "SYSTEM_LOGIN")
	public RestResultDto login(@RequestBody SysUser sysUser) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			String loginName = sysUser.getLoginName();
			String password = sysUser.getPassword();
			if (StringUtil.isNullOrEmpty(loginName) || StringUtil.isNullOrEmpty(password)) {
				throw new Exception("用户名或者密码不能为空！");
			}
			SysUser user = sysUserService.findByLoginName(loginName);
			if (null == user || !user.getPassword().equals(MD5.md5Hex(password))) {
				throw new Exception("用户名或者密码错误！");
			}
			// 更新登陆信息
			Date nowTime = new Date();
			user.setLastLoginTime(nowTime);
			user = sysUserService.saveOrUpdate(user);
			Map<String, Object> mapValue = Maps.newHashMap();
			// 保存token
			saveTokenInfo(user, mapValue);
			data = mapValue;
			msg = "登陆成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = false;
			msg = "登陆失败！";
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
	 * @Title: saveTokenInfo
	 * @Description: 设置TOKEN信息
	 */
	private void saveTokenInfo(SysUser user, Map<String, Object> mapValue) {
		String encryptMsg = "nhb" + user.getId() + user.getPassword();
		String token = MD5.md5Hex(encryptMsg);
		RedisUtils.set(user.getId(), token, 0);
		RedisUtils.set("Authorization_Id", user.getId(), 0);
		Map<String, Object> loginMap = Maps.newHashMap();
		loginMap.put("uuid", user.getId());
		loginMap.put("token", token);
		loginMap.put("name", user.getName());
		loginMap.put("role", user.getRole());
		loginMap.put("loginName", user.getLoginName());
		mapValue.put("loginMsg", loginMap);
	}

	/**
	 * @return RestResultDto
	 * @Title: save
	 * @Description: 保存用户信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "新增用户", notes = "必传：parentId")
	@RequestMapping(value = "save", method = { RequestMethod.POST })
	@SystemLog(module = "SYSTEM", methods = "SYSTEM_ADD_USER")
	public RestResultDto save(@RequestBody SysUser sysUser) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			SysUser users = sysUserService.findByLoginName(sysUser.getLoginName());
			if (null != users) {
				throw new Exception("用户名已存在！");
			}
			// 查询其父节点
			SysUser parent = null;
			if (!StringUtil.isNullOrEmpty(sysUser.getParentId())) {
				parent = sysUserService.findById(sysUser.getParentId());
			}
			Date nowTime = new Date();
			sysUser.setId(UUID.randomUUID().toString());
			// 初始密码123456
			sysUser.setPassword(MD5.md5Hex("123456"));
			sysUser.setCreateTime(nowTime);
			sysUser.setBeenDelete(0);
			// 设置权限
			if (null == parent) {
				sysUser.setParentId("0");
			}
			SysUser user = sysUserService.saveOrUpdate(sysUser);
			msg = "用户保存成功！";
			data = user;
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "用户保存失败！";
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
	 * @Title: resetPassword
	 * @Description: 重置用户密码
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "重置用户密码", notes = "必传参数：id 和 parentId")
	@RequestMapping(value = "resetPassword", method = { RequestMethod.POST })
	@SystemLog(module = "SYSTEM", methods = "SYSTEM_RESET_PASSWORD")
	public RestResultDto resetPassword(@RequestBody SysUserRequest request) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			String parentId = request.getParentId();
			String id = request.getId();
			if (StringUtil.isNullOrEmpty(id) || StringUtil.isNullOrEmpty(parentId)) {
				throw new Exception("用户id不能为空");
			}
			SysUser parent = sysUserService.findById(parentId);
			SysUser son = sysUserService.findById(id);
			if (!parent.getId().equals(son.getParentId())) {
				throw new Exception("您没有权限重置该用户密码！");
			}
			son.setPassword(MD5.md5Hex("123456"));
			sysUserService.saveOrUpdate(son);
			msg = "密码重置成功！";
			data = true;
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "密码重置失败！";
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
	 * @Title: updatePassword
	 * @Description: 修改密码信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "修改密码信息", notes = "修改密码信息")
	@RequestMapping(value = "updatePassword", method = { RequestMethod.POST })
	@SystemLog(module = "SYSTEM", methods = "SYSTEM_MODIFY_PASSWORD")
	public RestResultDto updatePassword(@RequestBody SysUserRequest request) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			String userId = request.getId();
			String oldPass = request.getOldPass();
			String newPass = request.getNewPass();
			if (StringUtil.isNullOrEmpty(userId) || StringUtil.isNullOrEmpty(newPass)
					|| StringUtil.isNullOrEmpty(oldPass)) {
				throw new Exception("必要参数不能为空！");
			}
			SysUser sysUser = sysUserService.findById(userId);
			if (!sysUser.getPassword().equals(MD5.md5Hex(oldPass))) {
				throw new Exception("原密码错误！");
			}
			sysUser.setPassword(MD5.md5Hex(newPass));
			Date nowTime = new Date();
			sysUser.setUpdateTime(nowTime);
			sysUser = sysUserService.saveOrUpdate(sysUser);
			msg = "密码修改成功！";
			data = sysUser;
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "密码修改失败！";
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
	 * @Title: update
	 * @Description: 保存用户信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "修改用户信息", notes = "修改用户信息")
	@RequestMapping(value = "update", method = { RequestMethod.POST })
	@SystemLog(module = "SYSTEM", methods = "SYSTEM_MODIFY_USER")
	public RestResultDto update(@RequestBody SysUser sysUser) {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			Date nowTime = new Date();
			SysUser user = sysUserService.findById(sysUser.getId());
			sysUser.setBeenDelete(user.getBeenDelete());
			sysUser.setPassword(user.getPassword());
			sysUser.setUpdateTime(nowTime);
			sysUser.setCreateTime(user.getCreateTime());
			sysUser = sysUserService.saveOrUpdate(sysUser);
			msg = "用户修改成功！";
			data = sysUser;
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "用户修改失败！";
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
	 * @Description: 条件查询用户列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "查询用户列表", notes = "查询用户列表")
	@RequestMapping(value = "findList", method = { RequestMethod.POST })
	public RestResultDto findList(@RequestBody SysUserRequest sysUserRequest) throws Exception {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			String userId = sysUserRequest.getId();
			if (StringUtil.isNullOrEmpty(userId)) {
				throw new Exception("用户id不能为空！");
			}
			SysUser sysUser = sysUserService.findById(userId);
			Map<String, Object> returnValue = Maps.newHashMap();
			Long total = 0L;
			Integer totolPage = 0;
			List<SysUser> users = Lists.newArrayList();

			String parentId = null;

			parentId = sysUser.getId();
			Sort sort = new Sort(Direction.DESC, "createTime");
			Pageable pageable = PageRequest.of(sysUserRequest.getPage() - 1, sysUserRequest.getRows(), sort);

			Page<SysUser> pageList = sysUserService.findByParentIdAndBeenDelete(parentId, 0, pageable);
			if (pageList != null) {
				users = pageList.getContent();
				total = pageList.getTotalElements();
				totolPage = pageList.getTotalPages();
			}
			returnValue.put("total", total);
			returnValue.put("totolPage", totolPage);
			returnValue.put("currPage", sysUserRequest.getPage());
			returnValue.put("rows", users);
			data = returnValue;
			msg = "查询用户列表成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "查询用户列表失败！";
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
	 * @Title: findById
	 * @Description: 根据id查询用户
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "根据用户id获取用户信息", notes = "传参：id")
	@RequestMapping(value = "findById", method = { RequestMethod.POST })
	public RestResultDto findById(@RequestBody SysUserRequest sysUserRequest) throws Exception {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			SysUser sysUser = sysUserService.findById(sysUserRequest.getId());
			data = sysUser;
			msg = "根据id查询用户成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = null;
			msg = "根据id查询用户失败！";
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
	 * @Description: 删除
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "删除用户", notes = "根据id删除用户信息")
	@RequestMapping(value = "delete", method = { RequestMethod.POST })
	@SystemLog(module = "SYSTEM", methods = "SYSTEM_DELETE_USER")
	public RestResultDto delete(@RequestBody SysUserRequest sysUserRequest) throws Exception {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {

			String userId = sysUserRequest.getId();
			if (!StringUtil.isNullOrEmpty(userId)) {
				throw new Exception("要删除的id不能未空！");
			}

			List<SysUserArea> areas = sysUserAreaService.findByUserId(userId);
			if (!CollectionUtils.isEmpty(areas)) {
				throw new Exception("该用户下挂有区域信息，请解绑后删除！");
			}
			Page<SysUserCollector> collectors = sysUserCollectorService.findByUserId(userId, null);
			if (null != collectors) {
				throw new Exception("该用户下挂有采集器设备，请解绑后删除！");
			}

			Date nowTime = new Date();
			SysUser sysUser = sysUserService.findById(sysUserRequest.getId());
			sysUser.setBeenDelete(1);
			sysUser.setDeleteTime(nowTime);
			sysUserService.saveOrUpdate(sysUser);
			data = true;
			msg = "删除用户成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = false;
			msg = "删除用户失败！";
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
	 * @Title: userCounts
	 * @Description: 用户数量
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "用户数量", notes = "获取该系统用户数量")
	@RequestMapping(value = "userCounts", method = { RequestMethod.POST })
	public RestResultDto userCounts(@RequestBody SysUserRequest sysUserRequest) throws Exception {
		RestResultDto resultDto = new RestResultDto();
		Integer result = RestResultDto.RESULT_SUCC;
		String msg = null;
		Object data = null;
		String exception = null;
		try {
			String userId = sysUserRequest.getId();
			if (StringUtil.isNullOrEmpty(userId)) {
				throw new Exception("用户id不能为空！");
			}
			SysUser sysUser = sysUserService.findById(sysUserRequest.getId());
			if (null == sysUser) {
				throw new Exception("当前用户不存在！");
			}
			long userCounts = 1L;
			List<SysUser> users = sysUserService.findByParentId(userId);
			if (null != users) {
				userCounts = userCounts + users.size();
			}
			data = userCounts;
			msg = "获取用户数量成功！";
		} catch (Exception e) {
			result = RestResultDto.RESULT_FAIL;
			exception = e.getMessage();
			data = false;
			msg = "获取用户数量失败！";
		} finally {
			resultDto.setData(data);
			resultDto.setException(exception);
			resultDto.setMsg(msg);
			resultDto.setResult(result);
		}
		return resultDto;
	}

}
