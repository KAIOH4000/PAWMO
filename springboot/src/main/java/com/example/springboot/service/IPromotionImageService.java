package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.PromotionImage;

import java.util.List;

public interface IPromotionImageService extends IService<PromotionImage> {
    
    List<PromotionImage> selectAll();
    
    PromotionImage selectByCategory(String category);
    
    void update(PromotionImage promotionImage);
    
}
