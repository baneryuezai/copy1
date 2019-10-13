package com.IT.liuJia.service;

import com.IT.liuJia.constant.MessageConstant;
import com.IT.liuJia.dao.CheckGroupDao;
import com.IT.liuJia.entity.PageResult;
import com.IT.liuJia.entity.QueryPageBean;
import com.IT.liuJia.exception.MyException;
import com.IT.liuJia.pojo.CheckGroup;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 包名: com.IT.liuJia.service
 * 作者: JiaLiu
 * 日期: 2019-09-24   10:49
 */
//注释中的时候事务控制
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /*
     *添加项目组
     *
     * */
    @Transactional
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
//        添加项目组
        checkGroupDao.add(checkGroup);
//        通过selectKey方式获取新加的检查组的ID
        Integer checkGroupId = checkGroup.getId();
//        将检查组的id和检查项的id添加到一起 添加到checkItemids
        if (null != checkitemIds) {
//            checkitemIds是前端传过来的,所有接收之后再遍历添加
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkitemId);
            }
        }
    }

    /*
     *
     * 分页查询
     * */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
//        先判断是否有输入数据
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
//            模糊查询  %
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
//        使用pageHelper分页    将当前页和每页的个数输入进去
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
//        紧接着的查询语句会被分页
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());
//        封装成分页结果再返回
        PageResult<CheckGroup> pageResult = new PageResult<CheckGroup>(page.getTotal(), page.getResult());
        return pageResult;
    }

    /*
     * 数据回显
     *
     * */
    @Override
    public CheckGroup findById(int id) {
        CheckGroup checkGroup = checkGroupDao.findById(id);
        return checkGroup;
    }

    @Override
    public List<Integer> findCheckItemIdsById(int id) {
        return checkGroupDao.findCheckItemIdsById(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
        /*
        * 数据的更新
        *
        * */
    @Override
    public void upData(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 1. 更新检查组
        checkGroupDao.upData(checkGroup);
        // 2. 删除已有关系
//        要想删除已有关系,先得到项目组的id,再调用dao进行删除 项目项的id,
        Integer checkGroupId = checkGroup.getId();
        checkGroupDao.deleteCheckItemById(checkGroupId);
        // 3. 建立新关系
//        遍历之前先判断是否为null?
        if (null != checkitemIds) {
            for (Integer checkitemId : checkitemIds) {
                // 添加新的关系
                checkGroupDao.addCheckGroupCheckItem(checkGroupId,checkitemId);
            }

        }
    }
    /*
    * 删除检查组
    * 先要判断有没有被套餐引用,若被引用,则不能删
    * */
    @Override
    public void delete(int id) throws MyException{

        int count=checkGroupDao.findCountByCheckGroupId(id);
        if (count>0) {
//            被引用,不可以删除
            throw new MyException(MessageConstant.DELETE_CHECKGROUP_FALL_USERD);
        }else{
//            可以删除
//            checkGroupDao.delete(id);
        checkGroupDao.deleteRelation(id);
        checkGroupDao.deleteGroup(id);
        }

    }
}
