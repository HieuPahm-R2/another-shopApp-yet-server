package com.hustVN.otherShopYet.service;

import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.model.dtos.ProductDTO;
import com.hustVN.otherShopYet.model.dtos.ProductImageDTO;
import com.hustVN.otherShopYet.model.entity.Product;
import com.hustVN.otherShopYet.model.entity.ProductImage;
import com.hustVN.otherShopYet.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    Product addProduct(ProductDTO dto) throws DataNotFoundException;
    Product getProductById(long id) throws  Exception;
   Page<ProductResponse> getAllProducts(PageRequest pageRequest);
   Product updateProduct(long id, ProductDTO dto) throws  Exception;
   void deleteProduct(long id);
   boolean existsByName(String name);
   ProductImage createProductImage(Long id, ProductImageDTO dto) throws Exception;
}
