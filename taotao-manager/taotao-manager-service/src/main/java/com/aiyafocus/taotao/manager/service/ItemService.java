package com.aiyafocus.taotao.manager.service;

import com.aiyafocus.taotao.common.bo.BaseResult;
import com.aiyafocus.taotao.common.bo.ItemCategoryTree;
import com.aiyafocus.taotao.common.bo.PageResult;
import com.aiyafocus.taotao.manager.pojo.TbItem;
import com.aiyafocus.taotao.manager.pojo.TbItemParam;

import java.util.List;

/**
 * 处理商品信息项的业务逻辑接口
 *
 * @author AiyaFocus
 * createDate 2020/5/27 11:16
 */
public interface ItemService {

    /**
     * 查询所有商品信息
     * @param page 当前页
     * @param rows 显示条数
     * @return 返回给页面easyui-datagrid的数据封装在该类的对象中
     */
    PageResult selectItemList(Integer page, Integer rows);

    /**
     * 修改商品状态信息
     * @param ids 商品的id，可以有多个商品ID
     * @param status 要修改的商品状态信息（商品状态，1-正常，2-下架，3-删除）
     * @return 是否成功或者失败的状态码，若修改成功则返回的状态码为200，不成功则可以是任意数值
     */
    int updateByPrimaryKeySelective(Long[] ids, Byte status) throws RuntimeException;

    /**
     * 根据parentId查询分类信息
     * @param parentId 上级分类id
     * @return 返回分类信息集合
     */
    List<ItemCategoryTree> selectCategoryList(Long parentId);

    /**
     * 根据请求中的categoryId，查询TbItemParam对象
     * @param categoryId 分类ID
     * @return 返回TbItemParam对象
     */
    TbItemParam selectTbItemParam(Long categoryId);

    /**
     * 分页查询ItemParam项
     * @param page 当前页
     * @param rows 每页显示行数
     * @return 返回根据分页条件得到的ItemParam集合
     */
    PageResult selectItemParamList(Integer page, Integer rows);

    /**
     * 添加一条商品类目的规格参数信息
     * @param itemCategoryId 商品类目的ID
     * @param paramData 规格参数数据
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    BaseResult insertItemParam(Long itemCategoryId, String paramData);

    /**
     * 删除一条商品类目的规格参数信息
     * @param ids 需要删除的商品类目ID，可以有多个
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    BaseResult deleteItemParam(Long[] ids);

    /**
     * 添加商品
     * @param tbItem 接收前台发送的添加商品信息所需要的大部分数据
     * @param desc 商品描述
     * @param itemParams 商品参数
     * @return 返回添加商品的结果
     */
    boolean addItem(TbItem tbItem, String desc, String itemParams);
}
