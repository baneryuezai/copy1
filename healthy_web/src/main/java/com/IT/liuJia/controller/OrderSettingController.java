package com.IT.liuJia.controller;

import com.IT.liuJia.constant.MessageConstant;
import com.IT.liuJia.entity.Result;
import com.IT.liuJia.pojo.OrderSetting;

import com.IT.liuJia.service.OrderSettingService;
import com.IT.liuJia.util.POIUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 包名: PACKAGE_NAME
 * 作者: JiaLiu
 * 日期: 2019-10-02   22:37
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;
    @PostMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) throws Exception {
//        解析excel  将上传的表格解析出来
        try {
            List<String[]> strings = strings = POIUtils.readExcel(excelFile);
            // 将把它转成OrderSetting实体List,遍历出来的数据都被list添加进去
            List<OrderSetting> list = new ArrayList<>();
//        日期转化
            SimpleDateFormat dateFormat = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            OrderSetting os = null;
            for (String[] arr : strings) {
                os = new OrderSetting(dateFormat.parse(arr[0]), Integer.valueOf(arr[1]));
                list.add(os);
            }
            // 再调用业务服务
//       因为读取的excelFile有可能是空的,所以需要判断一下list是否为空,严格上来说应该判断strings
//        但后面List做了添加动作,所以判断List也可以
            if (list.size() > 0) {
                orderSettingService.doImport(list);
                return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }

    @PostMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month) {
//        调用业务层来查询
        List<OrderSetting> list = orderSettingService.getOrderSettingByMonth(month);
//        将结果封装成Map的集合 因为一对就是一个Map多个Map就是一个集合
//          date: 1, number: 120, reservations: 1 },
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
//        每日的数据
//        遍历结合之前先判断是否存在集合
        if (null != list && list.size() > 0) {
//        数据格式转化,将查询到的数据库中的data转成day天数类型
            SimpleDateFormat sdf = new SimpleDateFormat("d");
            for (OrderSetting os : list) {
                Map<String, Object> dayData = new HashMap<>();
                dayData.put("number", os.getNumber());
                dayData.put("reservations", os.getReservations());
//                获取当天的日期,转成整型
                dayData.put("date", Integer.valueOf(sdf.format(os.getOrderDate())));
//                将这个Map放入返回的结果集Resultlist里面去,返回Resultist即可
                resultList.add(dayData);
//                System.out.println(dayData);
            }
        }
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, resultList);
    }

    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        orderSettingService.editNumberByDate(orderSetting);
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}
