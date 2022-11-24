package com.example.picbed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ResourceLoader resourceLoader;

    private String filePath = "E:\\imgRepo";

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file) {
        // 获取上传文件名
        String filename = file.getOriginalFilename();
        String suffixName = filename.substring(filename.lastIndexOf("."));
        filename = UUID.randomUUID() + suffixName;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());

        // 定义上传文件保存路径
        String path = filePath +  "\\" + date + "\\";
        // 新建文件
        File filepath = new File(path, filename);
        // 判断路径是否存在，如果不存在就创建一个
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        try {
            // 写入文件
            file.transferTo(filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "http:localhost:8080/image/getImage/" + date + "/" + filename;
    }
    @RequestMapping("/getImage/{date}/{filename}")
    public void showPhotos(HttpServletResponse response, @PathVariable("date") String date, @PathVariable("filename") String filename){

        System.out.println(filePath + "\\" + date + "\\"+ filename);
        String suffixName = filename.substring(filename.lastIndexOf(".") + 1);
        try {
            BufferedImage image = ImageIO.read( new FileInputStream(filePath + "\\" + date + "\\"+ filename) );
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, suffixName, outputStream);
            response.setContentType("image/" + suffixName);
            ServletOutputStream outputStream1 = response.getOutputStream();
            outputStream1.write(outputStream.toByteArray());
            outputStream1.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
