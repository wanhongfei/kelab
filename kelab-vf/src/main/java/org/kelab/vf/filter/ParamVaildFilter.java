package org.kelab.vf.filter;

import org.kelab.vf.filter.wrapper.ParamVaildRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by hongfei.whf on 2017/3/8.
 */
public class ParamVaildFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
	                     FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new ParamVaildRequestWrapper(
				(HttpServletRequest) request), response);
	}

	@Override
	public void destroy() {

	}
}
