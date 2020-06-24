package com.aiyafocus.taotao.common.bo;

import java.util.List;

/**
 * 商品分类数据结构实体类对象
 *
 * @author AiyaFocus
 * createDate 2020/6/24 14:15
 */
public class ItemCategoryDataStructure {
    private String u;
    private String n;
    private List i;

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public List getI() {
        return i;
    }

    public void setI(List i) {
        this.i = i;
    }

    public ItemCategoryDataStructure(String u, String n, List i) {
        this.u = u;
        this.n = n;
        this.i = i;
    }

    public ItemCategoryDataStructure() {
    }
}
