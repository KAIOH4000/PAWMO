package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.PromotionImage;
import com.example.springboot.mapper.PromotionImageMapper;
import com.example.springboot.service.IPromotionImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionImageServiceImpl extends ServiceImpl<PromotionImageMapper, PromotionImage> implements IPromotionImageService {

    @Override
    public List<PromotionImage> selectAll() {
        return list();
    }

    @Override
    public PromotionImage selectByCategory(String category) {
        LambdaQueryWrapper<PromotionImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PromotionImage::getCategory, category);
        return getOne(wrapper);
    }

    @Override
    public void update(PromotionImage promotionImage) {
        updateById(promotionImage);
    }

}
