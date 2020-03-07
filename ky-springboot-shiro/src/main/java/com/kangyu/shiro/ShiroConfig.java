package com.kangyu.shiro;


import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

	/**
	 * @Author kangyu
	 * @Description
	 * 1.  创建Realm、配置自定义的权限登录器
	 * @Date 16:04 2019/11/10
	 * @Param []
	 * @return com.kangyu.shiro.UserRealm
	 **/
	@Bean(name="userRealm")
	public UserRealm getRealm(){
		System.out.println("---getRealm---");
		return new UserRealm();
	}

    @Bean("sessionManager")
    public SessionManager sessionManager(){
        //增加将sessionId存放到redis缓存的方法 2018-6-12
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置session过期时间为1小时(单位：毫秒)，默认为30分钟
        sessionManager.setGlobalSessionTimeout(300 * 1000);
        return sessionManager;
    }

	/**
	 * 创建DefaultWebSecurityManager
	 * 配置核心安全事务管理器
	 */
	@Bean(name="securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm){
		System.out.println("---getDefaultWebSecurityManager---");
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		//关联realm
		securityManager.setRealm(userRealm);
		// session管理器
        securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	/**
	 * 创建ShiroFilterFactoryBean
	 * 过滤器工厂类
	 */
	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager){
		System.out.println("---getShiroFilterFactoryBean---");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		//设置安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 设置过滤权限
		//添加Shiro内置过滤器
		/**
		 * Shiro内置过滤器，可以实现权限相关的拦截器
		 *    常用的过滤器：
		 *       anon: 无需认证（登录）可以访问
		 *       authc: 必须认证才可以访问
		 *       user: 如果使用rememberMe的功能可以直接访问
		 *       perms： 该资源必须得到资源权限才可以访问
		 *       role: 该资源必须得到角色权限才可以访问
		 */
		Map<String,String> filterMap = new LinkedHashMap<String,String>();
		/*filterMap.put("/add", "authc");
		filterMap.put("/update", "authc");*/
		filterMap.put("/testThymeleaf", "anon");
		//放行login.html页面
		filterMap.put("/login", "anon");
		//授权过滤器
		//注意：当前授权拦截后，shiro会自动跳转到未授权页面
		filterMap.put("/add", "perms[user:add]");
		filterMap.put("/update", "perms[user:update]");
		filterMap.put("/*", "user");
		//修改调整的登录页面
		shiroFilterFactoryBean.setLoginUrl("/toLogin");
		//设置未授权提示页面
		shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		return shiroFilterFactoryBean;
	}

}
