package gogitek.restaurentorder.service;

import gogitek.restaurentorder.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getListCategory();
    Optional<Category> getCategoryById(Long id);
    boolean addCategory(Category category);
    boolean deleteCategory(Long id);
    void updateCategory(Long id, Category category);
}
