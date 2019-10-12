package com.IT.liuJia.controller;
import com.IT.liuJia.constant.MessageConstant;
import com.IT.liuJia.constant.RedisConstant;
import com.IT.liuJia.entity.PageResult;
import com.IT.liuJia.entity.QueryPageBean;
import com.IT.liuJia.entity.Result;
import com.IT.liuJia.pojo.Package;
import com.IT.liuJia.service.PackageService;
import com.IT.liuJia.util.QiNiuUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/**
 * 包名: com.IT.liuJia.controller
 * 作者: JiaLiu
 * 日期: 2019-09-25   23:48
 */
@RestController
@RequestMapping("/package")
public class PackageController {
    @Reference
    private PackageService packageService;
    @Autowired
    private JedisPool jedisPool;

    /*图片的上传
     *
     * */
    @PostMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
//        图片原名称
        String originalFilename = imgFile.getOriginalFilename();
//        1.产生唯一的key
        UUID uuid = UUID.randomUUID();
//        文件的扩展名
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = uuid.toString() + extension;
//        2.调用QiNiuUtil上传图片
        try {
            QiNiuUtil.uploadViaByte(imgFile.getBytes(), newFileName);
            // 保存到redis，所有all
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, newFileName);
//       3.封装返回的结果, picName domain
            Map<String, String> resultMap = new HashMap<String, String>();
//            因为要将返回的结果封装到Result 里面的data中,所以用map来传
            resultMap.put("picName", newFileName);
            resultMap.put("domain", QiNiuUtil.DOMAIN);
//            4.返回结果
            return new Result(true, MessageConstant.UPLOAD_SUCCESS, resultMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 失败的
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Package pkg, Integer[] checkgroupIds) {
        packageService.add(pkg, checkgroupIds);
//        保存到数据库db中的图片
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pkg.getImg());
        return new Result(true, MessageConstant.ADD_PACKAGE_SUCCESS);
    }
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Package> pageResult=packageService.findPage(queryPageBean);
        Result result = new Result(true,MessageConstant.QUERY_PACKAGE_SUCCESS,pageResult);
        return result;
    }
}
