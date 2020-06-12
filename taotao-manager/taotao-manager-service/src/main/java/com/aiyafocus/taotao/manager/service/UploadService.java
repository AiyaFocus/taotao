package com.aiyafocus.taotao.manager.service;

import com.aiyafocus.taotao.common.utils.PictureResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传业务逻辑层接口
 *
 * @author AiyaFocus
 * createDate 2020/6/6 16:56
 */
public interface UploadService {

    /**
     * 上传多张图片的方法
     * @param uploadFile 接收前端发送的图片信息
     * @return 返回指定的PictureResult对象，该对象中包含页面需要的结果数据
     */
    PictureResult uploadImages(MultipartFile uploadFile);

}
