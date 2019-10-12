package com.IT.liuJia.controller;

import com.IT.liuJia.constant.MessageConstant;
import com.IT.liuJia.constant.RedisMessageConstant;
import com.IT.liuJia.entity.Result;
import com.IT.liuJia.pojo.Order;
import com.IT.liuJia.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 包名: com.IT.liuJia.controller
 * 作者: JiaLiu
 * 日期: 2019-10-05   09:24
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String, String> map) throws Exception {
        Jedis jedis = jedisPool.getResource();
//        获得手机号
        String telephone = map.get("telephone");
//        拼接成验证码的key........获得验证码存入redis的key
        String key = "Order_" + RedisMessageConstant.SENDTYPE_ORDER + telephone;
//        获取redis里的value,也就是验证码
        String codeInRedis = jedis.get(key);
        System.out.println(codeInRedis);
        if (StringUtils.isEmpty(codeInRedis)) {
            // 空值, 过时或没有点击获取验证码
            return new Result(false, "请点击获取验证码");
        }
        if (!codeInRedis.equals(map.get("validateCode"))) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

//          清除redis中的验证码
        jedis.del(key);
        // 调用业务服务
        map.put("orderType", "微信预约");
        Order order = orderService.submitOrder(map);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    @PostMapping("/findById")
    public Result findById(Integer id) throws Exception {
        Map<String, Object> map = orderService.findById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
    }
}
