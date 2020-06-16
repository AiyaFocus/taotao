package com.aiyafocus.taotao.portal.service;

import com.aiyafocus.taotao.common.bo.BigContent;

import java.util.List;

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
}
