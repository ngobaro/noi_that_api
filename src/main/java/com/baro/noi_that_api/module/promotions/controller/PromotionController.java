package com.baro.noi_that_api.module.promotions.controller;

import com.baro.noi_that_api.common.dto.ApiResponse;
import com.baro.noi_that_api.common.security.SecurityUtils;
import com.baro.noi_that_api.exception.AppException;
import com.baro.noi_that_api.exception.ErrorCode;
import com.baro.noi_that_api.module.promotions.dto.request.CategoryPromotionRequest;
import com.baro.noi_that_api.module.promotions.dto.request.PromotionCreateRequest;
import com.baro.noi_that_api.module.promotions.dto.request.PromotionUpdateRequest;
import com.baro.noi_that_api.module.promotions.dto.response.CategoryPromotionResponse;
import com.baro.noi_that_api.module.promotions.dto.response.PromotionResponse;
import com.baro.noi_that_api.module.promotions.service.CategoryPromotionService;
import com.baro.noi_that_api.module.promotions.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;
    private final CategoryPromotionService categoryPromotionService;

    // ===================== PROMOTION =====================

    @GetMapping("/promotions/{id}")
    public ApiResponse<PromotionResponse> getById(@PathVariable Integer id) {
        return ApiResponse.<PromotionResponse>builder()
                .code(200)
                .result(promotionService.getById(id))
                .build();
    }

    @GetMapping("/promotions/code/{code}")
    public ApiResponse<PromotionResponse> getByCode(@PathVariable String code) {
        return ApiResponse.<PromotionResponse>builder()
                .code(200)
                .result(promotionService.getByCode(code))
                .build();
    }

    @GetMapping("/promotions/validate/{code}")
    public ApiResponse<PromotionResponse> validateCode(@PathVariable String code) {
        return ApiResponse.<PromotionResponse>builder()
                .code(200)
                .result(promotionService.validateCode(code))
                .build();
    }

    @GetMapping("/promotions/active")
    public ApiResponse<List<PromotionResponse>> getActive() {
        return ApiResponse.<List<PromotionResponse>>builder()
                .code(200)
                .result(promotionService.getActive())
                .build();
    }

    @GetMapping("/promotions")
    public ApiResponse<List<PromotionResponse>> getAll() {
        return ApiResponse.<List<PromotionResponse>>builder()
                .code(200)
                .result(promotionService.getAll())
                .build();
    }

    @PostMapping("/promotions")
    public ApiResponse<PromotionResponse> create(
            @Valid @RequestBody PromotionCreateRequest request) {
        return ApiResponse.<PromotionResponse>builder()
                .code(201)
                .message("Tạo khuyến mãi thành công")
                .result(promotionService.create(request))
                .build();
    }

    @PutMapping("/promotions/{id}")
    public ApiResponse<PromotionResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody PromotionUpdateRequest request) {

        if (!SecurityUtils.isStaff()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return ApiResponse.<PromotionResponse>builder()
                .code(200)
                .result(promotionService.update(id, request))
                .build();
    }

    @DeleteMapping("/promotions/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        promotionService.delete(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa khuyến mãi thành công")
                .build();
    }

    @PutMapping("/promotions/{id}/activate")
    public ApiResponse<Void> activate(@PathVariable Integer id) {
        promotionService.activate(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Kích hoạt thành công")
                .build();
    }

    @PutMapping("/promotions/{id}/deactivate")
    public ApiResponse<Void> deactivate(@PathVariable Integer id) {
        promotionService.deactivate(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Vô hiệu hóa thành công")
                .build();
    }

    // ===================== CATEGORY PROMOTION =====================

    @GetMapping("/category-promotions/category/{categoryId}")
    public ApiResponse<List<CategoryPromotionResponse>> getByCategoryId(
            @PathVariable Integer categoryId) {
        return ApiResponse.<List<CategoryPromotionResponse>>builder()
                .code(200)
                .result(categoryPromotionService.getByCategoryId(categoryId))
                .build();
    }

    @GetMapping("/category-promotions/promotion/{promotionId}")
    public ApiResponse<List<CategoryPromotionResponse>> getByPromotionId(
            @PathVariable Integer promotionId) {
        return ApiResponse.<List<CategoryPromotionResponse>>builder()
                .code(200)
                .result(categoryPromotionService.getByPromotionId(promotionId))
                .build();
    }

    @PostMapping("/category-promotions")
    public ApiResponse<CategoryPromotionResponse> createCategoryPromotion(
            @Valid @RequestBody CategoryPromotionRequest request) {
        return ApiResponse.<CategoryPromotionResponse>builder()
                .code(201)
                .message("Gán khuyến mãi cho category thành công")
                .result(categoryPromotionService.create(request))
                .build();
    }

    @DeleteMapping("/category-promotions/{id}")
    public ApiResponse<Void> deleteCategoryPromotion(@PathVariable Integer id) {
        categoryPromotionService.delete(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa gán khuyến mãi thành công")
                .build();
    }

    @GetMapping("/category-promotions")
    public ApiResponse<List<CategoryPromotionResponse>> getAllCategoryPromotions() {
        return ApiResponse.<List<CategoryPromotionResponse>>builder()
                .code(200)
                .result(categoryPromotionService.getAll())
                .build();
    }
}