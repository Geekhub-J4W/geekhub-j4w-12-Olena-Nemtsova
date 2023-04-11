package edu.geekhub.homework.categories.interfaces;

import edu.geekhub.homework.categories.Category;
import java.util.List;

public interface CategoryService {
    Category getCategoryById(int categoryId);

    Category addCategory(Category category);

    boolean deleteCategoryById(int id);

    Category updateCategoryById(Category category, int id);

    List<Category> getCategories();

}
