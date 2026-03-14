package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Carousel;
import com.example.springboot.entity.Type;
import com.example.springboot.mapper.CarouselMapper;
import com.example.springboot.mapper.TypeMapper;
import com.example.springboot.service.ICarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CarouselServiceImpl implements ICarouselService {
    @Autowired
    private CarouselMapper carouselMapper;

    @Autowired
    private TypeMapper typeMapper;

    @Override
    public void save(Carousel carousel) {
        carouselMapper.insert(carousel);
    }

    @Override
    public void update(Carousel carousel) {
        carouselMapper.updateById(carousel);
    }

    @Override
    public void remove(Integer id) {
        carouselMapper.deleteById(id);
    }

    @Override
    public List<Carousel> selectAll() {
        return carouselMapper.selectList(null);
    }

    @Override
    public Carousel selectById(Integer id) {
        return carouselMapper.selectById(id);
    }

    @Override
    public IPage<Carousel> selectPage(Integer pageNum, Integer pageSize, String name) {
        Page<Carousel> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Carousel> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Carousel::getName, name);
        }
        queryWrapper.orderByDesc(Carousel::getId);

        Page<Carousel> carouselPage = carouselMapper.selectPage(page, queryWrapper);
        
        // 填充分类名称
        List<Carousel> records = carouselPage.getRecords();
        for (Carousel carousel : records) {
            if (carousel.getTypeId() != null) {
                Type type = typeMapper.selectById(carousel.getTypeId());
                carousel.setTypeName(Objects.nonNull(type) ? type.getName() : "全部分类");
            } else {
                carousel.setTypeName("全部分类");
            }
        }
        
        return carouselPage;
    }
}