package com.jk.dao;

import com.jk.pojo.FareBean;
import com.jk.pojo.MusicBean;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by IntelliJ IDEA.
 * User: 李旺
 * Date: 2020/12/31
 * Time: 10:55
 */
public interface MusicDaoEs extends ElasticsearchRepository<MusicBean,Integer> {

}
