package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Orders {

    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String orderNo;
    private Integer goodsId;
    private Double price;
    private Integer nums;
    private String userName;
    private String userPhone;
    private String userAddress;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(exist = false)
    private String time; // 保持兼容性
    private String state;
    private Integer userId;

    @TableField(exist = false)
    private Goods goods;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private java.util.List<Integer> orderIds;
}