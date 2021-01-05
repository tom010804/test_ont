package com.jk.dao;

import com.jk.pojo.FareBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by IntelliJ IDEA.
 * User: 李旺
 * Date: 2020/12/28
 * Time: 20:11
 */
@Mapper
public interface FareDao {

    void saveFare(FareBean fareBean);

    void delFare(Integer fareId);

    void upFare(FareBean fareBean);
}
