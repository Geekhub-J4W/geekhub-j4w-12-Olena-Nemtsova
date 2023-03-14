package edu.geekhub.homework;

import edu.geekhub.homework.domain.Category;
import edu.geekhub.homework.service.interfaces.CategoryService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Collection<Category> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable int id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/newCategory")
    public Category addCategory(@RequestBody Category category) {
        Category newCategory = categoryService.addCategory(category);
        if (newCategory == null) {
            throw new IllegalArgumentException("Category wasn't added");
        }
        return newCategory;
    }

    @DeleteMapping(value = "/deleteCategory/{id}")
    public void deleteCategory(@PathVariable(value = "id") int id) {

        if (!categoryService.deleteCategoryById(id)) {
            throw new IllegalArgumentException("Category wasn't deleted");
        }
    }

    @PostMapping("/editCategory/{id}")
    public Category editCategory(@PathVariable(value = "id") int id,
                                 @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategoryById(category, id);
        if (updatedCategory == null) {
            throw new IllegalArgumentException("Category wasn't updated");
        }
        return updatedCategory;
    }
}
