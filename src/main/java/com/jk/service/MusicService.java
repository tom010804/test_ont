package com.jk.service;

import com.jk.pojo.MusicBean;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: 李旺
 * Date: 2020/12/31
 * Time: 10:54
 */
public interface MusicService {
    void save(MusicBean music);

    HashMap<String, Object> findAll(int page, int rows, MusicBean musicBean);
}
