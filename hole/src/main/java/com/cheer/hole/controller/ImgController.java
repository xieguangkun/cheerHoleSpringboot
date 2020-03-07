package com.cheer.hole.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;

@RestController
public class ImgController {

    @Value("${img.location}")
    private String location;

    @RequestMapping("/upload")
    public String upload(MultipartFile file) throws Exception {
        // 打印文件的名称
        System.out.println("FileName:" + file.getOriginalFilename());

        // 确定上传文件的位置
        // 本地路径,测试确实能通过
        // String path = "E:/temp/temp";
        // Linux系统
        // 获取上传的位置（存放图片的文件夹）,如果不存在，创建文件夹
        File fileParent = new File(location);
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        File newFile = new File(location + "/img/", file.getOriginalFilename());
        // 如果不存在，创建一个副本
        if (!newFile.exists()) {
            newFile.createNewFile();
        }
        // 将io上传到副本中
        file.transferTo(newFile);
        return "上传成功"+file.getOriginalFilename();
    }
}
