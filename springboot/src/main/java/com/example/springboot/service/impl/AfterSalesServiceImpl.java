package com.example.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.AfterSales;
import com.example.springboot.entity.Goods;
import com.example.springboot.entity.Orders;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.AfterSalesMapper;
import com.example.springboot.mapper.GoodsMapper;
import com.example.springboot.mapper.OrdersMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IAfterSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AfterSalesServiceImpl implements IAfterSalesService {

    @Autowired
    private AfterSalesMapper afterSalesMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public void save(AfterSales afterSales) {
        afterSales.setApplyTime(DateUtil.now());
        afterSales.setStatus("待审核");
        afterSalesMapper.insert(afterSales);
    }

    @Override
    public void update(AfterSales afterSales) {
        afterSalesMapper.updateById(afterSales);
    }

    @Override
    public void remove(Integer id) {
        afterSalesMapper.deleteById(id);
    }

    @Override
    public List<AfterSales> selectAll() {
        List<AfterSales> list = afterSalesMapper.selectList(null);
        list.forEach(item -> {
            try {
                Orders order = ordersMapper.selectById(item.getOrderId());
                if (order != null) {
                    if (order.getGoodsId() != null) {
                        Goods goods = goodsMapper.selectById(order.getGoodsId());
                        order.setGoods(goods);
                    }
                    item.setOrder(order);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                item.setUser(userMapper.selectById(item.getUserId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return list;
    }

    @Override
    public AfterSales selectById(Integer id) {
        AfterSales afterSales = afterSalesMapper.selectById(id);
        if (afterSales != null) {
            try {
                Orders order = ordersMapper.selectById(afterSales.getOrderId());
                if (order != null) {
                    if (order.getGoodsId() != null) {
                        Goods goods = goodsMapper.selectById(order.getGoodsId());
                        order.setGoods(goods);
                    }
                    afterSales.setOrder(order);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                afterSales.setUser(userMapper.selectById(afterSales.getUserId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return afterSales;
    }

    @Override
    public IPage<AfterSales> selectPage(Integer pageNum, Integer pageSize, String type, String status, Integer userId) {
        Page<AfterSales> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<AfterSales> queryWrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            queryWrapper.like(AfterSales::getType, type);
        }
        if (status != null && !status.isEmpty()) {
            queryWrapper.like(AfterSales::getStatus, status);
        }
        if (userId != null) {
            queryWrapper.eq(AfterSales::getUserId, userId);
        }
        Page<AfterSales> resultPage = afterSalesMapper.selectPage(page, queryWrapper);
        resultPage.getRecords().forEach(item -> {
            try {
                Orders order = ordersMapper.selectById(item.getOrderId());
                if (order != null) {
                    if (order.getGoodsId() != null) {
                        Goods goods = goodsMapper.selectById(order.getGoodsId());
                        order.setGoods(goods);
                    }
                    item.setOrder(order);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                item.setUser(userMapper.selectById(item.getUserId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return resultPage;
    }

    @Override
    public void audit(AfterSales afterSales) {
        afterSales.setAuditTime(DateUtil.now());
        afterSalesMapper.updateById(afterSales);

        // 注意：退款不再在审核时自动执行，需要管理员手动点击退款按钮
        // 这样可以区分仅退款和退货退款的不同流程
    }

    @Override
    public void refund(Integer id) {
        // 查询售后记录
        AfterSales afterSales = afterSalesMapper.selectById(id);
        if (afterSales == null) {
            throw new RuntimeException("售后记录不存在");
        }

        // 查询订单
        Orders order = ordersMapper.selectById(afterSales.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 查询用户
        User user = userMapper.selectById(order.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 退回用户余额
        if (afterSales.getRefundAmount() != null && afterSales.getRefundAmount() > 0) {
            Double currentAccount = user.getAccount();
            if (currentAccount == null) {
                currentAccount = 0.0;
            }
            user.setAccount(currentAccount + afterSales.getRefundAmount());
            userMapper.updateById(user);
        }

        // 更新售后记录状态为"已退款"
        afterSales.setStatus("已退款");
        afterSalesMapper.updateById(afterSales);

        // 如果是退货退款或退货类型，更新订单状态为"已退款"
        if ("退货退款".equals(afterSales.getType()) || "退货".equals(afterSales.getType())) {
            order.setState("已退款");
            ordersMapper.updateById(order);
        }
    }
}
