package com.hustVN.otherShopYet.service;

import java.util.List;

import com.hustVN.otherShopYet.model.dtos.CategoryDTO;
import com.hustVN.otherShopYet.model.entity.Category;
import org.springframework.stereotype.Service;

@Service
public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);

    Category getCategoryById(long id);

    Category updateCategory(long id, CategoryDTO categoryDTO);

    List<Category> getAllCategories();

    void deleteCategory(long id);
}
