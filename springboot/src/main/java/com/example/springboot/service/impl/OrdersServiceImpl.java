package com.example.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Goods;
import com.example.springboot.entity.Orders;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.GoodsMapper;
import com.example.springboot.mapper.OrdersMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IOrdersService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class OrdersServiceImpl implements IOrdersService {
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy.M.d.HH.mm");

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void save(Orders orders) {
        // 1、判断商品库存是否充足，如果不充足，给提示
        Goods goods = goodsMapper.selectById(orders.getGoodsId());
        // 2、如果商品库存充足，就下单（数据库新增一条订单）
        if (goods.getStore() < orders.getNums()){
            throw new ServiceException("201", goods.getName() + "商品库存不足");
        }

// 3、新增订单
        // (1) 订单编号保持原格式
        SimpleDateFormat orderNoFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        orders.setOrderNo(orderNoFormat.format(new Date()));
        
        // (2) 设置数据库时间和前端显示时间
        LocalDateTime now = LocalDateTime.now();
        orders.setCreateTime(now);
        orders.setTime(now.format(TIME_FORMATTER));
        
        // (3) 验证日志
        org.slf4j.LoggerFactory.getLogger(OrdersServiceImpl.class)
            .info("时间验证 | 订单: {} | 显示时间: {}", 
                orders.getOrderNo(),
                orders.getTime());
        ordersMapper.insert(orders);

        // 4、商品库存减去对应的数量
        goods.setStore(goods.getStore() - orders.getNums());
        // 5、销量累加对应的数量（处理null情况）
        Integer currentSales = goods.getSales();
        if (currentSales == null) {
            currentSales = 0;
        }
        goods.setSales(currentSales + orders.getNums());
        // 6、更新一下商品
        goodsMapper.updateById(goods);
    }

    @Override
    public void update(Orders orders) {
        ordersMapper.updateById(orders);
    }

    @Override
    public void remove(Integer id) {
        ordersMapper.deleteById(id);
    }

    @Override
    public List<Orders> selectAll() {
        List<Orders> list = ordersMapper.selectList(null);
        list.forEach(orders -> {
            // 统一格式化时间为yyyy.M.d.HH.mm
            if (orders.getCreateTime() != null) {
                orders.setTime(orders.getCreateTime().format(TIME_FORMATTER));
            }
            try {
                if (orders.getGoodsId() != null) {
                    orders.setGoods(goodsMapper.selectById(orders.getGoodsId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (orders.getUserId() != null) {
                    orders.setUser(userMapper.selectById(orders.getUserId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return list;
    }

    @Override
    public Orders selectById(Integer id) {
        Orders orders = ordersMapper.selectById(id);
        if (orders != null) {
            // 统一格式化时间为yyyy.M.d.HH.mm
            if (orders.getCreateTime() != null) {
                orders.setTime(orders.getCreateTime().format(TIME_FORMATTER));
            }
            try {
                if (orders.getGoodsId() != null) {
                    orders.setGoods(goodsMapper.selectById(orders.getGoodsId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (orders.getUserId() != null) {
                    orders.setUser(userMapper.selectById(orders.getUserId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return orders;
    }

    @Override
    public IPage<Orders> selectPage(Integer pageNum, Integer pageSize, String name, String orderNo, String state) {
        Page<Orders> page = new Page<>(pageNum, pageSize);

        // select * from orders where name like %name% or order_no = xx
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Orders::getName,name);
        queryWrapper.like(Orders::getOrderNo,orderNo);
        // 添加状态筛选
        if (state != null && !state.isEmpty()) {
            queryWrapper.eq(Orders::getState, state);
        }

        User currentUser = TokenUtils.getCurrentUser();
        // 普通用户只能查看自己的订单
        if (currentUser != null && currentUser.getRole() != null && currentUser.getRole().equals("USER")){
            queryWrapper.eq(Orders::getUserId, currentUser.getId());
        }
        
        Page<Orders> ordersPage = ordersMapper.selectPage(page, queryWrapper);
        ordersPage.getRecords().forEach(orders -> {
            // 统一格式化时间为yyyy.M.d.HH.mm
            if (orders.getCreateTime() != null) {
                orders.setTime(orders.getCreateTime().format(TIME_FORMATTER));
            }
            try {
                if (orders.getGoodsId() != null) {
                    orders.setGoods(goodsMapper.selectById(orders.getGoodsId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (orders.getUserId() != null) {
                    orders.setUser(userMapper.selectById(orders.getUserId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ordersPage;
    }

    @Override
    public void pay(Orders orders) {
        // 1、从 Token 获取当前登录用户
        User user = TokenUtils.getCurrentUser();
        if (user == null) {
            throw new ServiceException("401", "用户未登录，请重新登录");
        }
        
        System.out.println("=== 支付请求开始 ===");
        System.out.println("接收到的订单 ID: " + orders.getId());
        System.out.println("接收到的 orderIds: " + orders.getOrderIds());
        
        Double account = user.getAccount();
        if (account == null) {
            account = 0.0;
        }
        
        // 2、判断是批量支付还是单订单支付
        java.util.List<Integer> orderIds = orders.getOrderIds();
        if (orderIds != null && !orderIds.isEmpty()) {
            // 批量支付
            System.out.println("执行批量支付，订单数量：" + orderIds.size());
            batchPay(user, account, orderIds);
        } else {
            // 单订单支付
            System.out.println("执行单订单支付，订单 ID: " + orders.getId());
            singlePay(user, account, orders.getId());
        }
    }

    /**
     * 根据订单号更新订单状态
     */
    @Override
    public boolean updateOrderStatusByOrderNo(String orderNo, String state) {
        try {
            // 根据订单号查询订单
            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Orders::getOrderNo, orderNo);
            Orders order = ordersMapper.selectOne(queryWrapper);
            
            if (order != null) {
                order.setState(state);
                ordersMapper.updateById(order);
                org.slf4j.LoggerFactory.getLogger(OrdersServiceImpl.class)
                    .info("订单状态已更新 | 订单号：{} | 新状态：{}", orderNo, state);
                return true;
            } else {
                org.slf4j.LoggerFactory.getLogger(OrdersServiceImpl.class)
                    .warn("订单不存在 | 订单号：{}", orderNo);
                return false;
            }
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(OrdersServiceImpl.class)
                .error("更新订单状态失败 | 订单号：{}", orderNo, e);
            return false;
        }
    }

    /**
     * 确认到货
     */
    @Override
    public void confirmArrival(Integer id) {
        if (id == null) {
            throw new ServiceException("400", "订单ID不能为空");
        }
        Orders order = ordersMapper.selectById(id);
        if (order == null) {
            throw new ServiceException("404", "订单不存在");
        }
        if (!"已发货".equals(order.getState())) {
            throw new ServiceException("400", "订单状态必须为已发货才能确认到货");
        }
        order.setState("已完成");
        ordersMapper.updateById(order);
    }

    /**
     * 批量支付
     */
    private void batchPay(User user, Double account, java.util.List<Integer> orderIds) {
        double totalPrice = 0.0;
        java.util.List<Orders> ordersList = new java.util.ArrayList<>();
        
        // 计算总价并校验订单
        for (Integer orderId : orderIds) {
            Orders order = ordersMapper.selectById(orderId);
            if (order == null) {
                throw new ServiceException("404", "订单不存在：" + orderId);
            }
            
            System.out.println("=== 订单权限校验 ===");
            System.out.println("订单 ID: " + orderId);
            System.out.println("订单的用户 ID: " + order.getUserId());
            System.out.println("当前登录用户 ID: " + user.getId());
            System.out.println("是否匹配：" + java.util.Objects.equals(order.getUserId(), user.getId()));
            
            // 验证订单是否属于当前用户
            if (!java.util.Objects.equals(order.getUserId(), user.getId())) {
                throw new ServiceException("403", "无权支付订单：" + orderId);
            }
            
            if (order.getPrice() != null) {
                totalPrice += order.getPrice();
            }
            ordersList.add(order);
        }
        
        // 检查余额是否充足
        if (account < totalPrice) {
            throw new ServiceException("201", "余额不足，请充值~");
        }
        
        // 更新所有订单状态
        for (Orders order : ordersList) {
            order.setState("未发货");
            ordersMapper.updateById(order);
        }
        
        // 扣除用户余额
        user.setAccount(account - totalPrice);
        userMapper.updateById(user);
    }
    
    /**
     * 单订单支付
     */
    private void singlePay(User user, Double account, Integer orderId) {
        // 查询订单信息
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            throw new ServiceException("404", "订单不存在");
        }
        
        Double price = order.getPrice();
        if (price == null) {
            price = 0.0;
        }
        
        // 如果余额充足，改变订单状态并且减去对应的值
        if (account >= price){
            order.setState("未发货");
            ordersMapper.updateById(order);

            // 更新用户余额
            user.setAccount(account - price);
            userMapper.updateById(user);
        } else{
            // 如果余额不足，就给前台提示
            throw new ServiceException("201", "余额不足，请充值~");
        }
    }
}