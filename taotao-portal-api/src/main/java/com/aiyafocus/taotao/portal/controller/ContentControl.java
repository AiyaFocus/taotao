package com.aiyafocus.taotao.portal.controller;

import com.aiyafocus.taotao.common.bo.BaseResult;
import com.aiyafocus.taotao.common.bo.BigContent;
import com.aiyafocus.taotao.common.utils.JsonUtils;
import com.aiyafocus.taotao.portal.service.ContentService;
import org.apache.http.entity.ContentType;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 内容信息控制器
 *
 * @author AiyaFocus
 * createDate 2020/6/13 10:47
 */
@RestController
public class ContentControl {

    @Resource
    ContentService contentService;

    /**
     * 查询客户端首页大广告需要的内容信息集合
     * @return 返回查询到的大广告的内容信息集合序列化成Json格式的数据
     */
    @GetMapping("/select/content")
    public String selectContent(){
        // 调用Service层查询内容信息集合，并接收
        List<BigContent> bigContentList = contentService.selectContent();
        // 将接收到的内容信息集合，序列化成Json格式的数据，并返回
        return JsonUtils.serialize(bigContentList);
    }
}
