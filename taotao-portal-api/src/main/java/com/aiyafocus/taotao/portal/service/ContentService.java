package com.aiyafocus.taotao.portal.service;

import com.aiyafocus.taotao.common.bo.BigContent;

import java.util.List;
import java.util.Map;

/**
 * 内容信息业务逻辑层接口
 *
 * @author AiyaFocus
 * createDate 2020/6/15 21:17
 */
public interface ContentService {
    /**
     * 查询客户端首页大广告要求的内容信息集合
     * @return 返回客户端首页大广告要求的内容信息集合
     */
    List<BigContent> selectContent();

    /**
     * 查询所有商品分类
     * @return 返回指定格式的Map集合
     */
    Map<String, List> selectItemCategory();
}
