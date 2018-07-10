package com.enn.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器打印请求地址及参数，解决跨域问题
 * @author zxj
 * @since 2018-06-19
 */
@Component
@WebFilter(urlPatterns = "/*",filterName = "xssFilter")
public class XssFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(XssFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        XssHttpServletRequestWrapper httpRequest = new XssHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;




        /* 解决跨域问题 */
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Headers","Content-Type,ticket");
        httpResponse.setHeader("Access-Control-Max-Age","86400");
        httpResponse.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS");
        filterChain.doFilter(httpRequest, httpResponse);
    }

    @Override
    public void destroy() {

    }
}
