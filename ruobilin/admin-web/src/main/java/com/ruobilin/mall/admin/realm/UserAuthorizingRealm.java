package com.ruobilin.mall.admin.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.ruobilin.mall.entity.User;
import com.ruobilin.mall.service.UserService;

public class UserAuthorizingRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;
	
	@Override  
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {  
        String id = (String) principalCollection.getRealmNames().iterator().next();  
        if(id != null) {
        	User u = userService.getById(Long.valueOf(id));
            SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
            if (u.getUserType().intValue() == 8) {
            	info.addRole("operator");
            }
            else if (u.getUserType().intValue() == 9) {
            	info.addRole("admin");
            }
            else {
            	info.addRole("guest");
            }
            
            return info;
        }  
        return null;  
    }  
  
    /** 
     * 登录认证; 
     */  
    @Override  
    protected AuthenticationInfo doGetAuthenticationInfo(  
            AuthenticationToken authenticationToken) throws AuthenticationException {  
    	UsernamePasswordToken token=(UsernamePasswordToken) authenticationToken;
        String pwd = new String(token.getPassword());
        User u = userService.login(token.getUsername(), pwd);
        if (u != null && u.getPassword().equals(pwd)) {
        	return new SimpleAuthenticationInfo(token.getUsername(), token.getPassword(), String.valueOf(u.getId()));
        }  
        return null;
    }  
}
