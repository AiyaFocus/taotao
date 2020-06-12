package com.aiyafocus.taotao.manager.controller;

import com.aiyafocus.taotao.common.bo.BaseResult;
import com.aiyafocus.taotao.common.bo.ItemCategoryTree;
import com.aiyafocus.taotao.common.bo.PageResult;
import com.aiyafocus.taotao.manager.pojo.TbItem;
import com.aiyafocus.taotao.manager.service.ItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目控制器（项目名：taotao_manager_web_war）
 *
 * @author AiyaFocus
 * createDate 2020/5/25 10:42
 */
@RestController
public class ItemController {

    @Resource
    ItemService itemService;

    /**
     * 分页查询Item项
     * @param page 当前页
     * @param rows 每页显示行数
     * @return 根据分页条件得到PageResult对象并返回
     */
    @GetMapping("/item/list")
    public PageResult itemList(Integer page, Integer rows){
        return itemService.selectItemList(page, rows);
    }

    /**
     * 删除、下架、上架商品（本质上是修改商品的status，数据库商品状态：商品状态，1-正常，2-下架，3-删除）
     * @param ids 要修改的商品，注意可以同时修改多个商品状态
     * @return 将删除商品的结果的状态码封装成Map对象返回给页面
     */
    @PostMapping("/rest/item/{url}")
    public Map<String, Integer> delete(Long[] ids, @PathVariable("url") String url){
        // 创建一个Map对象，作为返回给前台页面的对象
        Map<String, Integer> map = new HashMap<>();

        // 根据请求判断用户想完成的操作，设置商品需要修改的状态码
        byte itemStatus = 0;
        switch (url) {
            case "delete":
                itemStatus = 3;
                break;
            case "instock":
                itemStatus = 2;
                break;
            case "reshelf":
                itemStatus = 1;
                break;
        }

        // 调用ItemService的方法去修改商品的状态信息，并得到返回给页面的状态码
        int status = itemService.updateByPrimaryKeySelective(ids, itemStatus);

        // 将状态码存入Map对象中
        map.put("status", status);

        // 返回Map对象给页面
        return map;
    }

    /**
     * 查询分类信息方法
     * @return 返回分类信息对象集合
     */
    @PostMapping("/item/cat/list")
    public List<ItemCategoryTree> categoryList(
            @RequestParam(name = "id", defaultValue = "0", required = false) Long parentId){
        // 查询分类信息
        return itemService.selectCategoryList(parentId);
    }

    /**
     * 根据请求中的categoryId，查询TbItemParam对象及相关信息
     *
     * @param categoryId 分类ID
     * @return 返回根据前台页面需要的数据封装好的BaseResult对象
     */
    @GetMapping("/item/param/query/itemcatid/{categoryId}")
    public BaseResult selectTbItemParam(
            @PathVariable("categoryId") Long categoryId){
        // 方法一：直接将前台需要的数据封装为Map对象返回给前端页面
//        Map<String, Object> map = new HashMap<>();
//        map.put("status", 200);
//        map.put("data", itemService.selectTbItemParam(categoryId));
//        return map;
        // 方法二：封装一个BO对象，也就是业务逻辑对象保存数据，并返回给前端页面
        return BaseResult.ok(itemService.selectTbItemParam(categoryId));
    }

    /**
     * 分页查询ItemParam项
     * @param page 当前页
     * @param rows 每页显示行数
     * @return 根据分页条件得到的PageResult对象并返回
     */
    @GetMapping("/item/param/list")
    public PageResult itemParamList(Integer page, Integer rows){
        return itemService.selectItemParamList(page, rows);
    }

    /**
     * 添加一条商品类目的规格参数信息
     * @param itemCategoryId 商品类目的ID
     * @param paramData 规格参数数据
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @PostMapping("/item/param/save/{itemCategoryId}")
    public BaseResult insertItemParam(
            @PathVariable("itemCategoryId") Long itemCategoryId,
            String paramData){
        // 调用Service层插入ItemParam数据的方法，并返回封装了前端需要的数据的BaseResult对象
        return itemService.insertItemParam(itemCategoryId, paramData);

    }

    /**
     * 删除一条商品类目的规格参数信息
     * @param ids 需要删除的商品类目ID，可以有多个
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @PostMapping("/item/param/delete")
    public BaseResult deleteItemParam(Long[] ids){
        return itemService.deleteItemParam(ids);
    }

    /**
     * 添加商品
     * @param tbItem 接收前台发送的添加商品信息所需要的大部分数据
     * @param desc 商品描述
     * @param itemParams 商品参数
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @PostMapping("/item/save")
    public BaseResult addItem(TbItem tbItem, String desc, String itemParams){
        // 设置一个变量保存调用添加商品的方法的结果，默认为false
        // 这样做的原因是，如果添加商品失败，则会抛出一个运行时异常，无法返回正常数据给前端页面
        boolean result = false;
        result = itemService.addItem(tbItem, desc, itemParams);
        return result ? BaseResult.ok(result) : BaseResult.error(result);
    }

}
