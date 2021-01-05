package com.jk.controller;

import com.jk.pojo.MusicBean;
import com.jk.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: 李旺
 * Date: 2020/12/31
 * Time: 10:53
 */
@Controller
@RequestMapping("music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @RequestMapping("findAll")
    @ResponseBody
    public HashMap<String,Object> findAll(int page,int rows,MusicBean musicBean){
        return musicService.findAll(page,rows,musicBean);
    }
}
