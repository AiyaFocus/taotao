package com.aiyafocus.taotao.portal.service.impl;

import com.aiyafocus.taotao.common.bo.BigContent;
import com.aiyafocus.taotao.manager.dao.TbContentMapper;
import com.aiyafocus.taotao.manager.pojo.TbContent;
import com.aiyafocus.taotao.manager.pojo.TbContentExample;
import com.aiyafocus.taotao.portal.service.ContentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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
}
