package com.jk.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by IntelliJ IDEA.
 * User: 李旺
 * Date: 2020/12/28
 * Time: 19:57
 */
@Data
@Document(indexName = "fare",type = "t_fare")
public class FareBean {
    @Id
    private Integer fareId;
    private String fareName;
    private Integer typeId;
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
    private String start;
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
    private String end;
    private Double farePrice;
    private String fareInfo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String fareDate;

    @Transient
    private Double startPrice;
    @Transient
    private Double endPrice;
    @Transient
    private String zhan;
}
