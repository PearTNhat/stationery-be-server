package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.Error.AuthErrorCode;
import com.project.stationery_be_server.Error.NotExistedErrorCode;
import com.project.stationery_be_server.dto.request.CategoryRequest;
import com.project.stationery_be_server.entity.Category;
import com.project.stationery_be_server.exception.AppException;
import com.project.stationery_be_server.repository.CategoryRepository;
import com.project.stationery_be_server.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category createCategory(CategoryRequest request) {
        if (categoryRepository.existsByCategoryName(request.getCategory_name())) {
            throw new AppException(NotExistedErrorCode.CATEGORY_NAME_EXITS);
        }

        Category category = new Category();
        category.setCategoryName(request.getCategory_name());
        category.setIcon(request.getIcon());
        category.setBgColor(request.getBg_color());

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(String categoryId, CategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(NotExistedErrorCode.CATEGORY_NOT_FOUND));

        // Check if new category name is unique (excluding current category)
        if (!category.getCategoryName().equals(request.getCategory_name()) &&
                categoryRepository.existsByCategoryName(request.getCategory_name())) {
            throw new AppException(NotExistedErrorCode.CATEGORY_NAME_EXITS);
        }

        category.setCategoryName(request.getCategory_name());
        category.setIcon(request.getIcon());
        category.setBgColor(request.getBg_color());

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(NotExistedErrorCode.CATEGORY_NOT_FOUND));

        // Check if category has associated products before deletion
        if (!category.getProducts().isEmpty()) {
            throw new AppException(AuthErrorCode.DELETE_CATEGORY_FAIL);
        }

        categoryRepository.delete(category);
    }
}