package com.aiyafocus.taotao.manager.service.impl;

import com.aiyafocus.taotao.common.utils.IDUtils;
import com.aiyafocus.taotao.common.utils.PictureResult;
import com.aiyafocus.taotao.common.utils.SFtpUtils;
import com.aiyafocus.taotao.manager.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传业务逻辑层接口的实现类
 *
 * @author AiyaFocus
 * createDate 2020/6/6 17:01
 */
@Service("uploadService")
public class UploadServiceImpl implements UploadService {

    // 将database.properties属性配置文件中的关键参数值注入属性中使用
    @Value("${SFTP_HOSTNAME}")
    private String hostname;
    @Value("${SFTP_PORT}")
    private Integer port;
    @Value("${SFTP_USERNAME}")
    private String username;
    @Value("${SFTP_PASSWORD}")
    private String password;
    @Value("${SFTP_TIMEOUT}")
    private Integer timeout;
    @Value("${SFTP_BASEPATH}")
    private String basePath;
    @Value("${SFTP_IMAGEURL}")
    private String imageURL;

    /**
     * 上传多张图片的方法
     *
     * @param uploadFile 接收前端发送的图片信息
     * @return 返回指定的PictureResult对象，该对象中包含页面需要的结果数据
     */
    @Override
    public PictureResult uploadImages(MultipartFile uploadFile) {
        // 1.上传图片
        // 获得上传的文件的名称
        String upFileName = uploadFile.getOriginalFilename();
        // 创建保存到图片服务器的图片的名称
        String fileName = IDUtils.genImageName() +
                upFileName.substring(upFileName.lastIndexOf("."));
        // 创建上传文件保存的子路径
        String childPath = IDUtils.createDateDir();
        try {
            // 调用上传图片的方法
            SFtpUtils.uploadFile(hostname, port, username, password,
                    timeout, uploadFile.getInputStream(),
                    basePath+childPath, fileName);
            // 2.返回上传图片的结果，如果正常，则调用PictureResult的ok方法，并将图片的URL地址传入ok方法，再返回PictureResult对象
            // 注意：图片的URL由3部分组成
            // a.图片服务器的访问地址；
            // b.上传文件保存的子路径；
            // c.保存到图片服务器的图片的名称；
            return PictureResult.ok(imageURL+childPath+fileName);
        } catch (Exception e) {
            e.printStackTrace();
            // 2.返回上传图片的结果，如果出现异常，则调用PictureResult的error方法返回PictureResult对象
            return PictureResult.error();
        }
    }

}
