package com.IT.liuJia.dao;
import com.IT.liuJia.pojo.CheckItem;
import com.github.pagehelper.Page;
import java.util.List;
public interface CheckItemDao {
    /*
     * 添加
     * */
    void add(CheckItem checkItem);

    /*
     * 分页查询
     * */
    Page<CheckItem> findAllByCondition(String queryString);

    /*
     * 根据id删除
     * */
    void delete(int id);

    /*
     * 查看是否被引用
     * */
    int findCountByCheckItemId(int id);

    /*
     *根据id显示数据
     *
     * */
    CheckItem findById(int id);

    /*
     *更改数据
     *
     * */
    void update(CheckItem checkItem);

    /*
    *
    * 查询所有检查项
    * */
    List<CheckItem> findAll();
}
