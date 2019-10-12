package com.IT.liuJia.service;

import com.IT.liuJia.constant.MessageConstant;
import com.IT.liuJia.dao.MemberDao;
import com.IT.liuJia.dao.OrderDao;
import com.IT.liuJia.dao.OrderSettingDao;
import com.IT.liuJia.exception.MyException;
import com.IT.liuJia.pojo.Member;
import com.IT.liuJia.pojo.Order;
import com.IT.liuJia.pojo.OrderSetting;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 包名: com.IT.liuJia.service
 * 作者: JiaLiu
 * 日期: 2019-10-05   10:04
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    @Transactional
    public Order submitOrder(Map<String, String> map) throws MyException, Exception {
//        判断是否可以预约,通过日期来判断
        String orderDate = map.get("orderDate");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(orderDate);
        } catch (ParseException e) {
            e.printStackTrace();
            // orderDateStr有可能为空值，如果是空值进行拼接则会出nullpointException
            throw new MyException("日期转换失败: orderDateStr" + (orderDate == null ? "" : orderDate));
        }
//        查询看那天有没有预约信息预约信息
        OrderSetting order = orderSettingDao.findByOrderDate(date);
        if (null == order) {
//            自定义的异常,   当日不可预约,这天没有设置预约
            throw new MyException(MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        if (order.getNumber() <= order.getReservations()) {
//            设置预约数小于可预约数
            throw new MyException(MessageConstant.ORDER_FULL);
        }
//        获得手机号来判断是否是会员
        String telephone = map.get("telephone");
//        可预约,判断是否是会员,通过手机号码
        Member member = memberDao.findByTelephone(telephone);
//        不是会员.注册为会员,取出ID.将添加订单表时用
        if (null == member) {
            member = new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            member.setSex(map.get("sex"));
            member.setName(map.get("name"));
            member.setIdCard(map.get("idCard"));
            memberDao.add(member);
        }
        //是会员，取出它的ID，将添加订单表时可以用
        int memberId = member.getId();
        //判断是否已经预约过了，通过memeber_id，packageid, orderDate
        Order od = new Order();
        od.setMemberId(memberId);
        od.setPackageId(Integer.valueOf(map.get("packageId")));
        od.setOrderDate(date);
        List<Order> ods = orderDao.findByCondition(od);
        //已预约，报错
        if (null != ods && ods.size() > 0) {
            throw new MyException(MessageConstant.HAS_ORDERED);
        }
        //没预约则预约成功
        od.setOrderStatus("未到诊");
        od.setOrderType(map.get("orderType"));
        orderDao.add(od);
//        更新//更新t_ordersetting已预约人数
        orderSettingDao.editReservationsByOrderDate(order);
        return od;
    }

    @Override
    public Map<String, Object> findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        //处理日期格式
        if (map != null) {
            Date orderDate = (Date) map.get("orderDate");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = dateFormat.parse(String.valueOf(orderDate));
            map.put("orderDate", orderDate);
        }
        return map;
    }
}
