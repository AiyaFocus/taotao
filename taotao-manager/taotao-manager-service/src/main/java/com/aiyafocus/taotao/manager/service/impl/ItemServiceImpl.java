package com.aiyafocus.taotao.manager.service.impl;

import com.aiyafocus.taotao.common.bo.BaseResult;
import com.aiyafocus.taotao.common.bo.ItemCategoryTree;
import com.aiyafocus.taotao.common.bo.ItemParamBiz;
import com.aiyafocus.taotao.common.bo.PageResult;
import com.aiyafocus.taotao.common.utils.IDUtils;
import com.aiyafocus.taotao.manager.dao.*;
import com.aiyafocus.taotao.manager.pojo.*;
import com.aiyafocus.taotao.manager.service.ItemService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Item业务逻辑实现类
 *
 * @author AiyaFocus
 * createDate 2020/5/27 11:23
 */
@Service("itemService")
public class ItemServiceImpl implements ItemService {

    @Resource
    TbItemMapper tbItemMapper;
    @Resource
    TbItemCatMapper tbItemCatMapper;
    @Resource
    TbItemParamMapper tbItemParamMapper;
    @Resource
    TbItemDescMapper tbItemDescMapper;
    @Resource
    TbItemParamItemMapper tbItemParamItemMapper;

    /**
     * 查询所有商品信息
     *
     * @param page 当前页
     * @param rows 显示条数
     * @return 返回给页面easyui-datagrid的数据封装在该类的对象中
     */
    public PageResult selectItemList(Integer page, Integer rows) {
        // 调用PageHelper静态方法，将页码和每页显示的条数告诉PageHelper，让PageHelper帮助处理数据
        PageHelper.startPage(page, rows);
        // 调用DAO层，得到查询的所有商品信息（此时分页相关已由PageHelper提前处理注入完成）
        List<TbItem> tbItems = tbItemMapper.selectByExample(new TbItemExample());

        // 调用Service层公共的分页查询通用方法，返回PageResult对象
        return ServiceCommonCode.pagedQuery(tbItems);
    }

    /**
     * 修改商品状态信息
     * @param ids 商品的id，可以有多个商品ID
     * @param status 要修改的商品状态信息（商品状态，1-正常，2-下架，3-删除）
     * @return 是否成功或者失败的状态码，若修改成功则返回的状态码为200，不成功则可以是任意数值
     */
    @Transactional
    public int updateByPrimaryKeySelective(Long[] ids, Byte status) throws RuntimeException {
        int returnStatus = 200;  // 返回的状态码
        // 创建TbItem对象
        TbItem tbItem = new TbItem();
        // 根据商品ID，循环修改多个商品，将多个商品状态改为删除
        for (Long id : ids) {
            // 为TbItem对象需要的部分属性赋值
            tbItem.setId(id);  // 商品ID
            tbItem.setStatus(status);  // 商品状态
            // 调用条件修改方法，得到受影响行数
            int result = tbItemMapper.updateByPrimaryKeySelective(tbItem);
            // 根据受影响行数判断修改是否成功
            if (result == 0) {
                // 如果受影响行数为0，则表示修改失败，那么就抛出一个异常
                // 使用springmvc进行全局异常处理
                throw new RuntimeException("修改多个商品信息状态出现问题。");
            }
        }

        return returnStatus;
    }

