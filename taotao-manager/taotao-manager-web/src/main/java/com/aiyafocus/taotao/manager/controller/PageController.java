package com.aiyafocus.taotao.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 页面跳转控制器类
 *
 * @author AiyaFocus
 * @date 2020/5/24 15:44
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/{page}")
    public String index(@PathVariable("page") String page){
        return page;
    }

}
