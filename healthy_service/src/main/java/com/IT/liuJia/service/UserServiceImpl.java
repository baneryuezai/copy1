package com.IT.liuJia.service;

import com.IT.liuJia.dao.UserDao;
import com.IT.liuJia.pojo.User;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 包名: com.IT.liuJia.service
 * 作者: JiaLiu
 * 日期: 2019-10-09   15:23
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;
    /*
    * 用户的认证与授权,用户的所有权限
    *
    * */
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