    /**
     * 根据parentId查询分类信息
     *
     * @param parentId 上级分类id
     * @return 返回分类信息集合
     */
    public List<ItemCategoryTree> selectCategoryList(Long parentId) {
        // 创建TbItemCatExample对象并设置查询条件
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        tbItemCatExample.createCriteria().andParentIdEqualTo(parentId);
        // 查询分类信息，得到TbItemCat的集合
        List<TbItemCat> tbItemCats = tbItemCatMapper.selectByExample(tbItemCatExample);

        // 将得到的TbItemCat的集合，转换成CategoryTree集合，并返回
        // 本质上就是将TbItemCat集合中的数据转移到CategoryTree集合中
        return tbItemCats.stream().map(tbItemCat -> {
            // 1.创建CategoryTree对象
            ItemCategoryTree categoryTree = new ItemCategoryTree();
            // 2.将TbItemCat对象中的数据，转移到CategoryTree对象中
            /*
                BeanUtils类：是基于Spring提供的工具类，用于高效地复制对象。
                BeanUtils.copyProperties(source, target);
                解释：source是源对象，target是等待被赋值的对象。将source中属性值赋值给target中对应的字段。
                注意：source中有的属性target中必须有，并且target可以有更多属性。
             */
            BeanUtils.copyProperties(tbItemCat, categoryTree);
            // 3.给额外的两个属性text和state赋值
            // text：表示分类名称
            // state：表示分类状态，catTree要求：节点如果是父节点state就是closed，如果不是父节点state就是open
            categoryTree.setText(tbItemCat.getName());
            categoryTree.setState(tbItemCat.getIsParent() ? "closed" : "open");
            // 4.返回设置好属性值的CategoryTree对象
            return categoryTree;
        }).collect(Collectors.toList());

    }

