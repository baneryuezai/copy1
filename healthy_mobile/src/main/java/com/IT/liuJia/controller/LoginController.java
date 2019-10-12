package com.IT.liuJia.controller;
import com.IT.liuJia.constant.MessageConstant;
import com.IT.liuJia.constant.RedisMessageConstant;
import com.IT.liuJia.entity.Result;
import com.IT.liuJia.pojo.Member;
import com.IT.liuJia.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
/**
 * 包名: com.IT.liuJia.controller
 * 作者: JiaLiu
 * 日期: 2019-10-07   06:22
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    @PostMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map<String, String> map) {
//        拿到连接
        Jedis jedis = jedisPool.getResource();
//        获得手机号
        String telephone = map.get("telephone");
//         拼接key
        String key = "login_" + RedisMessageConstant.SENDTYPE_LOGIN + telephone;
        // redis中的验证码
        String codeInRedis = jedis.get(key);
//        判断验证码是否为空
        if (StringUtils.isEmpty(codeInRedis)) {
//              空值, 过时或没有点击获取验证码
            return new Result(false, "请点击获取验证码");
        }
        // 编码规范：先把不符合条件的先处理，后再处理符合条件。
        // 有值
        if (!codeInRedis.equals(map.get("validateCode"))) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 清除redis中的验证码
        jedis.del(key);

        //判断当前用户是否为会员  怎么判断呢? 将telephone传进去看能不能找到
        Member member = memberService.findByTelephone(telephone);
        if (null == member) {
//                  说明不是会员,自动完成注册
//                注意这里面的对象用查找之后的那个member对象
            member = new Member();
            member.setPhoneNumber("telephone");
            member.setRegTime(new Date());
            memberService.add(member);
        }
//          用户跟踪，写手机号码到Cookie，当用户再次访问我们的网站时就会带上
//          这个cookie，这样我们就知道是哪个用户了，方便日后做统计分析
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setMaxAge(60 * 60 * 24 * 30);
        // 设置为网站的根路径，当访问到这个网站就会带上cookie
        cookie.setPath("/");
        // 给客户端写cookie
        response.addCookie(cookie);
//            登录成功
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}


