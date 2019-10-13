package com.IT.liuJia.controller;
import com.IT.liuJia.constant.MessageConstant;
import com.IT.liuJia.entity.PageResult;
import com.IT.liuJia.entity.QueryPageBean;
import com.IT.liuJia.entity.Result;
import com.IT.liuJia.pojo.CheckItem;
import com.IT.liuJia.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 包名: com.IT.liuJia.controller
 * 作者: JiaLiu
 * 日期: 2019-09-20   23:08
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;
    /*
     * 添加
     *
     * */
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem) {
//        调用业务服务
        checkItemService.add(checkItem);
//        返回结果
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /*
     *
     * 查找
     * */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean pageBean) {
        PageResult<CheckItem> pageResult = checkItemService.findPage(pageBean);
        Result result = new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, pageResult);
        return result;
    }

    /*
     *
     * 删除
     * */
    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result delete(@RequestParam int id) {
        try {
            checkItemService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /*
     * 更新
     *
     * */
    @GetMapping("/findById")
    public Result findById(int id) {
//        调用业务查询修改的数据
        CheckItem checkItem = checkItemService.findById(id);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
    }
    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem) {
//        调用业务服务来更新
        checkItemService.update(checkItem);
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
    /*
    * 查询所有
    *
    * */
    @PostMapping("/findAll")
    public Result findAll(){
        List<CheckItem> list=checkItemService.findAll();
        Result result = new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        return result;
    }

}
