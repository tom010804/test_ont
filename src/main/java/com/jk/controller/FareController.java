package com.jk.controller;

import com.jk.pojo.FareBean;
import com.jk.service.FareServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: 李旺
 * Date: 2020/12/28
 * Time: 20:06
 */
@Controller
@RequestMapping("fare")
public class FareController {

    @Autowired
    private FareServie fareServie;

    @RequestMapping("saveFare")
    @ResponseBody
    public void saveFare(FareBean fareBean){
        fareServie.saveFare(fareBean);
    }

    @RequestMapping("findAll")
    @ResponseBody
    public HashMap<String,Object> findAll(Integer rows,Integer page,FareBean fareBean){
        return fareServie.findAll(rows,page,fareBean);
    }
    @RequestMapping("findAll1")
    @ResponseBody
    public HashMap<String,Object> findAll1(Integer rows,Integer page,FareBean fareBean){
        return fareServie.findAll1(rows,page,fareBean);
    }

    @RequestMapping("delFare")
    @ResponseBody
    public void delFare(Integer fareId){
        fareServie.delFare(fareId);
    }

    @RequestMapping("findFareById")
    @ResponseBody
    public FareBean findFareById(Integer fareId){
        return fareServie.findFareById(fareId);
    }

}
