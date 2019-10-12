package com.IT.liuJia.service;
import com.IT.liuJia.dao.MemberDao;
import com.IT.liuJia.dao.OrderDao;
import com.IT.liuJia.dao.PackageDao;
import com.IT.liuJia.util.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * 包名: com.IT.liuJia.service
 * 作者: JiaLiu
 * 日期: 2019-10-10   11:28
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {
    @Autowired
    private PackageDao packageDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    /*套餐占比*/
    @Override
    public List<Map<String, Object>> getPackageReport() {
        return packageDao.getPackageReport();
    }
    /*报表分析*/
    @Override
    public Map<String, Object> getBusinessReportData() {
        //星期一的日期
        String monday = DateUtils.date2String(DateUtils.getThisWeekMonday(), DateUtils.YMD);
//        星期日的日期
        String sunday = DateUtils.date2String(DateUtils.getSundayOfThisWeek(), DateUtils.YMD);
//        本月第一天   1号
        String firstDayOfThisMonth = DateUtils.date2String(DateUtils.getFirstDayOfThisMonth(), DateUtils.YMD);
//        月末   31号
        String lastDayOfThisMonth = DateUtils.date2String(DateUtils.getLastDayOfThisMonth(), DateUtils.YMD);
        /*日期  当前系统日期*/
        Date date=new Date();
        String reportDate = DateUtils.date2String(date, DateUtils.YMD);
        /*会员数据统计*/
        //今日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(reportDate);
        // 总会员数
        Integer totalMember = memberDao.findMemberTotalCount();
        // 本周新增会员数  大于星期一小于今日  所以方法就可以是大于星期一就行,因为数据库
        // 中没有大于今日的会员数据,参数就是星期一,语句就是星期一之后的数据
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);
        /*预约到诊数据统计*/
        // 今日预约数
        Integer todayOrderNumber=orderDao.findOrderCountByDate(reportDate);
        // 今日到诊数
        Integer todayVisitsNumber=orderDao.findVisitsCountByDate(reportDate);
        // 本周预约数   有时间范围的 了  预约可以发生在未来
        Integer thisWeekOrderNumber=orderDao.findOrderCountBetweenDate(monday,sunday);
        // 本周到诊数
        Integer thisWeekVisitsNumber=orderDao.findVisitsCountAfterDate(monday);
        // 本月预约数  有时间范围的 了  预约可以发生在未来
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth,lastDayOfThisMonth);
        // 本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfThisMonth);
        /*热门套餐     取前四个 */
        List<Map<String,Object>> hotPackage=orderDao.findHotPackage();
        Map<String,Object>  result=new HashMap<String,Object>();
        result.put("reportDate",reportDate);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotPackage",hotPackage);
        return result;
    }
}
