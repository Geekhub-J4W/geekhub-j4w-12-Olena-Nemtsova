package edu.geekhub.homework.repository.interfaces;

import edu.geekhub.homework.domain.Category;
import java.util.List;

public interface CategoryRepository {
    List<Category> getCategories();

    void addCategory(Category productCategory);

    Category getCategoryById(int id);

    boolean deleteCategoryById(int id);

    boolean updateCategoryById(Category category, int id);
}
