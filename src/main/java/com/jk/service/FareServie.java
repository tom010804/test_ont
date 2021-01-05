package com.jk.service;

import com.jk.pojo.FareBean;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: 李旺
 * Date: 2020/12/28
 * Time: 20:10
 */
public interface FareServie {
    void saveFare(FareBean fareBean);

    HashMap<String, Object> findAll(Integer rows, Integer page, FareBean fareBean);

    void delFare(Integer fareId);

    FareBean findFareById(Integer fareId);

    HashMap<String, Object> findAll1(Integer rows, Integer page, FareBean fareBean);
}
