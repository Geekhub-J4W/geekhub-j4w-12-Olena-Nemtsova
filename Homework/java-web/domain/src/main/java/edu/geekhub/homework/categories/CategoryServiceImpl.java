package edu.geekhub.homework.categories;

import edu.geekhub.homework.categories.interfaces.CategoryRepository;
import edu.geekhub.homework.categories.interfaces.CategoryService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
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
        return categoryRepository.getCategoryById(categoryId);
    }

    @Override
    public Category addCategory(Category category) {
        try {
            categoryValidator.validate(category);
            int newCategoryId = categoryRepository.addCategory(category);
            if (newCategoryId == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            Logger.info("Category was added:\n" + category);
            return getCategoryById(newCategoryId);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Category wasn't added: " + category + "\n" + exception.getMessage());
            return null;
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
            Logger.info("Category was deleted:\n" + categoryToDel);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Product wasn't deleted: " + categoryToDel + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public Category updateCategoryById(Category category, int id) {
        try {
            categoryValidator.validate(category);
            if (getCategoryById(id) == null) {
                throw new IllegalArgumentException("Category with id" + id + "not found");
            }
            categoryRepository.updateCategoryById(category, id);
            Logger.info("Category was updated:\n" + category);
            return getCategoryById(id);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Category wasn't edited: " + category + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.getCategories();
    }

}
