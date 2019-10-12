package com.IT.liuJia.dao;

import com.IT.liuJia.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> findByCondition(Order od);

    void add(Order od);

    Map findById4Detail(Integer id);

    Integer findOrderCountByDate(String reportDate);

    Integer findVisitsCountByDate(String reportDate);

    Integer findVisitsCountAfterDate(String monday);
    /**/
    Integer findOrderCountBetweenDate(@Param("startDate") String monday, @Param("endDate") String sunday);


    List<Map<String,Object>> findHotPackage();
}
