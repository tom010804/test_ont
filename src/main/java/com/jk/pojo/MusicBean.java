package com.jk.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by IntelliJ IDEA.
 * User: 李旺
 * Date: 2020/12/31
 * Time: 10:48
 */
@Data
@Document(indexName = "music",type = "t_music")
public class MusicBean {
    @Id
    private Integer id;
    private String name;
    private String author;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String createdate;
    private Double price;
    private String info;

    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String start;
    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String end;
}
