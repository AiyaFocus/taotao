package com.aiyafocus.taotao.portal.controller;

import com.aiyafocus.taotao.common.utils.HttpClientUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面跳转控制器
 *
 * @author AiyaFocus
 * createDate 2020/6/13 13:42
 */
@Controller
public class PageController {

    /**
     * 默认访问index页面，查询并返回页面需要的数据
     * @param model index页面需要的数据，放在Model中返回
     * @return 指定返回的逻辑视图名（页面名称）
     * @throws Exception 如果出现异常则继续向上抛出
     */
    @GetMapping("/")
    public String index(Model model) throws Exception {
        // 准备index页面需要的数据放到Model中
        // 1.查询index页面需要的“大广告”的数据
        // 请求taotao-portal-api服务器获取需要的“大广告”的Json格式的数据
        String ab1Json = HttpClientUtils.doGet("http://localhost:8081/select/content");
        // 将获取到的数据添加到Model中，带回给页面
        model.addAttribute("ad1", ab1Json);
        return "index";
    }
}
