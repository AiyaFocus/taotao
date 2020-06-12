package com.aiyafocus.taotao.common.bo;

import com.aiyafocus.taotao.manager.pojo.TbContentCategory;

/**
 * 返回给页面的数据对象，是处理内容分类的
 * 页面需要的数据格式为：
 * catTree:
 * 	节点如果是父节点state就是closed, 如果不是父节点state就是open
 * 	集合:
 * 		Long id, Long parentId, String name, Boolean isParent, String text,String state;
 *
 * @author AiyaFocus
 * createDate 2020/6/9 21:25
 */
public class ContentCategoryTree extends TbContentCategory {

    private String text; // 内容分类名称
    private String state; // 内容分类状态，catTree要求：节点如果是父节点state就是closed，如果不是父节点state就是open

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
