package com.aaaa.falas.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by gaoyuan on 2018/6/25.
 */

@Entity
public class CollectBean {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "img")
    @NotNull
    private String img;

    @NotNull
    @Property(nameInDb = "name")
    private String name;

    @NotNull
    @Property(nameInDb = "num")
    private String num;

    @NotNull
    @Unique
    @Property(nameInDb = "url")
    private String url;

    @Generated(hash = 1361160663)
    public CollectBean(Long id, @NotNull String img, @NotNull String name,
            @NotNull String num, @NotNull String url) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.num = num;
        this.url = url;
    }

    @Generated(hash = 420494524)
    public CollectBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
