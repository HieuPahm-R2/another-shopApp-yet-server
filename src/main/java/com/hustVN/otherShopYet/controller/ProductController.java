package com.hustVN.otherShopYet.controller;

import com.hustVN.otherShopYet.model.dtos.ProductImageDTO;
import com.hustVN.otherShopYet.model.entity.Product;
import com.hustVN.otherShopYet.model.entity.ProductImage;
import com.hustVN.otherShopYet.service.ICategoryService;
import com.hustVN.otherShopYet.service.IProductService;
import com.hustVN.otherShopYet.service.implement.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final IProductService productService;
    @GetMapping("")
    public ResponseEntity<String> getAllProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {
        return ResponseEntity.ok("get products done");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(
            @PathVariable("id") long id) {
        return ResponseEntity.ok("get product by id done");
    }

    @PostMapping("/create")
    public ResponseEntity<?> insertProduct(
            @Valid @RequestBody ProductDTO productDTO, BindingResult result,
            @RequestPart("file") MultipartFile file) {
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
    public ResponseEntity<?> uploadImage(@PathVariable long id,@ModelAttribute("files")  List<MultipartFile> files){
        try {
            Product existProduct = productService.getProductById(id);
            files = files == null ? new ArrayList<>() : files;
            List<ProductImage> productImages = new ArrayList<>();
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
        // handle null error

    }
    // handle file upload
    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // use uuid to generate unique file name
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") long id) {
        return ResponseEntity.ok("get product done");
    }
}
