package cn.coselding.myblog.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**控制不要缓存动态页面的过滤器
 * @author 宇强
 *
 */
public class NoCacheFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse resp,
						 FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		//设置不缓存
		response.setDateHeader("Expires", -1);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		//放行
		chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {

	}
}
