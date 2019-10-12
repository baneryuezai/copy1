package com.IT.liuJia.service;

import com.IT.liuJia.dao.OrderSettingDao;
import com.IT.liuJia.pojo.OrderSetting;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 包名: com.IT.liuJia.service
 * 作者: JiaLiu
 * 日期: 2019-10-02   22:40
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Override
    /*批量导入*/
    public void doImport(List<OrderSetting> list) {
//        1.遍历
        for (OrderSetting orderSetting : list) {
//        2. 调用dao按日期查询看当前这条数据是否存在,查到了说明存在,否则不存在
//            查到的osInDB就是当前所查日期的数据
//            而orderSetting是当前正在进行的数据
            OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
            if (null != osInDB) {
//        3.这条数据如果存在则更新数量上一次查到的当前日期数据的数字,
                osInDB.setNumber(orderSetting.getNumber());
                orderSettingDao.updateNumber(osInDB);
            } else {
//        // 4. 不存在则插入
                orderSettingDao.add(orderSetting);
            }
        }
    }
    /*
    *
    * 按照日期范围查询
    * */
    @Override
    public List<OrderSetting> getOrderSettingByMonth(String month) {
        // month = 2019-09
        // 按时间范围查询
        String startDate = month + "-01";
        String endDate = month + "-31";
        List<OrderSetting> orderList=orderSettingDao.getOrderSettingByMonth(startDate,endDate);
        return orderList;
    }
    /*通过日期设置预约人数*/
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        OrderSetting os = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        if (os==null) {
//          不存在,则插入数据
            orderSettingDao.add(orderSetting);
        }else{
//            存在 则更新,不需要重新写方法来更新,因为上面已经查到了就是当前的结果,就用这个结果来设置更新数据
                os.setNumber(orderSetting.getNumber());
                orderSettingDao.updateNumber(os);
        }
    }
}
