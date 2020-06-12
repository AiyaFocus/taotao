package com.aiyafocus.taotao.common.bo;

import com.aiyafocus.taotao.manager.pojo.TbItemCat;

/**
 * 返回给页面的数据对象，是处理商品分类的
 * 页面需要的数据格式为：
 * catTree:
 * 	节点如果是父节点state就是closed, 如果不是父节点state就是open
 * 	集合:
 * 		Long id, Long parentId, String name, Boolean isParent, String text,String state;
 *
 * @author AiyaFocus
 * createDate 2020/5/30 16:32
 */
public class ItemCategoryTree extends TbItemCat {

    private String text; // 商品分类名称
    private String state; // 商品分类状态，catTree要求：节点如果是父节点state就是closed，如果不是父节点state就是open

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
