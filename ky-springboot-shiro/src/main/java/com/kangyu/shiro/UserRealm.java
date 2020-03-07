package com.kangyu.shiro;

import com.kangyu.domain.User;
import com.kangyu.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm{

    /**
     * @Author kangyu
     * @Description
     * 1. 权限校验
     * @Date 15:59 2019/11/10
     * @Param [principalCollection]
     * @return org.apache.shiro.authz.AuthorizationInfo
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权逻辑");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        User doUser = userSerivce.findById(user.getId());
        // 添加权限，实际项目中可以把这个加入到redis中
        simpleAuthorizationInfo.addStringPermission(doUser.getPerms());
        return simpleAuthorizationInfo;
    }

    @Autowired
    private UserService userSerivce;

    /**
     * @Author kangyu
     * @Description
     * 1. 用户登录校验
     * @Date 15:59 2019/11/10
     * @Param [authenticationToken]
     * @return org.apache.shiro.authc.AuthenticationInfo
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行登录逻辑");
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)authenticationToken;
        String username = usernamePasswordToken.getUsername();
        char[] password = usernamePasswordToken.getPassword();
        User user = userSerivce.findByName(username);
        if(user==null){
            //用户名不存在
            return null;//shiro底层会抛出UnKnowAccountException
        }

        return new SimpleAuthenticationInfo(user, password, "");
    }
}
