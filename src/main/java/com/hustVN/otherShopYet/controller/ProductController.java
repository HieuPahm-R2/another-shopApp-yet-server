package com.hustVN.otherShopYet.controller;

import com.github.javafaker.Faker;
import com.hustVN.otherShopYet.components.LocalizationUtils;
import com.hustVN.otherShopYet.model.dtos.ProductImageDTO;
import com.hustVN.otherShopYet.model.entity.Product;
import com.hustVN.otherShopYet.model.entity.ProductImage;
import com.hustVN.otherShopYet.response.ProductListResponse;
import com.hustVN.otherShopYet.response.ProductResponse;
import com.hustVN.otherShopYet.service.ICategoryService;
import com.hustVN.otherShopYet.service.IProductService;
import com.hustVN.otherShopYet.service.implement.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hustVN.otherShopYet.model.dtos.ProductDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam(defaultValue = "")String keyword,@RequestParam(defaultValue = "0", name = "category_id")Long categoryId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit) {
        PageRequest pageRequest = PageRequest.of(
                page, limit, Sort.by("id").ascending());
        List<ProductResponse> products = productService.getAllProducts(keyword,categoryId,pageRequest).getContent();
        int totalPages = productService.getAllProducts(keyword,categoryId,pageRequest).getTotalPages();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") Long id) {
        try {
            Product existProduct = productService.getProductById(id);
            return ResponseEntity.ok(ProductResponse.from(existProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id, @Valid @RequestBody ProductDTO productDTO) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> insertProduct(
            @Valid @RequestBody ProductDTO productDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> res = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(res);
            }
            Product savedProduct = productService.addProduct(productDTO);

            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@PathVariable long id, @RequestParam(value = "files", required = false) List<MultipartFile> files){
        try {
            Product existProduct = productService.getProductById(id);
            files = files == null ? new ArrayList<>() : files;
            List<ProductImage> productImages = new ArrayList<>();
            if(files.size() > 5){
                return ResponseEntity.badRequest().body("Up 5 cái thôi mày");
            }
            for (MultipartFile f : files) {
                if (f.getSize() == 0) {
                    continue;
                }
                // check file size not over 10MB
                if (f.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("file size must be less than 10MB");
                }
                String contentType = f.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest().body("file must be an image");
                }
                String fileName = storeFile(f);
                // save to db
               ProductImage pro = productService.createProductImage(
                        existProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(fileName)
                                .build());
                productImages.add(pro);
            }
            return ResponseEntity.ok(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    // handle file upload
    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // use uuid to generate unique
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // đường dẫn đầy đủ đến file
        Path filePath = Paths.get(uploadDir.toString(), uniqueFileName);
        // sao chép và lưu vào thư mục đích
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }
// handle view image
@GetMapping("/images/{imageName}")
public ResponseEntity<?> viewImage(@PathVariable String imageName) {
    try {
        Path imagePath = Paths.get("uploads/" + imageName);
        UrlResource resource = new UrlResource(imagePath.toUri());

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new UrlResource(Paths.get("uploads/nonomi.jpg").toUri()));
        }
    } catch (Exception e) {
        return ResponseEntity.notFound().build();
    }
}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") long id) {
        try {
             productService.deleteProduct(id);
            return ResponseEntity.ok("delete product by id done");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generate-fake-data")
    public ResponseEntity<?> generateFakeData() {
        Faker faker = new Faker();
        for(int i = 0; i < 50; i++){
            String name = faker.commerce().productName();
            if(productService.existsByName(name)){
                continue;
            }
            ProductDTO dto = ProductDTO.builder()
                    .name(name)
                    .price(faker.number().randomDouble(2, 10000, 1000000))
                    .description(faker.lorem().paragraph())
                    .thumbnail("")
                    .categoryId(faker.number().numberBetween(1, 6))
                    .build();
            try{
                productService.addProduct(dto);
            }catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Done everything");
    }
}
