package edu.geekhub.homework.controllers;

import edu.geekhub.homework.CategoryConsoleParser;
import edu.geekhub.homework.domain.Category;
import edu.geekhub.homework.service.interfaces.CategoryService;
import java.util.List;

public class CategoriesController {
    private final CategoryService categoryService;
    private final CategoryConsoleParser categoryConsoleParser;

    public CategoriesController(CategoryService categoryService,
                                CategoryConsoleParser categoryConsoleParser) {
        this.categoryService = categoryService;
        this.categoryConsoleParser = categoryConsoleParser;
    }

    public boolean addCategory(String line) {
        Category category = categoryConsoleParser.parse(line);
        return categoryService.addCategory(category);
    }

    public boolean deleteCategory(int id) {
        return categoryService.deleteCategoryById(id);
    }

    public boolean updateCategoryById(Category category, int id) {
        return categoryService.updateCategoryById(category, id);
    }

    public List<Category> getCategories() {
        return categoryService.getCategories();
    }
}
