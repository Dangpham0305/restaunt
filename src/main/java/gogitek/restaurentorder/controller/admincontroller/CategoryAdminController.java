package gogitek.restaurentorder.controller.admincontroller;

import gogitek.restaurentorder.entity.Category;
import gogitek.restaurentorder.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class CategoryAdminController {
    private static final String currentDirectory = System.getProperty("user.dir");
    private static final Path path = Paths.get(currentDirectory + Paths.get("/src/main/resources/static/image"));
    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/category")
    public String getViewCategoryAdmin(Model model) {
        model.addAttribute("categoryList", categoryService.getListCategory());
        return "/admin-page/category";
    }

    @GetMapping("/admin/category/add")
    public String getViewAddCategory(Model model) {
        model.addAttribute("category", new Category());
        return "/admin-page/add-category";
    }

    @PostMapping("/admin/category/add")
    public String handleAddCategory(@ModelAttribute @Valid Category category, @RequestParam MultipartFile photo, BindingResult result) {
        if (photo.isEmpty() || result.hasErrors()) return "redirect:/admin/category/add";
        if (!photo.isEmpty()) {
            try {
                Files.write(path.resolve(photo.getOriginalFilename()), photo.getBytes());
                category.setImage(photo.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        categoryService.addCategory(category);
        return "redirect:/admin/category";
    }

    @GetMapping("/admin/category/delete/{id}")
    public String handleDeleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/category";
    }

    @GetMapping("/admin/category/edit/{id}")
    public String getViewEditCategory(@PathVariable("id") Long id, Model model) {
        if (categoryService.getCategoryById(id).isPresent()) {
            model.addAttribute("category", categoryService.getCategoryById(id).get());
            return "/admin-page/add-category";
        }
        return "redirect:/admin/category";
    }

    @PostMapping("/admin/category/edit/{id}")
    public String handleEditCategory(@PathVariable("id") Long id, @ModelAttribute Category category, @RequestParam MultipartFile photo) {
        if (!photo.isEmpty()) {
            try {
                Files.write(path.resolve(photo.getOriginalFilename()), photo.getBytes());
                category.setImage(photo.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        categoryService.updateCategory(id, category);
        return "redirect:/admin/category";
    }

}
