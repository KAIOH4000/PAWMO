package com.example.springboot.controller;

import com.example.springboot.common.AuthAccess;
import com.example.springboot.common.Result;
import com.example.springboot.entity.AfterSales;
import com.example.springboot.service.IAfterSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/afterSales")
public class AfterSalesController {

    @Autowired
    private IAfterSalesService afterSalesService;

    @PostMapping("/add")
    public Result add(@RequestBody AfterSales afterSales) {
        try {
            afterSalesService.save(afterSales);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("500", e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result update(@RequestBody AfterSales afterSales) {
        afterSalesService.update(afterSales);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        afterSalesService.remove(id);
        return Result.success();
    }

    @AuthAccess
    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(afterSalesService.selectAll());
    }

    @GetMapping("/selectById")
    public Result selectById(@RequestParam Integer id) {
        return Result.success(afterSalesService.selectById(id));
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "") String type,
                             @RequestParam(defaultValue = "") String status,
                             @RequestParam(required = false) Integer userId,
                             @RequestParam Integer pageNum,
                             @RequestParam Integer pageSize) {
        return Result.success(afterSalesService.selectPage(pageNum, pageSize, type, status, userId));
    }

    @PostMapping("/audit")
    public Result audit(@RequestBody AfterSales afterSales) {
        try {
            afterSalesService.audit(afterSales);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("500", e.getMessage());
        }
    }

    @PutMapping("/refund")
    public Result refund(@RequestBody AfterSales afterSales) {
        try {
            afterSalesService.refund(afterSales.getId());
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("500", e.getMessage());
        }
    }
}
