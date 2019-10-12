package com.IT.liuJia.controller;

import com.IT.liuJia.constant.MessageConstant;
import com.IT.liuJia.entity.Result;
import com.IT.liuJia.service.MemberService;
import com.IT.liuJia.service.PackageService;
import com.IT.liuJia.service.ReportService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包名: com.IT.liuJia.controller
 * 作者: JiaLiu
 * 日期: 2019-10-09   23:16
 */

/*
 * 统计报表
 *
 * */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private PackageService packageService;
    @Reference
    private ReportService reportService;

    /*
     * 会员数量统计
     * */
    @PostMapping("/getMemberReport")
    public Result getMemberReport() {
        // {flag,message,data{
//              months:[],
//              memberCount: []
// }}
        // 调用业务服务查询过去一年的会员数量 从当前起前12个月
        Map<String, List<Object>> list = memberService.getMemberReport();
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, list);
    }
        /*套餐
        占比统计
        */

    @PostMapping("/getPackageReport")
    public Result getPackageReport() {
//          {true,message,data{
//              packageNames:[]
//              packageCount:[
//                          {name:value,name:value}
//                           ]
//          }}
//        List<Package> packageNames = packageService.findAllPackageName();
//        查询到的结果会自动映射到这个类型里面去,sql语句要显示的,会对应map类型,eg:name:value...并且是集合类型
//        所以会有多个这样的name:value,name:value,name:value,...直接添加到返回结果的result里面即可
        List<Map<String, Object>> packageCount = reportService.getPackageReport();
        Map<String, Object> result = new HashMap<String, Object>(2);
        List<Object> packageNames = new ArrayList<>();
        if (null != packageCount) {
            for (Map<String, Object> map : packageCount) {
                packageNames.add(map.get("name"));
            }
//            下面这个resultMap添加集合类型不需要放在for循环里面,因为是集合,能自动扩长度,也没有覆盖
            result.put("packageNames", packageNames);
            result.put("packageCount", packageCount);
        }
        return new Result(true, MessageConstant.GET_PACKAGE_COUNT_REPORT_SUCCESS, result);
    }

    /*
     *
     * 运营数据统计
     * */
    @PostMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        Map<String, Object> map = reportService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
    }

    @GetMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        // 调用业务服务查询运营数据
        Map<String, Object> map = reportService.getBusinessReportData();
//        写到Execel中
//        1.获取Execel模板  getRealPath :webapp
          String template=request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
//        2.创建工作薄,根据模板
        try {
            Workbook wk=new XSSFWorkbook(template);
//        3.获取工作表
            Sheet sht = wk.getSheetAt(0);
//            4.获取行
//            sht.getRow(2);
////            5.获取单元格
//            sht.getRow(2).getCell(5);
//            4.获取行,单元格,设置数据
            sht.getRow(2).getCell(5).setCellValue((String) map.get("reportDate"));
//            5.会员数量
            sht.getRow(4).getCell(5).setCellValue((Integer) map.get("todayNewMember"));
            sht.getRow(4).getCell(7).setCellValue(((Integer) map.get("totalMember")));
            sht.getRow(5).getCell(5).setCellValue(((Integer) map.get("thisWeekNewMember")));
            sht.getRow(5).getCell(7).setCellValue(((Integer) map.get("thisMonthNewMember")));
//            6.// 预约到诊数据统计数量
            sht.getRow(7).getCell(5).setCellValue((Integer) map.get("todayOrderNumber"));
            sht.getRow(7).getCell(7).setCellValue((Integer) map.get("todayVisitsNumber"));
            sht.getRow(8).getCell(5).setCellValue(((Integer) map.get("thisWeekOrderNumber")));
            sht.getRow(8).getCell(7).setCellValue(((Integer) map.get("thisWeekVisitsNumber")));
            sht.getRow(9).getCell(5).setCellValue(((Integer) map.get("thisMonthOrderNumber")));
            sht.getRow(9).getCell(7).setCellValue(((Integer) map.get("thisMonthVisitsNumber")));
//          7.热门套餐
            int rowCnt=12;
            List<Map<String,Object>> hotPackage= (List<Map<String, Object>>) map.get("hotPackage");
            if (null!=hotPackage) {
                for (Map<String, Object> hpkmap : hotPackage) {
                    sht.getRow(rowCnt).getCell(4).setCellValue((String) hpkmap.get("name"));
                    sht.getRow(rowCnt).getCell(5).setCellValue((Long) hpkmap.get("count"));
                    sht.getRow(rowCnt).getCell(6).setCellValue(((BigDecimal) hpkmap.get("proportion")).toString());
                    sht.getRow(rowCnt).getCell(7).setCellValue((String) hpkmap.get("remark"));
                    rowCnt++;
                }
            }
                // 5. 调用Response的输出流实现 下载
            // 6. 下载之前要告诉浏览器接收的是文件 Content-Type
            // 这个内容的是excel文件
            response.setContentType("application/vnd.ms-excel");
            // 下载文件
            String filename = "运营数据.xlsx";
//            解决乱码问题
            filename = new String(filename.getBytes(),"ISO-8859-1");
            ServletOutputStream out = response.getOutputStream();
            response.setHeader("Content-Disposition","attachment;filename=" + filename);
            wk.write(out);
            out.flush();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
    }
}
