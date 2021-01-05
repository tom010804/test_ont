package com.jk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA.
 * User: 李旺
 * Date: 2020/12/28
 * Time: 20:13
 */
@Controller
@RequestMapping("page")
public class PageController {

    @RequestMapping("show")
    public String show(){
        return "show";
    }
    @RequestMapping("show1")
    public String show1(){
        return "show1";
    }

    @RequestMapping("show2")
    public String show2(){
        return "show2";
    }
}
