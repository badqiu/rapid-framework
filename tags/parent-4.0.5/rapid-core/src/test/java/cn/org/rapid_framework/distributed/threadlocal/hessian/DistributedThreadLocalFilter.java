package cn.org.rapid_framework.distributed.threadlocal.hessian;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import cn.org.rapid_framework.distributed.threadlocal.DistributedThreadLocal;
/**
 * 用于从 Http Header 中取回DistributedThreadLocal中的信息,并存放在DistributedThreadLocal中
 * 
 * @author badqiu
 */
public class DistributedThreadLocalFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Enumeration iteator = request.getHeaderNames();
		while(iteator.hasMoreElements()) {
			String key = iteator.nextElement().toString();
			if(key.startsWith(DistributedThreadLocal.DISTRIBUTED_THREAD_LOCAL_KEY_PREFIX)) {
				DistributedThreadLocal.put(key.substring(DistributedThreadLocal.DISTRIBUTED_THREAD_LOCAL_KEY_PREFIX.length()), request.getHeader(key));
			}
		}
		DistributedThreadLocal.onReceivedDistributedThreadLocal();
		
		filterChain.doFilter(request, response);
	}

}
