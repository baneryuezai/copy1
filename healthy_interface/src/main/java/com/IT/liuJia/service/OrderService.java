package com.IT.liuJia.service;

import com.IT.liuJia.exception.MyException;
import com.IT.liuJia.pojo.Order;

import java.util.Map;

public interface OrderService {
    Order submitOrder(Map<String, String> map) throws MyException,Exception;

    Map<String, Object> findById(Integer id) throws Exception;
}
