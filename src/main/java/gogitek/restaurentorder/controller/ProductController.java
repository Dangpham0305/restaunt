package gogitek.restaurentorder.controller;

import gogitek.restaurentorder.constaint.FormatPrice;
import gogitek.restaurentorder.entity.PreOrder;
import gogitek.restaurentorder.entity.Product;
import gogitek.restaurentorder.modelutil.FilterProduct;
import gogitek.restaurentorder.modelutil.SearchDTO;
import gogitek.restaurentorder.service.CartService;
import gogitek.restaurentorder.service.CategoryService;
import gogitek.restaurentorder.service.ProductService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final CartService cartService;
    private final FormatPrice formatPrice;

    public ProductController(ProductService productService, CategoryService categoryService,
                             CartService cartService, FormatPrice formatPrice) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.formatPrice = formatPrice;
    }

    @ModelAttribute
    public void addAttributeToHeader(Model model) {
        model.addAttribute("listCategory", categoryService.getListCategory());
        model.addAttribute("format", formatPrice);
    }

    @GetMapping("/order/{page}")
    public String getViewProduct(@PathVariable("page") long currentPage,
                                 Model model) {
        long totalPage = productService.getTotalPage();
        Integer sum = productService.getTotal();
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sum", sum);
//        model.addAttribute("listProduct", productService.getByPage(currentPage));
        return "chonban";
    }
    @GetMapping("/order/add")
    public String getProduct(Model model) {
        model.addAttribute("preOrder", cartService.addNewCart(new PreOrder()).getId());
        model.addAttribute("listProduct", productService.getByPage());
        return "chonmon";
    }
    @GetMapping("/listOrder")
    public String getListOrder(Model model){
        model.addAttribute("preorderList", cartService.getAllCartByUser());
        return "chonban";
    }

    @GetMapping("/product/{id}")
    public String getViewProductDetail(@PathVariable int id, Model model) {
        Integer idCategory = productService.getCategoryId(id);
        List<Product> list = productService.getListProductByCategoryId(idCategory);
        Collections.shuffle(list);
        if (list.size() > 8) list = list.subList(0, 7);
        model.addAttribute("listSimilar", list);
        model.addAttribute("productDetail", productService.getProductById(id));
        return "productdetail";
    }

    @PostMapping("/product/{id}")
    public String handleAddProductToCart(@PathVariable("id") int id,
                                         RedirectAttributes redirectAttributes,
                                         @RequestParam("quantity") String quantitystring) {
        int quantity = Float.valueOf(quantitystring).intValue();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) return "redirect:/login";
        Product product = productService.getProductById(id);
        boolean success = cartService.saveItemToCart(product, quantity);
        String msg;
        if (success) msg = "Thêm giỏ hàng thành công";
        else msg = "Thêm giỏ hàng thất bại";
        redirectAttributes.addFlashAttribute("msg", msg);
        return "redirect:/product/{id}";
    }

    @GetMapping("/category/{id}/fillByName")
    public String getViewSearchByName() {
        return "redirect:/category/{id}/fillByName/1";
    }

    @GetMapping("/category/{id}/fillByName/{page}")
    public String handleViewSearchByName(@PathVariable("page") long currentPage,
                                         @PathVariable("id") int id, Model model,
                                         @ModelAttribute SearchDTO searchDTO) {
        long totalPage = productService.getTotalPageByName(id, searchDTO.getName());
        List<Product> productList = productService.getListProductByHot();
        Collections.shuffle(productList);
        if (productList.size() < 4) model.addAttribute("bestSeller", productList);
        else model.addAttribute("bestSeller", productList.subList(0, 3));
        model.addAttribute("input", new SearchDTO());
        model.addAttribute("category", categoryService.getCategoryById(id).get());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);
        List<Product> dsProduct = productService.findProductByName(id, searchDTO.getName(), currentPage);
        model.addAttribute("filter", new FilterProduct());
        model.addAttribute("categoryId", id);
        model.addAttribute("sum", dsProduct.size());
        model.addAttribute("listProduct", dsProduct);
        model.addAttribute("currentFilter", searchDTO.getName());
        return "thucphamkhac";
    }
    @RequestMapping("/personalInfor")
    public String personalInfor(){return "personal-infor";}
    @RequestMapping("/orderHistory")
    public String orderHistory(){return "order-history";}
}
