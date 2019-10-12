package com.IT.liuJia.controller;



import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 包名: com.IT.liuJia.controller
 * 作者: JiaLiu
 * 日期: 2019-10-09   07:45
 */
@Controller
@RequestMapping("/hello")
public class SecurityController {
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('add')")//表示用户必须拥有add权限才能调用当前方法
    public String add() {
        System.out.println("add...");
        return null;
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//表示用户必须拥有ROLE_ADMIN角色才能调用当前方法
    public String delete() {
        System.out.println("delete...");
        return null;
    }
}

