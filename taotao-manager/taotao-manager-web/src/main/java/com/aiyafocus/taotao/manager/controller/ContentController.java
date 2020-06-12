package com.aiyafocus.taotao.manager.controller;

import com.aiyafocus.taotao.common.bo.BaseResult;
import com.aiyafocus.taotao.common.bo.ContentCategoryTree;
import com.aiyafocus.taotao.common.bo.PageResult;
import com.aiyafocus.taotao.manager.pojo.TbContent;
import com.aiyafocus.taotao.manager.pojo.TbContentCategory;
import com.aiyafocus.taotao.manager.service.ContentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 内容控制层控制器
 *
 * @author AiyaFocus
 * createDate 2020/6/9 21:05
 */
@RestController
public class ContentController {

    @Resource
    ContentService contentService;

    /**
     * 内容分类信息查询
     * @param parentId 父类ID
     * @return 根据parentId返回查询到的内容分类信息
     */
    @GetMapping("/content/category/list")
    public List<ContentCategoryTree> selectContentCategory(
            @RequestParam(name = "id", required = false, defaultValue = "0") Long parentId){
        return contentService.selectContentCategory(parentId);
    }

    /**
     * 添加内容分类信息
     * @param tbContentCategory 接收前端页面发送给服务器的数据，直接封装到TbContentCategory对象中
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @PostMapping("/content/category/create")
    public BaseResult addContentCategory(TbContentCategory tbContentCategory){
        return contentService.addContentCategory(tbContentCategory);
    }

    /**
     * 重命名内容分类名称
     * @param tbContentCategory 接收前端页面发送给服务器的数据，直接封装到TbContentCategory对象中
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @PostMapping("/content/category/update")
    public BaseResult renameContentCategory(TbContentCategory tbContentCategory){
        return contentService.renameContentCategory(tbContentCategory);
    }

    /**
     * 删除内容分类信息
     * @param parentId 要删除的内容分类信息的父ID
     * @param id 要删除的内容分类信息的ID
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @PostMapping("/content/category/delete/")
    public BaseResult deleteContentCategory(Long parentId, Long id){
        return contentService.deleteContentCategory(parentId, id);
    }

    /**
     * 根据内容分类ID分页查询该分类下的所有内容信息
     * @param categoryId 内容分类ID
     * @param page 当前页码
     * @param rows 每页显示条数
     * @return 根据分页条件得到PageResult对象并返回
     */
    @GetMapping("/content/query/list")
    public PageResult selectContent(Long categoryId, Integer page, Integer rows){
        return contentService.selectContent(categoryId, page, rows);
    }

    /**
     * 添加内容信息
     * @param tbContent 接收前端页面发送给服务器的数据，直接封装到TbContent对象中
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @PostMapping("/content/save")
    public BaseResult addContent(TbContent tbContent){
        return contentService.addContent(tbContent);
    }

    /**
     * 修改内容信息
     * @param tbContent 接收前端页面发送给服务器的数据，直接封装到TbContent对象中
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @PostMapping("/rest/content/edit")
    public BaseResult editContent(TbContent tbContent){
        return contentService.editContent(tbContent);
    }

    /**
     * 根据ID删除对应的内容信息
     * @param ids 要删除的内容信息的ID，注意可以同时删除多个内容信息
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @PostMapping("/content/delete")
    public BaseResult deleteContent(Long[] ids){
        return contentService.deleteContent(ids);
    }

}
