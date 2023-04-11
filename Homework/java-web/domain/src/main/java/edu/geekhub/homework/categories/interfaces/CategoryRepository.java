package edu.geekhub.homework.categories.interfaces;

import edu.geekhub.homework.categories.Category;
import java.util.List;

public interface CategoryRepository {
    List<Category> getCategories();

    int addCategory(Category productCategory);

    Category getCategoryById(int id);

    void deleteCategoryById(int id);

    void updateCategoryById(Category category, int id);
}
