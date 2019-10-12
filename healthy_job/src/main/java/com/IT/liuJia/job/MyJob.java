package com.IT.liuJia.job;

import com.IT.liuJia.constant.RedisConstant;
import com.IT.liuJia.util.QiNiuUtil;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 包名: com.IT.liuJia.job
 * 作者: JiaLiu
 * 日期: 2019-09-27   10:15
 */

public class MyJob {
    @Autowired
    private JedisPool jedisPool;
    public void doJob(){
        Jedis jedis = jedisPool.getResource();
//       1.取出Redis中所有图片的集合,减去,保存到数据库中的图片集合
        Set<String> need2Delete = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
//       2.调用QiNiuUtil删除服务器上的图片     因为removeFiles接受的是String... Files 是一个数组,所以下面要转换
        QiNiuUtil.removeFiles(need2Delete.toArray(new String[]{}));
//       3.成功要删除Redis中的所有缓存,所有的,包括保存到数据库中的
//        删掉key值,value值就能删掉了
        jedis.del(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
    }
    public static void main(String[] args){
        ApplicationContext ac=new ClassPathXmlApplicationContext("classpath:spring-job.xml");
    }
}
