package com.IT.liuJia.dao;

import com.IT.liuJia.pojo.User;

public interface UserDao {

    User findByUsername(String username);
}