    /**
     * 根据请求中的categoryId，查询TbItemParam对象
     *
     * @param categoryId 分类ID
     * @return 返回TbItemParam对象
     */
    @Override
    public TbItemParam selectTbItemParam(Long categoryId) {
        // 由于是根据categoryId查询分类参数信息，所以需要使用XXXExample对象，调用createCriteria()方法，创建动态SQL语句，进行查询的参数注入
        // 1.创建TbItemParamExample对象
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        // 2.设置根据categoryId值进行查询
        tbItemParamExample.createCriteria().andItemCatIdEqualTo(categoryId);
        // 3.调用查询分类参数对象的方法（注意区分selectByExample和selectByExampleWithBLOBs两个方法的区别）
        List<TbItemParam> tbItemParamList = tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);
        // 4.一般只返回1条数据，所以先判断查询的集合是否为空。若不为空，则返回第一条数据；若为空，则返回null。
        return tbItemParamList!=null && tbItemParamList.size()>0 ? tbItemParamList.get(0) : null;
    }

    /**
     * 分页查询ItemParam项
     *
     * @param page 当前页
     * @param rows 显示条数
     * @return 返回给页面easyui-datagrid的数据封装在PageResult类的对象中
     */
    public PageResult selectItemParamList(Integer page, Integer rows) {
        // 1.准备要返回PageResult对象中填充的数据，total和rows
        // 调用PageHelper静态方法，将页码和每页显示的条数告诉PageHelper，让PageHelper帮助处理数据
        PageHelper.startPage(page, rows);
        // 调用DAO层，得到查询的所有ItemParam信息（此时分页相关已由PageHelper提前处理注入完成）
        List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExampleWithBLOBs(new TbItemParamExample());

        // 由于TbItemParam中的属性不足以承载页面需要的数据，所以将TbItemParam重新封装了一个类ItemParamBiz
        // 将查询到的ItemParam集合的数据，复制到ItemParamBiz集合中
        // 也就是将List<TbItemParam> ---> List<ItemParamBiz>
        List<ItemParamBiz> itemParamBizs = tbItemParams.stream().map(tbItemParam -> {
            // 1.根据TbItemParam的itemCatId，查询相应的分类信息对象
            TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(tbItemParam.getItemCatId());
            // 2.创建ItemParamBiz对象
            ItemParamBiz itemParamBiz = new ItemParamBiz();
            // 3.将TbItemParam对象中的值，转移到ItemParamBiz
            BeanUtils.copyProperties(tbItemParam, itemParamBiz);
            // 4.设置ItemParamBiz对象的分类名称
            itemParamBiz.setItemCatName(tbItemCat.getName());
            // 5.返回ItemParamBiz对象
            return itemParamBiz;
        }).collect(Collectors.toList());

        // 调用Service层公共的分页查询通用方法，返回PageResult对象
        return ServiceCommonCode.pagedQuery(itemParamBizs);
    }

    /**
     * 添加一条商品类目的规格参数信息
     *
     * @param itemCategoryId 商品类目的ID
     * @param paramData      规格参数数据
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @Override
    public BaseResult insertItemParam(Long itemCategoryId, String paramData) {
        // 1.创建TbItemParam对象用以封装数据
        Date date = new Date();
        TbItemParam tbItemParam = new TbItemParam(itemCategoryId, date, date, paramData);
        // 2.调用DAO层新增TbItemParam对象的方法，并得到受影响的行数
        int result = tbItemParamMapper.insertSelective(tbItemParam);
        // 3.根据调用新增方法返回的受影响行数，返回封装了不同数据的BaseResult对象
        if (result != 0){
            return BaseResult.ok(result);
        } else {
            return BaseResult.error(result);
        }
    }

    /**
     * 删除一条商品类目的规格参数信息
     *
     * @param ids 需要删除的商品类目ID，可以有多个
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @Override
    @Transactional
    public BaseResult deleteItemParam(Long[] ids) {
        int result = 0; // 创建一个变量，存储调用删除方法后的受影响行数
        // 循环商品类目的规格参数的ID，并根据id删除该条类目信息
        for (Long id : ids) {
            // 根据ID删除商品类目的规格参数的数据
            result = tbItemParamMapper.deleteByPrimaryKey(id);
            if (result == 0) {
                throw new RuntimeException("删除商品失败，请检查！");
            }
        }
        // 返回封装了符合前端要求的数据的一个BaseResult对象
        return BaseResult.ok(result);
    }

    /**
     * 添加商品
     *
     * @param tbItem     接收前台发送的添加商品信息所需要的大部分数据
     * @param desc       商品描述
     * @param itemParams 商品参数
     * @return 返回添加商品的结果
     */
    @Override
    @Transactional
    public boolean addItem(TbItem tbItem, String desc, String itemParams) {
        // 添加商品，需要添加3张表中的数据：tb_item，tb_item_desc，tb_item_param_item
        // 1.分别准备3张表的数据，也就是创建3个对象，并且填充3个对象中的属性值
        Date date = new Date();  // 获取当前时间，以备以下属性值填充
        // 由于TbItem对象中部分数据已经由前台返回并填充，还剩下id、status、created、updated这四个属性需要设置属性值
        // 获得商品ID需要调用IDUtils中的商品ID生成方法来生成
        Long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);  // 设置商品ID
        tbItem.setStatus((byte) 1);  // 设置商品状态，商品状态分为：1-正常，2-下架，3-删除，由于是添加商品，所以商品状态默认为正常
        tbItem.setCreated(date);  // 设置商品的创建时间
        tbItem.setUpdated(date);  // 设置商品的更新时间
        // 创建TbItemDesc对象并为对象属性赋值
        TbItemDesc tbItemDesc = new TbItemDesc(itemId, date, date, desc);
        // 创建TbItemParamItem对象并为对象属性赋值
        TbItemParamItem tbItemParamItem = new TbItemParamItem(itemId, date, date, itemParams);

        // 2.调用DAO层添加商品信息的方法，分别向3张表中新增数据
        int result = 0;  // 存储分别向3张表插入数据后的受影响行数
        result += tbItemMapper.insert(tbItem);  // 向tb_item表插入商品信息
        result += tbItemDescMapper.insert(tbItemDesc);  // 向tb_item_desc表插入商品描述信息
        result += tbItemParamItemMapper.insert(tbItemParamItem);  // 向tb_item_param_item表插入商品参数信息

        // 3.根据新增数据返回结果，返回成功或者失败给Controller层
        if (result != 3) {
            // result应该存储的是3行受影响，如果不等于3，则表示有数据添加失败，则抛出运行时异常
            throw new RuntimeException("添加商品失败，请检查！");
        }else{
            // result为3，则表示3行受影响，全部数据插入成功，则返回True
            return true;
        }
    }

}
