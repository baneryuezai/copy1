package com.IT.liuJia.dao;

import com.IT.liuJia.pojo.CheckGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {
    /*
     * 添加检查组
     * */
    void add(CheckGroup checkGroup);

    //设置检查组与检查项之间的关系

    //因为两个类型都是Integer,所以需要别名来配置,避免出错

    void addCheckGroupCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);

    /*
     * 分页查询
     *
     * */
    Page<CheckGroup> findByCondition(String queryString);

    /*
     * 数据回显
     *
     * */
    CheckGroup findById(int id);

    List<Integer> findCheckItemIdsById(int id);

    /*
    * 查询所有检查组的下拉列表
    * */
        List<CheckGroup> findAll();
    /*
    * 更新检查组
    *
    * */
    void upData(CheckGroup checkGroup);
    /*
    * 添加新的关系
    *
    * */
    void deleteCheckItemById(Integer checkGroupId);


    int findCountByCheckGroupId(int id);

    void deleteRelation(int id);

    void deleteGroup(int id);

    void delete(int id);
}
