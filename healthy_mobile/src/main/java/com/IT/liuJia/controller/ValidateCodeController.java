package com.IT.liuJia.controller;
import com.IT.liuJia.constant.MessageConstant;
import com.IT.liuJia.constant.RedisMessageConstant;
import com.IT.liuJia.entity.Result;
import com.IT.liuJia.util.SMSUtils;
import com.IT.liuJia.util.ValidateCodeUtils;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
/**
 * 包名: com.IT.liuJia.controller
 * 作者: JiaLiu
 * 日期: 2019-09-29   14:47
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
    @PostMapping("/send4Order")
    public Result ValidateCode(@RequestParam String telephone) {
//        获取redis操作对象,用完了要关闭
        Jedis jedis = jedisPool.getResource();
//        定义一个key值,用于存放查找key,此时还没有跟redis发生关系,当后面存进redis时,才有关系
        String key = "Order_" + RedisMessageConstant.SENDTYPE_ORDER + telephone;
//        判断是否发送过了,就是判断是否存在key的value值
        if (null != jedis.get(key)) {
//            存在 说明已经发送了
            return new Result(true, MessageConstant.SENT_VALIDATECODE);
        } else {
//            不存在  没有发送过   就发送
//            先生成个验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
//            发送验证码
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
//                发送成功
                // 存入redis
                // 1: key
                // 2: 有效时间，单位秒。多长时间后这个key值就会自动删除
                // 3: key对应的值
//              第一个参数 key  第二个参数  单位秒  过了这个时间就无效第三个参数  value值
                jedis.setex(key, 10 * 60, code + "");
//                存入redis刘佳
//                jedis.set(key,code+"");
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
    @PostMapping("/send4Login")
    public Result send4Login(@RequestParam String telephone) {
//        获取redis操作对象,用完了要关闭
        Jedis jedis = jedisPool.getResource();
//        定义一个key值,用于存放查找key,此时还没有跟redis发生关系,当后面存进redis时,才有关系
        String key = "login_" + RedisMessageConstant.SENDTYPE_LOGIN + telephone;
//        判断是否发送过了,就是判断是否存在key的value值
        if (null != jedis.get(key)) {
//            存在 说明已经发送了
            return new Result(true, MessageConstant.SENT_VALIDATECODE);
        } else {
//            不存在  没有发送过   就发送
//            先生成个验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
//            发送验证码
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
//                发送成功
                // 存入redis
                // 1: key
                // 2: 有效时间，单位秒。多长时间后这个key值就会自动删除
                // 3: key对应的值
//              第一个参数 key  第二个参数  单位秒  过了这个时间就无效第三个参数  value值
                jedis.setex(key, 10 * 60, code + "");
//                存入redis刘佳
//                jedis.set(key,code+"");
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
}
