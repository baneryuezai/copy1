package com.IT.liuJia.dao;
import java.lang.String;
import com.IT.liuJia.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {
    /*通过日期查询预约信息*/
    OrderSetting findByOrderDate(Date orderDate);
    /*更新可预约数量*/
    void updateNumber(OrderSetting osInDB);
    /*添加预约信息*/
    void add(OrderSetting orderSetting);
    /*按日期范围查询*/
    List<OrderSetting> getOrderSettingByMonth(@Param("startDate")String startDate,@Param("endDate") String month);
    /*更新已预约人数*/
    void editReservationsByOrderDate(OrderSetting orderSetting);
    //更新可预约人数
    void editNumberByOrderDate(OrderSetting orderSetting);

}
