package com.IT.liuJia.service;

import com.IT.liuJia.pojo.User;

public interface UserService {
/*
*
* 认证与授权
* */
    User findByUsername(String username);
}
