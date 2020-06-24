package com.aiyafocus.taotao.portal.service.impl;

import com.aiyafocus.taotao.common.bo.BigContent;
import com.aiyafocus.taotao.common.bo.ItemCategoryDataStructure;
import com.aiyafocus.taotao.manager.dao.TbContentMapper;
import com.aiyafocus.taotao.manager.dao.TbItemCatMapper;
import com.aiyafocus.taotao.manager.pojo.TbContent;
import com.aiyafocus.taotao.manager.pojo.TbContentExample;
import com.aiyafocus.taotao.manager.pojo.TbItemCat;
import com.aiyafocus.taotao.manager.pojo.TbItemCatExample;
import com.aiyafocus.taotao.portal.service.ContentService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内容信息业务逻辑层接口的实现类
 *
 * @author AiyaFocus
 * createDate 2020/6/15 21:26
 */
@Service("contentService")
public class ContentServiceImpl implements ContentService {

    @Resource
    TbContentMapper tbContentMapper;

    @Resource
    TbItemCatMapper tbItemCatMapper;

    /**
     * 查询客户端首页大广告要求的内容信息集合
     *
     * @return 返回客户端首页大广告要求的内容信息集合
     */
    @Override
    public List<BigContent> selectContent() {
        // 创建TbContentExample对象
        TbContentExample tbContentExample = new TbContentExample();
        // 创建内容信息集合查询规则，要求查询大广告相关的内容信息，所以需要指定category_id列值为89
        tbContentExample.createCriteria().andCategoryIdEqualTo(89L);
        // 根据查询条件查询内容信息集合并接收
        List<TbContent> tbContentList = tbContentMapper.selectByExample(tbContentExample);
        // 将查询的List<TbContent>的数据转换到List<BigContent>集合中，并返回
        return tbContentList.stream().map(tbContent -> {
            return new BigContent(
                    tbContent.getPic(), tbContent.getPic2(),
                    tbContent.getUrl(), tbContent.getTitle()
            );
        }).collect(Collectors.toList());
    }

    /**
     * 查询所有商品分类
     *
     * @return 返回指定格式的Map集合
     */
    @Override
    public Map<String, List> selectItemCategory() {
        // 创建一个Map集合对象存储数据
        Map<String, List> map = new HashMap<>();
        // 根据前端要求的数据结构添加一个键值对数据
        map.put("data", selectItemCategoryDataStructure(0L));
        // 返回Map集合
        return map;
    }

    /**
     * 查询商品分类数据结构
     * @param parentId 根据父ID查询商品分类信息
     * @return 返回一个符合前端要求的商品分类数据结构List集合
     */
    private List selectItemCategoryDataStructure(Long parentId){
        // 为了前端页面美观，一般不需要显示所有分类，按照前台页面的布局，设置只查询14个一级分类
        if (parentId == 0L) {
            PageHelper.startPage(1, 14);
        }
        // 创建一个TbItemCatExample对象
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        // 设置查询商品分类规则，根据商品分类的父ID查询商品分类
        tbItemCatExample.createCriteria().andParentIdEqualTo(parentId);
        // 调用DAO层方法，查询商品分类信息，并得到查询到的商品分类信息集合
        List<TbItemCat> itemCatList = tbItemCatMapper.selectByExample(tbItemCatExample);

        // 根据商品分类数据格式分析需要的数据：分类ID和分类名称
        // 创建一个List集合
        List list = new ArrayList();
        // 循环根据父ID查询到的商品分类信息集合
        for (TbItemCat tbItemCat : itemCatList) {
            // 判断当前分类是否是父级分类
            if (tbItemCat.getIsParent()) {
                // 如果是，则按照前端要求的父级分类格式添加数据
                // 调用ItemCategoryDataStructure带参构造方法，创建ItemCategoryDataStructure对象，并按照前端要求的分类格式添加数据
                ItemCategoryDataStructure icds = new ItemCategoryDataStructure(
                        "/products/"+tbItemCat.getId()+".html",
                        parentId == 0L ? "<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>" : tbItemCat.getName(),
                        selectItemCategoryDataStructure(tbItemCat.getId())
                );
                // 将创建的ItemCategoryDataStructure对象，添加到List集合中
                list.add(icds);
            } else {
                // 如果不是，则按照前端要求的子级分类格式添加数据
                list.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
            }
        }
        // 返回List集合
        return list;
    }
}
