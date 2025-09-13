package com.hustVN.otherShopYet.service.implement;

import com.hustVN.otherShopYet.model.dtos.CategoryDTO;
import com.hustVN.otherShopYet.model.entity.Category;
import com.hustVN.otherShopYet.repo.CategoryRepository;
import com.hustVN.otherShopYet.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}
