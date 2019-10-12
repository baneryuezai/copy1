package com.IT.liuJia.service;
import com.IT.liuJia.dao.MemberDao;
import com.IT.liuJia.pojo.Member;
import com.IT.liuJia.util.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 包名: com.IT.liuJia.service
 * 作者: JiaLiu
 * 日期: 2019-10-07   07:04
 */
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    /*
    * 手机号查询会员
    * */
    @Override
    public Member findByTelephone(String telephone) {
        Member member = memberDao.findByTelephone(telephone);
        return member;
    }
    /*
    * 新增会员
    * */
    @Override
    public void add(Member member) {
//       还要判断一下是否密码不为空
        if (member.getPassword()!=null) {
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }else {
            memberDao.add(member);
        }
    }
    /*
    * 会员数量统计
    * */
    @Override
    public Map<String, List<Object>> getMemberReport() {
        // {flag,message,data{
//            months:[],
//            memberCount: []
// }}
        //1. 获取上一年份的数据
        //2. 循环12个月，每个月要查询一次
        //3. 再查询得到的数据封装到months list 月份,
//           memberCount list 到这个月份为止会员总数量

        //1. 获取上一年份的数据
        // 日历对象，java中来操作日期时间，当前系统时间
        Calendar car = Calendar.getInstance();
        // 回到12个月以前
        car.add(Calendar.MONTH,-12);
        //2. 循环12个月，每个月要查询一次
//        先建两个集合定义变量来接受两组集合
//        返回的月份数量集合
        List<Object> months =new ArrayList<>();
//        返回的会员数量集合
        List<Object> memberCount=new ArrayList<>();
//        由于数据库日期格式和要显示的日期数据格式不一致,要定义日期类型
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (int i = 1; i <=12 ; i++) {
            // 计算当前月的值(这个当前月是指在for循环里面要遍历的月份,由于之前已经减一
            // 了,也就是从系统时间月份-12等于要开始的月份,)
            car.add(Calendar.MONTH, 1);
            // 月份的字符串 2019-01
            String monthStr = sdf.format(car.getTime());
            months.add(monthStr);
            // 查询会员数量
            Integer monthCount = memberDao.findMemberCountBeforeDate(monthStr + "-31");
            memberCount.add(monthCount);
        }
            //3. 再查询得到的数据封装到months list 月份, memberCount list 到这个月份为止会员总数量
//            见一个map
            Map<String,List<Object>> map =new HashMap<>();
            map.put("months",months);
            map.put("memberCount",memberCount);
            return map;

    }
}
