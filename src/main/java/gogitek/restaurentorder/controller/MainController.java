package gogitek.restaurentorder.controller;

import gogitek.restaurentorder.constaint.FormatPrice;
import gogitek.restaurentorder.entity.Product;
import gogitek.restaurentorder.service.CartService;
import gogitek.restaurentorder.service.CategoryService;
import gogitek.restaurentorder.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final CartService cartService;
    private final FormatPrice formatPrice;

    public MainController(ProductService productService, CategoryService categoryService,
                          CartService cartService, FormatPrice formatPrice) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.formatPrice = formatPrice;
    }

    @ModelAttribute
    public void addAttributeToHeader(Model model) {
        model.addAttribute("listCategory", categoryService.getListCategory());
        List<Product> list = productService.findAllProduct();
        model.addAttribute("listProductDisplay", list.size() < 4 ? list : list.subList(0, 3));
        model.addAttribute("listProduct", list.size() < 8 ? list : list.subList(0, 7));
    }


    @GetMapping(value = {"/", "/index", "/home"})
    public String getViewHomepage() {
        return "index";
    }
}
