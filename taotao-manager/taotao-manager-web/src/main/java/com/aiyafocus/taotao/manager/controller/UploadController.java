package com.aiyafocus.taotao.manager.controller;

import com.aiyafocus.taotao.common.utils.PictureResult;
import com.aiyafocus.taotao.manager.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 控制文件上传的控制器
 *
 * @author AiyaFocus
 * createDate 2020/6/6 16:50
 */
@RestController
public class UploadController {

    @Resource
    UploadService uploadService;

    /**
     * 上传多张图片的方法
     * @param uploadFile 接收前端发送的图片信息
     * @return 返回指定的PictureResult对象，该对象中包含页面需要的结果数据
     */
    @PostMapping("/pic/upload")
    public PictureResult uploadImages(MultipartFile uploadFile) {
        return uploadService.uploadImages(uploadFile);
    }

}
