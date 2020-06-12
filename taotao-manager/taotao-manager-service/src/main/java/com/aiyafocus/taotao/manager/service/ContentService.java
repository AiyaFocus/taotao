package com.aiyafocus.taotao.manager.service;

import com.aiyafocus.taotao.common.bo.BaseResult;
import com.aiyafocus.taotao.common.bo.ContentCategoryTree;
import com.aiyafocus.taotao.common.bo.PageResult;
import com.aiyafocus.taotao.manager.pojo.TbContent;
import com.aiyafocus.taotao.manager.pojo.TbContentCategory;

import java.util.List;

/**
 * 内容业务逻辑层接口
 *
 * @author AiyaFocus
 * createDate 2020/6/9 21:35
 */
public interface ContentService {

    /**
     * 内容分类信息查询
     * @param parentId 父类ID
     * @return 根据parentId返回查询到的内容分类信息
     */
    List<ContentCategoryTree> selectContentCategory(Long parentId);

    /**
     * 添加内容分类信息
     * @param tbContentCategory 接收前端页面发送给服务器的数据，直接封装到TbContentCategory对象中
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    BaseResult addContentCategory(TbContentCategory tbContentCategory);

    /**
     * 重命名内容分类名称
     * @param tbContentCategory 接收前端页面发送给服务器的数据，直接封装到TbContentCategory对象中
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    BaseResult renameContentCategory(TbContentCategory tbContentCategory);

    /**
     * 删除内容分类信息
     * @param parentId 要删除的内容分类信息的父ID
     * @param id 要删除的内容分类信息的ID
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    BaseResult deleteContentCategory(Long parentId, Long id);

    /**
     * 根据内容分类ID分页查询该分类下的所有内容信息
     * @param categoryId 内容分类ID
     * @param page 当前页码
     * @param rows 每页显示条数
     * @return 根据分页条件得到PageResult对象并返回
     */
    PageResult selectContent(Long categoryId, Integer page, Integer rows);

    /**
     * 添加内容信息
     * @param tbContent 接收前端页面发送给服务器的数据，直接封装到TbContent对象中
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    BaseResult addContent(TbContent tbContent);

    /**
     * 修改内容信息
     * @param tbContent 接收前端页面发送给服务器的数据，直接封装到TbContent对象中
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    BaseResult editContent(TbContent tbContent);

    /**
     * 根据ID删除对应的内容信息
     * @param ids 要删除的内容信息的ID，注意可以同时删除多个内容信息
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    BaseResult deleteContent(Long[] ids);
}
