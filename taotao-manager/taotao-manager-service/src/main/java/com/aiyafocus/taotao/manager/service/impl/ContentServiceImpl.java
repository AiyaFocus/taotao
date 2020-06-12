package com.aiyafocus.taotao.manager.service.impl;

import com.aiyafocus.taotao.common.bo.BaseResult;
import com.aiyafocus.taotao.common.bo.ContentCategoryTree;
import com.aiyafocus.taotao.common.bo.PageResult;
import com.aiyafocus.taotao.manager.dao.TbContentCategoryMapper;
import com.aiyafocus.taotao.manager.dao.TbContentMapper;
import com.aiyafocus.taotao.manager.pojo.TbContent;
import com.aiyafocus.taotao.manager.pojo.TbContentCategory;
import com.aiyafocus.taotao.manager.pojo.TbContentCategoryExample;
import com.aiyafocus.taotao.manager.pojo.TbContentExample;
import com.aiyafocus.taotao.manager.service.ContentService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 内容业务逻辑层接口实现类
 *
 * @author AiyaFocus
 * createDate 2020/6/9 21:36
 */
@Service("contentService")
public class ContentServiceImpl implements ContentService {

    @Resource
    TbContentCategoryMapper tbContentCategoryMapper;
    @Resource
    TbContentMapper tbContentMapper;

    /**
     * 内容分类信息查询
     * @param parentId 父类ID
     * @return 根据parentId返回查询到的内容分类信息
     */
    @Override
    public List<ContentCategoryTree> selectContentCategory(Long parentId) {
        // 1.调用DAO层查询内容分类信息的方法
        // 创建TbContentCategoryExample对象
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        // 创建TbContentCategoryExample对象的查询规则：
        // a.根据父级ID查询内容分类信息；
        // b.内容分类的状态值不为2（根据数据库的要求：内容分类状态为可选值:“1”表示正常，“2”表示删除）
        // c.同级内容分类信息的展现次序，如数值相等则按名称次序排列
        tbContentCategoryExample.createCriteria()
                .andParentIdEqualTo(parentId)
                .andStatusNotEqualTo(2);
        // 设置同级内容分类信息的展现次序（排序规则）
        tbContentCategoryExample.setOrderByClause("sort_order,name asc");
        // 调用查询内容分类信息的方法，并得到返回的内容分类信息集合
        List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);

