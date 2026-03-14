package com.example.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.springboot.entity.AfterSales;

import java.util.List;

public interface IAfterSalesService {

    void save(AfterSales afterSales);

    void update(AfterSales afterSales);

    void remove(Integer id);

    List<AfterSales> selectAll();

    AfterSales selectById(Integer id);

    IPage<AfterSales> selectPage(Integer pageNum, Integer pageSize, String type, String status, Integer userId);

    void audit(AfterSales afterSales);

    void refund(Integer id);
}
