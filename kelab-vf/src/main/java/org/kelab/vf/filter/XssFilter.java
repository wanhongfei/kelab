package org.kelab.vf.filter;

import org.kelab.vf.filter.wrapper.XssRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author wwhhf
 * @comment 进行xss攻击过滤
 * @since 2016年6月12日
 */
public class XssFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
	                     FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new XssRequestWrapper(
				(HttpServletRequest) request), response);
	}

}
