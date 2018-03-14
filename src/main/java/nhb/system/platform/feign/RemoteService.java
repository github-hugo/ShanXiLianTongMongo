package nhb.system.platform.feign;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nhb.utils.nhb_utils.common.RestResultDto;

@FeignClient(value = "collector-soft")
public interface RemoteService {

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/remote/ctrl/turnswitch", method = RequestMethod.POST)
	RestResultDto remoteDevice(@RequestBody Map<String, Object> params);

}
