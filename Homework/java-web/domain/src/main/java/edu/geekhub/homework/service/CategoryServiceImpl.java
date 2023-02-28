package edu.geekhub.homework.service;

import edu.geekhub.homework.domain.Category;
import edu.geekhub.homework.domain.CategoryValidator;
import edu.geekhub.homework.repository.interfaces.CategoryRepository;
import edu.geekhub.homework.service.interfaces.CategoryService;
import java.util.List;
import org.springframework.dao.DataAccessException;
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
        try {
            categoryValidator.validate(category);
            int newCategoryId = categoryRepository.addCategory(category);
            if (newCategoryId == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            Logger.info("Category was added:\n" + category);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Category wasn't added: " + category + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCategoryById(int id) {
        Category categoryToDel = getCategoryById(id);
        try {
            if (categoryToDel == null) {
                throw new IllegalArgumentException("Category with id" + id + "not found");
            }
            categoryRepository.deleteCategoryById(id);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Product wasn't deleted: " + categoryToDel + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateCategoryById(Category category, int id) {
        try {
            categoryValidator.validate(category);
            if (getCategoryById(id) == null) {
                throw new IllegalArgumentException("Category with id" + id + "not found");
            }
            categoryRepository.updateCategoryById(category, id);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Category wasn't edited: " + category + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.getCategories();
    }

}
