package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Type;
import com.example.springboot.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private ITypeService typeService;

    @RequestMapping("/add")
    public Result add(@RequestBody Type type){
    typeService.save(type);
    return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Type type){
        typeService.update(type);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam Integer id){
        typeService.remove(id);
        return Result.success();
    }

    @RequestMapping("/selectAll")
    public Result selectAll(){
        return Result.success( typeService.selectAll());
    }

    @RequestMapping("/selectById")
    public Result selectById(@RequestParam Integer id) {
        return Result.success(typeService.selectById(id));
    }

    @RequestMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "") String name,
                             @RequestParam Integer pageNum,
                             @RequestParam Integer pageSize) {
        return Result.success(typeService.selectPage(pageNum, pageSize, name));
    }
}
