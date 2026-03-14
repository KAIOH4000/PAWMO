package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("promotion_image")
public class PromotionImage {

    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String category;
    
    private String imageUrl;
    
    @TableField(exist = false)
    private String categoryName;

}
