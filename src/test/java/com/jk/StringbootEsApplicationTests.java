package com.jk;

import com.jk.dao.MusicDaoEs;
import com.jk.pojo.MusicBean;
import com.jk.service.MusicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class StringbootEsApplicationTests {

    @Autowired
    private MusicService musicService;

    @Test
    public void save(){
        MusicBean music = new MusicBean();
        music.setId(2);
        music.setName("让世界充满爱");
        music.setAuthor("老王");
        music.setCreatedate("20018-11-12");
        music.setPrice(76.0);
        music.setInfo("没有任何描述");
        musicService.save(music);
    }

}
