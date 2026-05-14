package com.wn.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @Description:过滤器
 * @Author:kaige
 * @Date:2025/11/22 21:44
 */

@WebFilter("/*")
public class EncodingFilter implements Filter{

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");//设置全局编码
            chain.doFilter(request, response);
        }

}
