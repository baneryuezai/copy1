package com.IT.liuJia.controller;
import com.IT.liuJia.constant.MessageConstant;
import com.IT.liuJia.entity.Result;
import com.IT.liuJia.pojo.Package;
import com.IT.liuJia.service.PackageService;
import com.IT.liuJia.util.QiNiuUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
/**
 * 包名: com.IT.liuJia.controller
 * 作者: JiaLiu
 * 日期: 2019-09-27   21:12
 */
@RestController
@RequestMapping("/package")
public class Package2Controller {
    @Reference
    private PackageService packageService;
    @RequestMapping("/getPackage")
    public Result getPackage(){
//    获得套餐列表
        List<Package> list=packageService.findAll();
//    遍历之前先判断集合是否为空
        if (null!=list) {
//      拼接套餐的图片完成路径
            // 取出list中的每个元素赋值给pkg变量
            // pkg相当于list中的元素
            // 只要对pkg进行修改，list中的元素也会跟着修改
            list.forEach(pkg -> {
                pkg.setImg("http://" + QiNiuUtil.DOMAIN + "/" + pkg.getImg());
            });
        }
        return new Result(true, MessageConstant.QUERY_PACKAGE_SUCCESS, list);
    }
    @RequestMapping("/findById")
    public Result findById(int id){
//        调用业务服务查询套餐详情,
        Package pkg=packageService.findById(id);
//        拼接图片完整路径
        pkg.setImg("http://" + QiNiuUtil.DOMAIN + "/" + pkg.getImg());
        return new Result(true,MessageConstant.QUERY_PACKAGE_SUCCESS,pkg);
    }
    @PostMapping("/findByPkgId")
    public Result findByPkgId(int id){
        Package pkg=packageService.findByPkgId(id);
        pkg.setImg("http://"+QiNiuUtil.DOMAIN +"/"+pkg.getImg());
        return new Result(true,MessageConstant.QUERY_PACKAGE_SUCCESS,pkg);
    }
}
