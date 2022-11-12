package gogitek.restaurentorder.controller;

import gogitek.restaurentorder.constaint.FormatPrice;
import gogitek.restaurentorder.entity.PreOrder;
import gogitek.restaurentorder.modelutil.CartDTO;
import gogitek.restaurentorder.modelutil.CartItem;
import gogitek.restaurentorder.service.CartService;
import gogitek.restaurentorder.service.CategoryService;
import gogitek.restaurentorder.service.OrderService;
import gogitek.restaurentorder.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    private final CategoryService categoryService;
    private final CartService cartService;
    private final ProductService productService;
    private final FormatPrice formatPrice;
    private final OrderService orderService;

    public CartController(CategoryService categoryService, CartService cartService,
                          ProductService productService, FormatPrice formatPrice, OrderService orderService) {
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.productService = productService;
        this.formatPrice = formatPrice;
        this.orderService = orderService;
    }

    @ModelAttribute
    public void addAttributeToHeader(Model model) {
        model.addAttribute("listCategory", categoryService.getListCategory());
        model.addAttribute("format", formatPrice);
        model.addAttribute("countCartItem", cartService.countNumberOfItemInCart());
    }
    @GetMapping("/cart")
    public String getViewCart(Model model) {
        List<PreOrder> carts = cartService.getAllCartByUser();
        List<CartItem> listProductInCart = productService.getProductFromCart(carts);
        Float tempPrice = productService.getTempPriceOfCart(listProductInCart);
        Float ship = 20000f;
        if(tempPrice > 50000) ship = 0f;
        Float totalPrice = tempPrice + ship;
        model.addAttribute("tempPrice", formatPrice.formatPrice(tempPrice));
        model.addAttribute("ship", formatPrice.formatPrice(ship));
        model.addAttribute("totalPrice", formatPrice.formatPrice(totalPrice));
        model.addAttribute("listProductInCart", listProductInCart);
        CartDTO cartDTO = new CartDTO();
        model.addAttribute("listQuantity", cartDTO);
        return "ViewCart";
    }

    @GetMapping("/cart/delete")
    public String handleDeleteAllProduct(){
        cartService.deleteAllItemInCart();
        return "redirect:/cart";
    }
    @GetMapping("/cart/{id}")
    public String handleDeleteProduct(@PathVariable("id") int productId){
        cartService.deleteAnItemInCart(productId);
        return "redirect:/cart";
    }
    @PostMapping("/cart/save")
    public String handleSaveNewCart(@RequestParam("soluong") String[] list){
        List<Integer> soluong = new ArrayList<>();
        for(int i=0; i<list.length; i++){
            soluong.add(Integer.parseInt(list[i]));
        }
        List<PreOrder> listCart = cartService.getAllCartByUser();
        cartService.saveNewQuantity(listCart, soluong);
        return "redirect:/cart";
    }
    @GetMapping("/user/repurchase/{id}")
    public String getViewRepurchase(@PathVariable int id){
        cartService.saveItemToCartByOrder(orderService.getOrderById(id));
        return "redirect:/cart";
    }

}
