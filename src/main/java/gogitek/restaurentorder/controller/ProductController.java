package gogitek.restaurentorder.controller;

import gogitek.restaurentorder.constaint.FormatPrice;
import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.constaint.UrlUtils;
import gogitek.restaurentorder.entity.PreOrder;
import gogitek.restaurentorder.service.CartService;
import gogitek.restaurentorder.service.CategoryService;
import gogitek.restaurentorder.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/staff")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final CartService cartService;
    private final FormatPrice formatPrice;

    private final UrlUtils urlUtils;


    @ModelAttribute
    public void addAttributeToHeader(Model model) {
        model.addAttribute("listCategory", categoryService.getListCategory());
        model.addAttribute("format", formatPrice);
    }

//    @GetMapping("/order/{page}")
//    public String getViewProduct(@PathVariable("page") long currentPage,
//                                 Model model) {
//        long totalPage = productService.getTotalPage();
//        Integer sum = productService.getTotal();
//        model.addAttribute("totalPage", totalPage);
//        model.addAttribute("currentPage", currentPage);
//        model.addAttribute("sum", sum);
////        model.addAttribute("listProduct", productService.getByPage(currentPage));
//        return "chonban";
//    }
    @GetMapping(value = {"/order/{id}", "/order"})
    public String getProduct(Model model, @PathVariable Optional<Long> id) {
        PreOrder preOrder;
        if (id.isPresent()){
            preOrder = cartService.findById(id.get());
            model.addAttribute("orderedList", productService.getProductFromCart(preOrder, Arrays.asList(Status.PROCESSING, Status.DELIVERED, Status.DONE)));
            model.addAttribute("addedList", productService.getProductFromCart(preOrder, Collections.singletonList(Status.APPROVED)));
        }
        else {
             preOrder = cartService.addNewCart(new PreOrder());
        }
        model.addAttribute("preOrder", preOrder);
        model.addAttribute("listProduct", productService.getByPage());
        return "chonmon";
    }
    @PostMapping("/order")
    public String deleteOrder(@RequestParam Long id, HttpServletRequest request, RedirectAttributes redirectAttributes){
        PreOrder preOrder = cartService.findById(id);
        List<Status> acceptedStatus = Arrays.asList(Status.PROCESSING, Status.DONE, Status.DELIVERED);
        if (cartService.checkOrderDelivered(preOrder, acceptedStatus)){
            redirectAttributes.addFlashAttribute("msg", "Không thể xoá đơn!");
            return urlUtils.getPreviousPageByRequest(request).orElse("/");
        }
        cartService.deleteOrder(id);
        redirectAttributes.addFlashAttribute("msg", "Xoá đơn thành công!");
        return "redirect:/staff/list-order";
    }

    @PostMapping("/order/{preOrder}/addProduct")
    public String addProductToOrder(@PathVariable Long preOrder, @RequestParam("quantity") Integer quantity,
                                    @RequestParam("productId") Long id, RedirectAttributes attributes, HttpServletRequest request){
        if (quantity < 1) {
            attributes.addFlashAttribute("msg", "So luong khong the nho hon 1");
            return urlUtils.getPreviousPageByRequest(request).orElse("/");
        }
        cartService.saveItemToCart(productService.getProductById(id), preOrder, quantity);
        return "redirect:/staff/order/" + preOrder;
    }
    @GetMapping("/list-order")
    public String getListOrder(Model model){
        model.addAttribute("preorderList", cartService.getAllCartByUser());
        return "chonban";
    }
    @GetMapping("/completed")
    public String getListOrderComplete(Model model){
        model.addAttribute("preorderList", cartService.getAllOrderDone());
        return "tramon";
    }
    @GetMapping("/process/{id}")
    public String checkDeliver(@PathVariable Long id){
        cartService.changeStatus(id, Status.DELIVERED);
        return "redirect:/staff/completed";
    }
    @GetMapping("/deleteOrder/{id}")
    public String handleDeleteAllProduct(@PathVariable Long id){
        cartService.deleteAllItemInCart(id);
        return "redirect:/staff/order/" + id;
    }
    @GetMapping("/order/add/{id}")
    public String orderProduct(@PathVariable Long id){
        cartService.orderAllItem(id);
        return "redirect:/staff/order/" + id;
    }


//    @GetMapping("/product/{id}")
//    public String getViewProductDetail(@PathVariable int id, Model model) {
//        Integer idCategory = productService.getCategoryId(id);
//        List<Product> list = productService.getListProductByCategoryId(idCategory);
//        Collections.shuffle(list);
//        if (list.size() > 8) list = list.subList(0, 7);
//        model.addAttribute("listSimilar", list);
//        model.addAttribute("productDetail", productService.getProductById(id));
//        return "productdetail";
//    }

//    @PostMapping("/product/{id}")
//    public String handleAddProductToCart(@PathVariable("id") int id,
//                                         RedirectAttributes redirectAttributes,
//                                         @RequestParam("quantity") String quantitystring) {
//        int quantity = Float.valueOf(quantitystring).intValue();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof AnonymousAuthenticationToken) return "redirect:/login";
//        Product product = productService.getProductById(id);
//        boolean success = cartService.saveItemToCart(product, quantity);
//        String msg;
//        if (success) msg = "Thêm giỏ hàng thành công";
//        else msg = "Thêm giỏ hàng thất bại";
//        redirectAttributes.addFlashAttribute("msg", msg);
//        return "redirect:/product/{id}";
//    }

}
