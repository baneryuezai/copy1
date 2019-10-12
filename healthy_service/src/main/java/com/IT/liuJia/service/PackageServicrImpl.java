package com.IT.liuJia.service;

import com.IT.liuJia.dao.PackageDao;
import com.IT.liuJia.entity.PageResult;
import com.IT.liuJia.entity.QueryPageBean;
import com.IT.liuJia.pojo.Package;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 包名: com.IT.liuJia.service
 * 作者: JiaLiu
 * 日期: 2019-09-26   09:02
 */
@Service(interfaceClass = PackageService.class)
public class PackageServicrImpl implements PackageService {
    @Autowired
    private PackageDao packageDao;
    @Override
    public void add(Package pkg, Integer[] checkgroupIds) {
//        添加套餐
        packageDao.add(pkg);
//        通过selectKey方式获取新加的套餐的Id
        Integer pkgId = pkg.getId();
//        将套餐组的id和检查组的id添加到一起,添加到checkItemids
        if (null!=checkgroupIds) {
//            如果不为空,因为checkItemids是前端传过来的是个数组,所以接手之后再遍历添加
            for (Integer checkgroupId : checkgroupIds) {
//                添加套餐与检查组的关系
                packageDao.addPackageCheckGroup(pkgId,checkgroupId);
            }
        }
    }

    @Override
    public PageResult<Package> findPage(QueryPageBean queryPageBean) {
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
//            不为空 则进行模糊查询
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
//        使用pageHelper分页        将当前页数和每页条数输入进去 就是已知数据
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
//        紧接着查询语句就会被分页
        Page<Package> page=packageDao.findByCondition(queryPageBean.getQueryString());
//        封装成PageResult结果再返回
        PageResult<Package> pageResult = new PageResult<Package>(page.getTotal(),page.getResult());
        return pageResult;
    }

    @Override
    public List<Package> findAll() {
        return packageDao.findAll();
    }

    @Override
    public Package findById(int id) {
        return packageDao.findById(id);
    }

    @Override
    public Package findByPkgId(int id) {
        return packageDao.findByPkgId(id);
    }




}
