package com.IT.liuJia.dao;
import com.IT.liuJia.pojo.Package;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface PackageDao {

    void add(Package pkg);

    void addPackageCheckGroup(@Param("pkgId") Integer pkgId,@Param("checkgroupId") Integer checkgroupId);

    Page<Package> findByCondition(String queryString);

    List<Package> findAll();

    Package findById(int id);

    Package findByPkgId(int id);

    List<Map<String,Object>> getPackageReport();
}
