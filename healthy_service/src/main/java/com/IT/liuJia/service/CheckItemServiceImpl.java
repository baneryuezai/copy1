package com.IT.liuJia.service;
import com.IT.liuJia.constant.MessageConstant;
import com.IT.liuJia.dao.CheckItemDao;
import com.IT.liuJia.entity.PageResult;
import com.IT.liuJia.entity.QueryPageBean;
import com.IT.liuJia.exception.MyException;
import com.IT.liuJia.pojo.CheckItem;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * 包名: com.IT.liuJia.service
 * 作者: JiaLiu
 * 日期: 2019-09-21   23:46
 */
@Service(interfaceClass = CheckItemService.class)

public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    /*
     * 添加
     * */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /*
     *
     * 分页查询
     * */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean pageBean) {
//        判断是否有输入东西,有值就模糊查询,没有值就不模糊查询
        if (!StringUtils.isEmpty(pageBean.getQueryString())) {
//            有查询条件 输入的两边加上模糊查询  的%%
            pageBean.setQueryString("%" + pageBean.getQueryString() + "%");
        }
//        使用分页插件
        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
//        查询语句会被分页
        Page<CheckItem> page = checkItemDao.findAllByCondition(pageBean.getQueryString());
        PageResult<CheckItem> pageResult = new PageResult<CheckItem>(page.getTotal(), page.getResult());
        return pageResult;
    }

    /*
     *根据id删除
     *
     * */
    @Override
    public void delete(int id) throws MyException{
//        先检查一下,看检查项是否被检查组引用了
        int count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0) {
//            有引用,不许删除
            throw new MyException(MessageConstant.DELETE_CHECKITEM_FAIL_USED);
        } else {
//            可以删除
            checkItemDao.delete(id);
        }


    }
    /*
     *根据id查询
     *
     * */
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }
    /*
     *更改显示的数据
     *
     * */
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    /*
    * 查询所有检查项
    *
    * */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }


}
