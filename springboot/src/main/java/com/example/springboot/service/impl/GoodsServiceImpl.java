package com.example.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Collect;
import com.example.springboot.entity.Goods;
import com.example.springboot.entity.Type;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.CollectMapper;
import com.example.springboot.mapper.GoodsMapper;
import com.example.springboot.mapper.TypeMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.IGoodsService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Override
    public void save(Goods goods) {
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            throw new ServiceException("401", "用户未登录，请先登录");
        }
        goods.setUserId(currentUser.getId());
        goodsMapper.insert(goods);
    }

    @Override
    public void update(Goods goods) {
        goodsMapper.updateById(goods);
    }

    @Override
    public void remove(Integer id) {
        goodsMapper.deleteById(id);
    }

    @Override
    public List<Goods> selectAll() {
        return goodsMapper.selectList(null);
    }

    @Override
    public IPage<Goods> selectPage(Integer pageNum, Integer pageSize, String name) {
        Page<Goods> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(name), Goods::getName, name);

        Page<Goods> goodsPage = goodsMapper.selectPage(page, queryWrapper);
        goodsPage.getRecords().stream().forEach(goods -> {
            Type type = typeMapper.selectById(goods.getTypeId());
            goods.setTypeName(Objects.nonNull(type) ? type.getName() : "未知类型");

            User user = userMapper.selectById(goods.getUserId());
            goods.setUserName(Objects.nonNull(user) ? user.getName() : "未知用户");
            
            processCoverUrl(goods);
        });
        return goodsPage;
    }

    @Override
    public IPage<Goods> selectPageType(Integer pageNum, Integer pageSize, String name, Integer typeId) {
        Page<Goods> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(name), Goods::getName, name);
        queryWrapper.eq(typeId != 0, Goods::getTypeId, typeId);

        Page<Goods> goodsPage = goodsMapper.selectPage(page, queryWrapper);
        goodsPage.getRecords().stream().forEach(goods -> {
            Type type = typeMapper.selectById(goods.getTypeId());
            goods.setTypeName(Objects.nonNull(type) ? type.getName() : "未知类型");

            User user = userMapper.selectById(goods.getUserId());
            goods.setUserName(Objects.nonNull(user) ? user.getName() : "未知用户");
            
            processCoverUrl(goods);
        });
        return goodsPage;
    }

    @Override
    public List<Goods> times() {
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<Goods>();
        queryWrapper.eq(Goods::getState, "上架").orderByDesc(Goods::getId);
        List<Goods> list = goodsMapper.selectList(queryWrapper).stream().limit(4).collect(Collectors.toList());
        list.forEach(this::processCoverUrl);
        return list;
    }

    @Override
    public List<Goods> sales() {
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<Goods>();
        queryWrapper.eq(Goods::getState, "上架").orderByDesc(Goods::getSales);
        List<Goods> list = goodsMapper.selectList(queryWrapper).stream().limit(4).collect(Collectors.toList());
        list.forEach(this::processCoverUrl);
        return list;
    }

    public Goods selectById(Integer id) {
        Goods goods = goodsMapper.selectById(id);

        User user = TokenUtils.getCurrentUser();
        if (Objects.nonNull(user)){
            LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Collect::getUserId, user.getId());
            queryWrapper.eq(Collect::getGoodsId,id);
            Collect one = collectMapper.selectOne(queryWrapper);
            if (Objects.isNull(one)){
                goods.setIsCollect(false);
            } else {
                goods.setIsCollect(true);
            }
        }
        return goods;
    }

    private void processCoverUrl(Goods goods) {
        String cover = goods.getCover();
        System.out.println("处理前 cover: " + cover);
        if (cover == null || cover.isEmpty()) {
            return;
        }
        // 先修复重复的路径（包括双斜杠情况）
        if (cover.contains("/file/download/file/download/") || cover.contains("/file/download//file/download/")) {
            cover = cover.replace("/file/download/file/download/", "/file/download/");
            cover = cover.replace("/file/download//file/download/", "/file/download/");
        }
        // 如果已经是完整URL，直接返回
        if (cover.startsWith("http://") || cover.startsWith("https://")) {
            goods.setCover(cover);
        } else {
            // 如果是相对路径，拼接完整 URL
            goods.setCover("http://localhost:9999" + (cover.startsWith("/") ? cover : "/" + cover));
        }
        System.out.println("处理后 cover: " + goods.getCover());
    }
}