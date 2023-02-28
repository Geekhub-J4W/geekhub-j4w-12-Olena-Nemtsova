package edu.geekhub.homework.service.interfaces;

import edu.geekhub.homework.domain.Category;
import java.util.List;

public interface CategoryService {
    Category getCategoryById(int categoryId);

    boolean addCategory(Category category);

    boolean deleteCategoryById(int id);

    boolean updateCategoryById(Category category, int id);

    List<Category> getCategories();

}
