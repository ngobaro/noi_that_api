package com.baro.noi_that_api.module.promotions.service.impl;

import com.baro.noi_that_api.exception.AppException;
import com.baro.noi_that_api.exception.ErrorCode;
import com.baro.noi_that_api.module.promotions.dto.request.PromotionCreateRequest;
import com.baro.noi_that_api.module.promotions.dto.request.PromotionUpdateRequest;
import com.baro.noi_that_api.module.promotions.dto.response.PromotionResponse;
import com.baro.noi_that_api.module.promotions.entity.Promotion;
import com.baro.noi_that_api.module.promotions.mapper.PromotionMapper;
import com.baro.noi_that_api.module.promotions.repository.PromotionRepository;
import com.baro.noi_that_api.module.promotions.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Override
    public PromotionResponse getById(Integer id) {
        return promotionMapper.toResponse(findById(id));
    }

    @Override
    public PromotionResponse getByCode(String code) {
        Promotion promotion = promotionRepository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));
        return promotionMapper.toResponse(promotion);
    }

    @Override
    public PromotionResponse validateCode(String code) {
        Promotion promotion = promotionRepository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

        if (!promotion.getIsActive()) {
            throw new AppException(ErrorCode.PROMOTION_INACTIVE);
        }

        LocalDate today = LocalDate.now();
        if (today.isBefore(promotion.getStartDay()) || today.isAfter(promotion.getEndDay())) {
            throw new AppException(ErrorCode.PROMOTION_EXPIRED);
        }

        return promotionMapper.toResponse(promotion);
    }

    @Override
    public List<PromotionResponse> getActive() {
        return promotionRepository.findActiveAndValid(LocalDate.now())
                .stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PromotionResponse> getAll() {
        return promotionRepository.findAll()
                .stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PromotionResponse create(PromotionCreateRequest request) {
        if (promotionRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.PROMOTION_CODE_EXISTED);
        }

        Promotion promotion = promotionMapper.toEntity(request);
        promotion.setIsActive(true);

        return promotionMapper.toResponse(promotionRepository.save(promotion));
    }

    @Override
    public PromotionResponse update(Integer id, PromotionUpdateRequest request) {
        Promotion promotion = findById(id);
        promotionMapper.updateEntity(request, promotion);
        return promotionMapper.toResponse(promotionRepository.save(promotion));
    }

    @Override
    public void delete(Integer id) {
        promotionRepository.delete(findById(id));
    }

    @Override
    public void activate(Integer id) {
        Promotion promotion = findById(id);
        promotion.setIsActive(true);
        promotionRepository.save(promotion);
    }

    @Override
    public void deactivate(Integer id) {
        Promotion promotion = findById(id);
        promotion.setIsActive(false);
        promotionRepository.save(promotion);
    }

    // ==================== PRIVATE ====================

    private Promotion findById(Integer id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));
    }
}