        // 2.将查询到的TbContentCategory集合转换成ContentCategoryTree集合，并返回ContentCategoryTree集合
        return tbContentCategories.stream().map(tbContentCategory -> {
            // 创建ContentCategoryTree对象
            ContentCategoryTree contentCategoryTree = new ContentCategoryTree();
            // 拷贝tbContentCategory对象中的值，到contentCategoryTree对象中
            BeanUtils.copyProperties(tbContentCategory, contentCategoryTree);
            // 设置内容分类名称
            contentCategoryTree.setText(tbContentCategory.getName());
            // 设置内容分类状态，catTree要求：节点如果是父节点state就是closed，如果不是父节点state就是open
            contentCategoryTree.setState(tbContentCategory.getIsParent() ? "closed" : "open");
            // 返回ContentCategoryTree对象
            return contentCategoryTree;
        }).collect(Collectors.toList());

    }

    /**
     * 添加内容分类信息
     *
     * @param tbContentCategory 接收前端页面发送给服务器的数据，直接封装到TbContentCategory对象中
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @Override
    @Transactional
    public BaseResult addContentCategory(TbContentCategory tbContentCategory) {
        // 1.准备要插入数据库中的数据并封装到TbContentCategory对象中
        // TbContentCategory对象中id属性为数据库自增，所以不用设置
        // TbContentCategory对象中parentId属性、name属性为前端页面发送到服务器的数据，所以不用设置
        // TbContentCategory对象中status属性，根据数据库的要求，状态码：“1”表示正常，“2”表示删除，并且该字段默认为1，所以不用设置
        // TbContentCategory对象中sortOrder属性，排列序号暂时没有要求按照什么规则设置，并且该字段默认为NULL，所以不用设置
        // TbContentCategory对象中isParent属性，根据数据库的要求：该类目是否为父类目，1为true，0为false，并且该字段默认为1
        // 当添加一个内容分类节点的时候，该内容分类节点下没有子节点，所以isParent属性应该设置为false
        tbContentCategory.setIsParent(false);
        // TbContentCategory对象中created属性、updated属性取当前时间，并设值
        Date date = new Date();  // 获取当前时间
        tbContentCategory.setCreated(date);  // 设置创建时间
        tbContentCategory.setUpdated(date);  // 设置更新时间

        // 2.调用DAO层的添加内容分类信息的方法，并接收受影响的行数
        int result = tbContentCategoryMapper.insertSelective(tbContentCategory);

        // 3.由于添加了新的子节点，所以需要判断该子节点的父节点的isParent属性是否为true，如果不为true，则需要将该属性修改为true
        // 根据子节点的parentId属性，查询父节点的所有信息放入一个TbContentCategory对象中
        TbContentCategory tbContentCategoryParent = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
        // 判断tbContentCategoryParent对象的isParent属性是否为true
        if (!tbContentCategoryParent.getIsParent()) {
            // 如果不为true，则需要将该属性修改为true
            /*
                说明：由于只需要根据id修改is_parent和updated列值，
                    但tbContentCategoryParent对象中存在有所有属性值，
                    从节约服务器性能角度考虑，将不需要的属性值置空，或者重新创建一个对象并根据需要赋值。
             */
            tbContentCategoryParent.setParentId(null);
            tbContentCategoryParent.setName(null);
            tbContentCategoryParent.setStatus(null);
            tbContentCategoryParent.setSortOrder(null);
            tbContentCategoryParent.setIsParent(true);  // 设置isParent属性值为true
            tbContentCategoryParent.setCreated(null);
            tbContentCategoryParent.setUpdated(new Date());  // 设置修改时间为当前时间
            // 调用DAO层根据ID修改内容分类信息的方法，并接收返回的受影响行数
            result += tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategoryParent);

            // 判断受影响行数结果，如果受影响行数不为2，则表示有SQL语句出现问题，则抛出运行时异常触发事务回滚
            if (result != 2) {
                throw new RuntimeException("添加或者修改出现问题，请检查！");
            }

            // 4.根据受影响行数确定返回哪个BaseResult对象
            return BaseResult.ok(result);
        } else {
            // 4.根据受影响行数确定返回哪个BaseResult对象
            return result == 1 ? BaseResult.ok(result) : BaseResult.error(result);
        }

    }

    /**
     * 重命名内容分类名称
     *
     * @param tbContentCategory 接收前端页面发送给服务器的数据，直接封装到TbContentCategory对象中
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @Override
    public BaseResult renameContentCategory(TbContentCategory tbContentCategory) {
        // 设置TbContentCategory对象的修改时间为当前时间
        tbContentCategory.setUpdated(new Date());

        // 调用DAO层根据主键ID修改其他信息的方法，并接收受影响的行数
        int result = tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);

        // 根据受影响行数确定返回哪个BaseResult对象
        return result == 1 ? BaseResult.ok(result) : BaseResult.error(result);
    }

    /**
     * 删除内容分类信息
     *
     * @param parentId 要删除的内容分类信息的父ID
     * @param id       要删除的内容分类信息的ID
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @Override
    @Transactional
    public BaseResult deleteContentCategory(Long parentId, Long id) {
        // 1.判断当前要删除的节点是否是父节点，有两种情况，是父节点或不是父节点
        // 获取当前要删除的节点对象
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        if (tbContentCategory.getIsParent()) {
            // 2.是父节点，则不能删除该节点
            return BaseResult.error("该内容分类下还存在其他分类，不能删除！");
        } else {
            // 3.不是父节点，则删除该节点
            // 调用DAO层的根据ID删除内容分类信息的方法，并接收受影响行数
            int result = tbContentCategoryMapper.deleteByPrimaryKey(id);

            // 4.判断删除的该节点的父节点下是否还有其他子节点，如果没有其他子节点，则需要修改该父节点的is_parent列的值
            // 4-1.根据parentId查询所有parentId为指定值的内容分类信息集合
            // 创建TbContentCategoryExample对象
            TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
            // 设置TbContentCategoryExample对象查询规则
            tbContentCategoryExample.createCriteria().andParentIdEqualTo(parentId);
            // 调用DAO层方法，查询所有parentId为指定值的内容分类信息集合
            List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
            // 4-2.判断查询出来的内容分类信息集合中是否存在内容分类信息对象
            if (tbContentCategories!=null && tbContentCategories.size()>0) {
                // 4-3.若存在，则不需要修改，正常返回封装了符合前端要求的数据的一个BaseResult对象
                return BaseResult.ok(result);
            } else {
                // 4-4.若不存在，则根据ID修改该节点的is_parent列的值
                // 创建TbContentCategory对象，并设置id、isParent、updated属性值
                TbContentCategory tbContentCategoryParent = new TbContentCategory(parentId, null, null, null, null, false, null, new Date());
                // 调用DAO层根据ID修改列值的方法，并接收返回的受影响的行数
                result += tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategoryParent);
                // 5.判断受影响行数结果，如果受影响行数不为2，则表示有SQL语句出现问题，则抛出运行时异常触发事务回滚
                if (result != 2) {
                    throw new RuntimeException("删除或者修改出现问题，请检查！");
                }

                // 6.正常返回封装了符合前端要求的数据的一个BaseResult对象
                return BaseResult.ok(result);
            }
        }
    }

    /**
     * 根据内容分类ID分页查询该分类下的所有内容信息
     *
     * @param categoryId 内容分类ID
     * @param page       当前页码
     * @param rows       每页显示条数
     * @return 根据分页条件得到PageResult对象并返回
     */
    @Override
    public PageResult selectContent(Long categoryId, Integer page, Integer rows) {
        // 调用PageHelper静态方法，将页码和每页显示的条数告诉PageHelper，让PageHelper帮助处理数据
        PageHelper.startPage(page, rows);

        // 创建TbContentExample对象
        TbContentExample tbContentExample = new TbContentExample();
        // 设置TbContentExample对象查询规则，根据前端发送的分类ID进行查询该分类下的内容信息
        tbContentExample.createCriteria().andCategoryIdEqualTo(categoryId);
        // 调用DAO层，得到根据分类Id条件查询该分类下的所有内容信息（此时分页相关已由PageHelper提前处理注入完成）
        List<TbContent> tbContents = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);

        // 调用Service层公共的分页查询通用方法，返回PageResult对象
        return ServiceCommonCode.pagedQuery(tbContents);
    }

    /**
     * 添加内容信息
     * @param tbContent 接收前端页面发送给服务器的数据，直接封装到TbContent对象中
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @Override
    public BaseResult addContent(TbContent tbContent){
        // 创建一个Date对象，获取当前时间
        Date date = new Date();
        // 设置tbContent对象的created和updated的属性值
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        // 调用DAO层添加内容信息方法，并接收返回的受影响行数
        int result = tbContentMapper.insertSelective(tbContent);
        // 根据受影响行数确定返回哪个BaseResult对象
        return result == 1 ? BaseResult.ok(result) : BaseResult.error(result);
    }

    /**
     * 修改内容信息
     *
     * @param tbContent 接收前端页面发送给服务器的数据，直接封装到TbContent对象中
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @Override
    public BaseResult editContent(TbContent tbContent) {
        // 创建一个Date对象，获取当前时间
        Date date = new Date();
        // 设置tbContent对象的updated的属性值
        tbContent.setUpdated(date);
        // 调用DAO层修改内容信息方法，并接收返回的受影响行数
        int result = tbContentMapper.updateByPrimaryKeySelective(tbContent);
        // 根据受影响行数确定返回哪个BaseResult对象
        return result == 1 ? BaseResult.ok(result) : BaseResult.error(result);

    }

    /**
     * 根据ID删除对应的内容信息
     *
     * @param ids 要删除的内容信息的ID，注意可以同时删除多个内容信息
     * @return 返回封装了符合前端要求的数据的一个BaseResult对象
     */
    @Override
    public BaseResult deleteContent(Long[] ids) {
        // 创建TbContentExample对象
        TbContentExample tbContentExample = new TbContentExample();
        // 设置TbContentExample对象删除规则，根据前端发送的多个要删除的内容信息的ID，进行一一删除
        /*
            删除SQL语句的where条件可以写成id in (多个id)，
            所以调用andIdIn()方法，但是andIdIn()方法需要的参数为List，
            所以需要将前端发送的多个内容信息ID的数组转换成List集合，
            可以利用Arrays工具类中的asList()方法，将数组转换成集合。
          */
        tbContentExample.createCriteria().andIdIn(Arrays.asList(ids));
        // 调用DAO层根据条件删除内容信息的方法，删除多个内容信息，并接收受影响行数
        // 注意：此处返回的受影响行数，是根据要删除的所有内容信息的ID个数来的。
        // 例如：要删除的内容信息的ID有3个，那么返回的受影响行数就应该是3。
        int result = tbContentMapper.deleteByExample(tbContentExample);
        // 根据受影响行数确定返回哪个BaseResult对象
        return result == ids.length ? BaseResult.ok(result) : BaseResult.error(result);
    }

}
