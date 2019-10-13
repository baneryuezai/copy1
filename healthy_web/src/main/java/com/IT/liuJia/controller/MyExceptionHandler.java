package com.IT.liuJia.controller;
import com.IT.liuJia.entity.Result;
import com.IT.liuJia.exception.MyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
/**
 * 包名: com.IT.liuJia.controller
 * 作者: JiaLiu
 * 日期: 2019-10-09   11:51
 */
/*
* 拦截Controller抛出异常
*
* */
@RestControllerAdvice
public class MyExceptionHandler {
    //声明要捕获的异常
    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException my){
        my.printStackTrace();//stack:栈  trace:追踪
//        包装一下返回的结果
        return new Result(false,my.getLocalizedMessage());
    }
        //    声明要捕获的异常类型
    @ExceptionHandler(AccessDeniedException.class)
    public Result noAccessHandler(AccessDeniedException accessDeniedException){
//        这个拦截的是Controller里面的异常,所以controller就不抛异常了,被捕获了
        return new Result(false,"权限不足");
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result noInvalidHandler(MethodArgumentNotValidException e){
//        校验的结果
        BindingResult bindingResult = e.getBindingResult();
//        校验没通过的属性
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuffer sb = new StringBuffer();
        if (null!=fieldErrors) {
            for (FieldError error : fieldErrors) {
                sb.append(error.getField()+":"+error.getDefaultMessage());
            }
        }
        return new Result(false, sb.toString());
    }


}
