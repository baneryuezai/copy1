package com.IT.liuJia.controller;

import com.IT.liuJia.constant.MessageConstant;
import com.IT.liuJia.entity.PageResult;
import com.IT.liuJia.entity.QueryPageBean;
import com.IT.liuJia.entity.Result;
import com.IT.liuJia.pojo.CheckGroup;
import com.IT.liuJia.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 包名: com.IT.liuJia.controller
 * 作者: JiaLiu
 * 日期: 2019-09-24   10:26
 */
@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    @PostMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupService.add(checkGroup, checkitemIds);
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /*
     * 分页查询
     *
     * */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<CheckGroup> pageResult = checkGroupService.findPage(queryPageBean);
        Result result1 = new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, pageResult);
        return result1;
    }

    /*
     * 编辑
     *
     * 1*/
    @GetMapping("/findById")
    public Result findById(int id) {
        CheckGroup checkGroup = checkGroupService.findById(id);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
    }

    @GetMapping("/findCheckItemIdsById")
    public Result findCheckItemIdsById(int id) {
        List<Integer> list = checkGroupService.findCheckItemIdsById(id);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
    }

    /*
     * 查询所有
     *
     * */
    @PostMapping("/findAll")
    public Result findAll() {
        List<CheckGroup> checkGroups = checkGroupService.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroups);
    }

    /*
     * 提交编辑
     *
     * */
    @PostMapping("/upDate")
    public Result upData(@RequestBody CheckGroup checkGroup, @RequestParam Integer[] checkitemIds) {
        checkGroupService.upData(checkGroup, checkitemIds);
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @PostMapping("/delete")
    public Result delete(int id) {
        checkGroupService.delete(id);
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);

    }
}
