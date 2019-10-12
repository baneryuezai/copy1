package com.IT.liuJia.service;

import com.IT.liuJia.entity.PageResult;
import com.IT.liuJia.entity.QueryPageBean;
import com.IT.liuJia.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    /*
     * 添加
     * */
    void add(CheckItem checkItem);
    /*
     *
     * 分页查询
     * */
    PageResult<CheckItem> findPage(QueryPageBean pageBean);
    /*
     * 根据id来进行删除
     * */
    void delete(int id);
    /*
     * 根据id查询那条数据
     *
     * */
    CheckItem findById(int id);

    /*
     * 修改的那条数据
     * */
    void update(CheckItem checkItem);


    List<CheckItem> findAll();
}
