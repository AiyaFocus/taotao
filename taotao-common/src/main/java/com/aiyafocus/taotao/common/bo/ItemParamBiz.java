package com.aiyafocus.taotao.common.bo;

import com.aiyafocus.taotao.manager.pojo.TbItemParam;

/**
 * 重新封装ItemParam类，添加额外属性，使其满足承载页面需要的数据
 *
 * @author AiyaFocus
 * createDate 2020/6/1 14:41
 */
public class ItemParamBiz extends TbItemParam {

    private String itemCatName; // 商品类目名称

    public String getItemCatName() {
        return itemCatName;
    }

    public void setItemCatName(String itemCatName) {
        this.itemCatName = itemCatName;
    }
}
