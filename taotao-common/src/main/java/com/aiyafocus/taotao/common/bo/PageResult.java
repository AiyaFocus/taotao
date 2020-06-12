package com.aiyafocus.taotao.common.bo;

import java.util.List;

/**
 * BO（Business Object）：业务对象
 * 主要作用是把业务逻辑封装为一个对象。
 * 返回给页面easyui-datagrid的数据封装在该类的对象中
 *
 * easyui-datagrid:
 * 	total:   总条数
 * 	rows:    数据(list集合)
 * 			 	item对象
 *
 * @author AiyaFocus
 * @date 2020/5/27 11:10
 */
public class PageResult {

    private Long total;
    // 使用？代表可以是任意类型的List集合
    private List<?> rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

}
