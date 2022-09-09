//package com.lessing.equipment.config;
//
//import com.lessing.equipment.common.utils.AuthFilter;
//import com.lessing.equipment.common.utils.AuthRealm;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//import javax.servlet.Filter;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@Configuration
//public class ShiroConfig {
//
//    @Bean
//    public DefaultWebSecurityManager securityManager(AuthRealm authRealm) {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(authRealm);
//        securityManager.setRememberMeManager(null);
//        return securityManager;
//    }
//
//
//    @Bean
//    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
//        shiroFilter.setSecurityManager(securityManager);
//        //oauth过滤
//        Map<String, Filter> filters = new HashMap<>();
//        filters.put("utils", new AuthFilter());
//        shiroFilter.setFilters(filters);
//        Map<String, String> filterMap = new LinkedHashMap<>();
//        filterMap.put("/sys/login", "anon");
//        shiroFilter.setFilterChainDefinitionMap(filterMap);
//
//        return shiroFilter;
//    }
//
//
//
//    @Bean
//    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
//        advisor.setSecurityManager(securityManager);
//        return advisor;
//    }
//
//}
