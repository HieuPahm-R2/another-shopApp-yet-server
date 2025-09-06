package com.hustVN.otherShopYet.service.implement;

import com.hustVN.otherShopYet.model.dtos.CategoryDTO;
import com.hustVN.otherShopYet.model.entity.Category;
import com.hustVN.otherShopYet.repo.CategoryRepository;
import com.hustVN.otherShopYet.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCate = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCate);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Category not found"));
    }

    @Override
    public Category updateCategory(long id, CategoryDTO categoryDTO) {
        Category updCate = getCategoryById(id);
        updCate.setName(categoryDTO.getName());
        return categoryRepository.save(updCate);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}
