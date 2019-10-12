package com.IT.liuJia.service;

import com.IT.liuJia.entity.PageResult;
import com.IT.liuJia.entity.QueryPageBean;
import com.IT.liuJia.pojo.Package;

import java.util.List;
import java.util.Map;

public interface PackageService {

    void add(Package pkg, Integer[] checkgroupIds);

    PageResult<Package> findPage(QueryPageBean queryPageBean);

    List<Package> findAll();

    Package findById(int id);

    Package findByPkgId(int id);

}
