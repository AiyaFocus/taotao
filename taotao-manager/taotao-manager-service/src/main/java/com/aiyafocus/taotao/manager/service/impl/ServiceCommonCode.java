package com.aiyafocus.taotao.manager.service.impl;

import com.aiyafocus.taotao.common.bo.PageResult;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 业务类通用代码提取类
 *
 * @author AiyaFocus
 * createDate 2020/6/11 16:53
 */
class ServiceCommonCode {

    /**
     * 分页查询封装返回给页面数据的通用方法
     * @param t 查询的数据集合
     * @param <T> 查询的数据类型
     * @return 返回PageResult对象，页面easyui-datagrid需要的数据封装在PageResult类的对象中
     */
    static <T> PageResult pagedQuery(List<T> t){
        // 创建PageInfo对象，获取当前页面信息
        PageInfo<T> pageInfo = new PageInfo<>(t);
        // 2.准备要返回的PageResult对象
        PageResult pageResult = new PageResult();
        // 从PageInfo对象中获取总条数，设置到PageResult对象中
        pageResult.setTotal(pageInfo.getTotal());
        // 从PageInfo对象中获取页面要显示的数据信息，设置到PageResult对象中
        pageResult.setRows(pageInfo.getList());
        // 返回PageResult对象
        return pageResult;
    }

}
