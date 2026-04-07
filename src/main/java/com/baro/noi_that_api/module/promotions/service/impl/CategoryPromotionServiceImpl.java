package com.baro.noi_that_api.module.promotions.service.impl;

import com.baro.noi_that_api.exception.AppException;
import com.baro.noi_that_api.exception.ErrorCode;
import com.baro.noi_that_api.module.promotions.dto.request.CategoryPromotionRequest;
import com.baro.noi_that_api.module.promotions.dto.response.CategoryPromotionResponse;
import com.baro.noi_that_api.module.promotions.entity.CategoryPromotion;
import com.baro.noi_that_api.module.promotions.mapper.CategoryPromotionMapper;
import com.baro.noi_that_api.module.promotions.repository.CategoryPromotionRepository;
import com.baro.noi_that_api.module.promotions.service.CategoryPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryPromotionServiceImpl implements CategoryPromotionService {

    private final CategoryPromotionRepository categoryPromotionRepository;
    private final CategoryPromotionMapper categoryPromotionMapper;

    @Override
    public List<CategoryPromotionResponse> getByCategoryId(Integer categoryId) {
        return categoryPromotionRepository.findByCategoryId(categoryId)
                .stream()
                .map(categoryPromotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryPromotionResponse> getByPromotionId(Integer promotionId) {
        return categoryPromotionRepository.findByPromotionId(promotionId)
                .stream()
                .map(categoryPromotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryPromotionResponse create(CategoryPromotionRequest request) {
        if (categoryPromotionRepository.existsByCategoryIdAndPromotionId(
                request.getCategoryId(), request.getPromotionId())) {
            throw new AppException(ErrorCode.CATEGORY_PROMOTION_EXISTED);
        }

        CategoryPromotion entity = categoryPromotionMapper.toEntity(request);
        return categoryPromotionMapper.toResponse(categoryPromotionRepository.save(entity));
    }

    @Override
    public void delete(Integer id) {
        CategoryPromotion entity = categoryPromotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_PROMOTION_NOT_FOUND));
        categoryPromotionRepository.delete(entity);
    }

    @Override
    public List<CategoryPromotionResponse> getAll() {
        return categoryPromotionRepository.findAll()
                .stream()
                .map(categoryPromotionMapper::toResponse)
                .collect(Collectors.toList());
    }
}