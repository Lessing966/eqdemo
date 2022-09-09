package com.lessing.equipment.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class JwtInterceptorConfig implements WebMvcConfigurer  {

    @Autowired
    private JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    /**
     * 注册自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/sys/**","/eq/**")
                .excludePathPatterns("/sys/login","/sys/downloadExcel","/sys/eq/eqDownloadExcel","/eq/sdk/xiazai","/message/mge");
    }

}
