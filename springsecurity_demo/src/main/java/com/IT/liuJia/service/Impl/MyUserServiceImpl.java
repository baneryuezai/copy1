package com.IT.liuJia.service.Impl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.IT.liuJia.pojo.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.ArrayList;
import java.util.List;
/**
 * 包名: com.IT.liuJia.service.Impl
 * 作者: JiaLiu
 * 日期: 2019-10-08   09:45
 */

/**
 * 登陆认证时，security就会来调用这个方法
 *
 * @param
 * @return
 * @throws UsernameNotFoundException
 */

//  这篇的代码作用,查找数据库,找到一个用户,如果用户不为空,做出一个权限集合,然后创建某一个角色或者是权限名
//    把这个添加到集合中去,那么这个用户也就有了这个权限
public class MyUserServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        调用dao方法
        User user = findByusername(username);
        if (null != user) {
            // 密码校验 交给security
            //String username, 登陆用户名
            // String password, 密码 从数据库获取
            //Collection<? extends GrantedAuthority> authorities 用户的权限集合
            List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
            // 权限，这里可以是角色名也可以是权限名   这里是自定义的  不是从数据库中查出来的
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
            authorityList.add(authority);
            // security登陆用户信息, authorityList设置了用户的权限集合
            org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(username,user.getPassword(), authorityList);
            return user1;
        } else {
            return null;

        }
    }

    //    模拟dao层来被service层调用   模拟数据库被查找
    private User findByusername(String username) {
        User user = null;
        if ("admin".equals(username)) {
            user = new User();
            user.setUsername("admin");
            user.setPassword("$2a$10$Xa0j85bNoOY2F7JI11V1BeWYrGJgsJtucu19DzDmMXT7c5xpS.3/S");
            user.setId(9527);
        }
        return user;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        System.out.println(encoder.encode("admin"));
//        System.out.println(encoder.encode("12345"));
//        $2a$10$upeGli0OUt/EaXz9s.YsceOLyCWeuKxze7EFqqBkAow/BaXpZ7wp6
//        $2a$10$KW.adfX9HAa07P/KwKVauOhDgkeLgWX3a.hAr9Cxzj2Tkn1tVX4ka
        // 校验
        //rawPassword    没有加密 明文
        //encodedPassword  加密后的密文
//        System.out.println(encoder.matches("12345", "$2a$10$upeGli0OUt/EaXz9s.YsceOLyCWeuKxze7EFqqBkAow/BaXpZ7wp6"));
//        System.out.println(encoder.matches("12345", "$2a$10$KW.adfX9HAa07P/KwKVauOhDgkeLgWX3a.hAr9Cxzj2Tkn1tVX4ka"));
    }
}
