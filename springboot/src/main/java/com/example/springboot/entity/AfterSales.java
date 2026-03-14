package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("after_sales")
public class AfterSales {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    private Integer userId;
    private String type;
    private String reason;
    private String status;
    private String applyTime;
    private String auditTime;
    private String auditRemark;
    private Double refundAmount;
    private String logisticsNo;
    private String logisticsCompany;

    @TableField(exist = false)
    private Orders order;

    @TableField(exist = false)
    private User user;
}
