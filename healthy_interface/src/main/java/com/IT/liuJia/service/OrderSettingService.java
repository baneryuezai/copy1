package com.IT.liuJia.service;

import com.IT.liuJia.pojo.OrderSetting;
import java.util.List;

public interface OrderSettingService {
    /*导入模板*/
    void doImport(List<OrderSetting> list);
    /*通过月份得到预约设置集合*/
    List<OrderSetting> getOrderSettingByMonth(String month);
    /*设置的按钮,就是通过日期查找后再编辑设置*/
    void editNumberByDate(OrderSetting orderSetting);
}
