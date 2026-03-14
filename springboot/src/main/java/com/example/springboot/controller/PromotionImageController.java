package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.PromotionImage;
import com.example.springboot.service.IPromotionImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotion")
public class PromotionImageController {

    @Autowired
    private IPromotionImageService promotionImageService;

    /**
     * 查询全部宣传图
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<PromotionImage> list = promotionImageService.selectAll();
        // 处理图片URL
        list.forEach(this::processImageUrl);
        return Result.success(list);
    }

    /**
     * 根据分类查询宣传图
     */
    @GetMapping("/selectByCategory")
    public Result selectByCategory(@RequestParam String category) {
        PromotionImage promotionImage = promotionImageService.selectByCategory(category);
        if (promotionImage != null) {
            processImageUrl(promotionImage);
        }
        return Result.success(promotionImage);
    }

    /**
     * 更新宣传图
     */
    @PutMapping("/update")
    public Result update(@RequestBody PromotionImage promotionImage) {
        promotionImageService.update(promotionImage);
        return Result.success();
    }

    /**
     * 处理图片URL
     */
    private void processImageUrl(PromotionImage promotionImage) {
        String imageUrl = promotionImage.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                promotionImage.setImageUrl("http://localhost:9999" + (imageUrl.startsWith("/") ? imageUrl : "/" + imageUrl));
            }
        }
    }

}
