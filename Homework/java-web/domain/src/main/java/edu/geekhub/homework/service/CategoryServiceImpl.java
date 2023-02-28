package edu.geekhub.homework.service;

import edu.geekhub.homework.domain.Category;
import edu.geekhub.homework.domain.CategoryValidator;
import edu.geekhub.homework.repository.interfaces.CategoryRepository;
import edu.geekhub.homework.service.interfaces.CategoryService;
import java.util.List;
import org.tinylog.Logger;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryValidator categoryValidator) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
    }

    @Override
    public Category getCategoryById(int categoryId) {
        return getCategories().stream()
            .filter(category -> category.id() == categoryId)
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean addCategory(Category category) {
        boolean successAdd = false;
        try {
            categoryValidator.validate(category);
            categoryRepository.addCategory(category);
            Logger.info("Category was added:\n" + category);
            successAdd = true;
        } catch (IllegalArgumentException exception) {
            Logger.warn("Category wasn't added: " + category + "\n" + exception.getMessage());
        }
        return successAdd;
    }

    @Override
    public boolean deleteCategoryById(int id) {
        Category productCategory = categoryRepository.getCategoryById(id);

        if (productCategory != null) {
            categoryRepository.deleteCategoryById(id);
            Logger.info("Category was deleted:\n" + productCategory);
            return true;
        } else {
            Logger.warn("Category wasn't deleted:\nNo found category with id: " + id);
        }
        return false;
    }

    @Override
    public boolean updateCategoryById(Category category, int id) {
        boolean successEditing = false;
        try {
            categoryValidator.validate(category);
            successEditing = categoryRepository.updateCategoryById(category, id);

            if (successEditing) {
                Logger.info("Category was edited:\n" + category);
            } else {
                Logger.warn("Category wasn't edited:\nNo found  category with id: " + id);
            }
        } catch (IllegalArgumentException exception) {
            Logger.warn("Category wasn't edited: " + category + "\n" + exception.getMessage());
        }
        return successEditing;
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.getCategories();
    }

}
