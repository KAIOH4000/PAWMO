package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("carousel")
public class Carousel {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String cover;
    private Integer goodsId;
    private Integer typeId;

    @TableField(exist = false)
    private String typeName;

    public void setGoodsName(String s) {
    }

    public void setTypeName(String s) {
        this.typeName = s;
    }

    public String getTypeName() {
        return typeName;
    }
}
