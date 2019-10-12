package com.IT.liuJia.service;

import com.IT.liuJia.pojo.Permission;
import com.IT.liuJia.pojo.Role;
import com.IT.liuJia.pojo.User;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 包名: com.IT.liuJia.service
 * 作者: JiaLiu
 * 日期: 2019-10-09   10:19
 */
/*权限控制  SecurityUserServiceImpl   加在web里,*/
//指定配置文件的bean  就一定能找到这个bean了
@Service("securityUserServiceImpl")
public class SecurityUserServiceImpl implements UserDetailsService {
    @Reference
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        1.认证
//            登录---从数据库查找用户名,看能否查到,查到说明存在然后校验密码,查不到说明没有返回null
            User user=userService.findByUsername(username);
        if (null==user) {
//            空的  用户不存在  返回null
            return null;
        }
        /*if (null!=user){
            因为只有存在和不存在两种情况,所以不需要写else了,
        }*/
//        2.授权
//        /*
//              1.先建一个权限集合
//              2.拿到用户所拥有的角色
//              3.把角色添加到权限集合中去
//          */
        // 用户存在
        List<GrantedAuthority> authorityList = new ArrayList<>();
        // 用户拥有的 角色
        Set<Role> userRoles= user.getRoles();
        if (null!=userRoles) {
            GrantedAuthority authority=null;
            for (Role userRole : userRoles) {
//                创建角色
                authority=new SimpleGrantedAuthority(userRole.getKeyword());
                // 添加用户的角色
                authorityList.add(authority);
                // 判断角色下是否有权限
                if (null!=userRole.getPermissions()) {
                    for (Permission permission : userRole.getPermissions()) {
                        authority=new SimpleGrantedAuthority(permission.getKeyword());
                        authorityList.add(authority);
                    }
                }
            }
        }
        // user.getUsername(),user.getPassword() 让security帮我们校验，调用encoder的matches方法 encoder-> <bean id=encoder/>
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorityList);
    }
}
