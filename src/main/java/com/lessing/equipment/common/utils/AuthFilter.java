//package com.lessing.equipment.common.utils;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class AuthFilter extends AuthenticatingFilter {
//
//    // 定义jackson对象
//    private static final ObjectMapper MAPPER = new ObjectMapper();
//
//    /**
//     * 生成自定义token
//     *
//     * @param request
//     * @param response
//     * @return
//     * @throws Exception
//     */
//    @Override
//    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
//        //获取请求token
//        String token = this.getRequestToken((HttpServletRequest) request);
//
//        return new AuthToken(token);
//    }
//
//
//    /**
//     * 步骤1.所有请求全部拒绝访问
//     *
//     * @param request
//     * @param response
//     * @param mappedValue
//     * @return
//     */
//    @Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())) {
//            return true;
//        }
//        return false;
//    }
//
//
//
//    @Override
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        //获取请求token，如果token不存在，直接返回
//        String token = this.getRequestToken((HttpServletRequest) request);
//        if (StringUtils.isBlank(token)) {
//            HttpServletResponse httpResponse = (HttpServletResponse) response;
//            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
//            httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
//            httpResponse.setCharacterEncoding("UTF-8");
//            Map<String, Object> result = new HashMap<>();
//            result.put("status", 400);
//            result.put("msg", "请先登录");
//            String json = MAPPER.writeValueAsString(result);
//            httpResponse.getWriter().print(json);
//            return false;
//        }
//        return executeLogin(request, response);
//    }
//
//
//    /**
//     * token失效时候调用
//     */
//    @Override
//    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        httpResponse.setContentType("application/json;charset=utf-8");
//        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
//        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
//        httpResponse.setCharacterEncoding("UTF-8");
//        try {
//            //处理登录失败的异常
//            Throwable throwable = e.getCause() == null ? e : e.getCause();
//            Map<String, Object> result = new HashMap<>();
//            result.put("status", 400);
//            result.put("msg", "登录凭证已失效，请重新登录");
//            String json = MAPPER.writeValueAsString(result);
//            httpResponse.getWriter().print(json);
//        } catch (IOException e1) {
//        }
//        return false;
//    }
//
//
//
//    /**
//     * 获取请求的token
//     */
//    private String getRequestToken(HttpServletRequest httpRequest){
//        //从header中获取token
//        String token = httpRequest.getHeader("token");
//
//        //如果header中不存在token，则从参数中获取token
//        if(StringUtils.isBlank(token)){
//            token = httpRequest.getParameter("token");
//        }
//
//        return token;
//    }
//}
