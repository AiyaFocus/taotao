package com.aiyafocus.taotao.portal.controller;

import com.aiyafocus.taotao.common.bo.BigContent;
import com.aiyafocus.taotao.common.utils.JsonUtils;
import com.aiyafocus.taotao.portal.service.ContentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    /**
     * 查询所有商品分类
     * @param callback 因为存在跨域问题，需要通过JSONP这种方式解决，所以需要接收前端发送给后台服务器的回调函数名称
     * @return 返回一个String类型的字符串给前端页面，该字符串实为一个前端JSONP需要的回调函数
     */
    @GetMapping("/rest/itemcat/all")
    public String selectItemCategory(String callback){
        // 查询所有商品分类，返回指定格式的Map集合
        Map<String, List> itemCategoryList = contentService.selectItemCategory();
        // 返回前端页面需要的回调函数
        return callback+"("+JsonUtils.serialize(itemCategoryList)+")";
    }
}
