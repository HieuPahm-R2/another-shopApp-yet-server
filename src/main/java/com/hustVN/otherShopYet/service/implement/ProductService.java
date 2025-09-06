package com.hustVN.otherShopYet.service.implement;

import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.exception.InvalidParamException;
import com.hustVN.otherShopYet.model.dtos.ProductDTO;
import com.hustVN.otherShopYet.model.dtos.ProductImageDTO;
import com.hustVN.otherShopYet.model.entity.Category;
import com.hustVN.otherShopYet.model.entity.Product;
import com.hustVN.otherShopYet.model.entity.ProductImage;
import com.hustVN.otherShopYet.repo.CategoryRepository;
import com.hustVN.otherShopYet.repo.ProductImageRepository;
import com.hustVN.otherShopYet.repo.ProductRepository;
import com.hustVN.otherShopYet.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product addProduct(ProductDTO dto) throws DataNotFoundException {
        Category cate = categoryRepository.findById((dto.getCategoryId())).orElseThrow(
                () -> new DataNotFoundException("Category not found")
        );
        Product newProduct = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .thumbnail(dto.getThumbnail())
                .description(dto.getDescription())
                .category(cate)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long id) throws  Exception {
        return productRepository.findById(id).orElseThrow(
                () -> new  DataNotFoundException("Not Found")
        );
    }

    @Override
    public Page<Product> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    @Override
    public Product updateProduct(long id, ProductDTO dto) throws Exception {
        Product product =getProductById(id);
        if(product != null) {
            Category cate = categoryRepository.findById((dto.getCategoryId())).orElseThrow(
                    () -> new DataNotFoundException("Category not found")
            );
            product.setCategory(cate);
            product.setName(dto.getName());
            product.setPrice(dto.getPrice());
            product.setThumbnail(dto.getThumbnail());
            product.setDescription(dto.getDescription());
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long id,ProductImageDTO dto) throws Exception {
       Product existProduct = productRepository.findById(id).orElseThrow(
                () -> new  DataNotFoundException("Not Found")
        );
       ProductImage proImage = ProductImage.builder()
               .product(existProduct)
               .imageUrl(dto.getImageUrl())
               .build();
    // size not over five pics
        int size = productImageRepository.findByProductId(existProduct.getId()).size();
        if(size > 5) {
            throw new InvalidParamException("Not over 5 pics for uploading");
        }
        return productImageRepository.save(proImage);
    }
}
