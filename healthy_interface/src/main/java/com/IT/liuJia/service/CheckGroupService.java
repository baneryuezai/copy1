package com.IT.liuJia.service;

import com.IT.liuJia.entity.PageResult;
import com.IT.liuJia.entity.QueryPageBean;
import com.IT.liuJia.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
/*
* 添加检查组
*
* */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);
/*
* 分页查询
*
* */
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    CheckGroup findById(int id);

    List<Integer> findCheckItemIdsById(int id);

    List<CheckGroup> findAll();

    void upData(CheckGroup checkGroup, Integer[] checkitemIds);

    void delete(int id);
}
