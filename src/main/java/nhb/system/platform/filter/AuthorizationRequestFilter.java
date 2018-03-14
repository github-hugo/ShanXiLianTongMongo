package nhb.system.platform.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.nhb.utils.nhb_utils.common.StringUtil;

import nhb.system.platform.util.RedisUtils;

@WebFilter(filterName = "authorizationRequestFilter", urlPatterns = "/*")
public class AuthorizationRequestFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String url = httpServletRequest.getServletPath();

		if (!StringUtil.isNullOrEmpty(url)) {
			chain.doFilter(request, response);
			return;
		}

		String id = httpServletRequest.getHeader("Authorization_Id");
		String token = httpServletRequest.getHeader("Authorization_Token");

		if (StringUtil.isNullOrEmpty(token)) {
			throw new ServletException("没有权限！");
		}
		if (RedisUtils.getStr(id).equals(token)) {
			chain.doFilter(request, response);
		}

	}

	@Override
	public void destroy() {

	}


}
