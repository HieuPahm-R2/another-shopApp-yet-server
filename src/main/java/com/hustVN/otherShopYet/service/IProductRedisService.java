package com.hustVN.otherShopYet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hustVN.otherShopYet.response.ProductResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductRedisService {
    List<ProductResponse> getAll(String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException;
    void saveAll(List<ProductResponse> productResponses,
                 String keyword,
                 Long categoryId,
                 PageRequest pageRequest) throws JsonProcessingException;
    //Clear cached data in Redis
    void clear();
}
