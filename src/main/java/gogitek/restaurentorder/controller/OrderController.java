package gogitek.restaurentorder.controller;

import gogitek.restaurentorder.constaint.FormatPrice;
import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.constaint.UrlUtils;
import gogitek.restaurentorder.entity.*;
import gogitek.restaurentorder.modelutil.CartItem;
import gogitek.restaurentorder.modelutil.PaymentInformation;
import gogitek.restaurentorder.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final CategoryService categoryService;
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final FormatPrice formatPrice;
    private final UrlUtils urlUtils;



    @ModelAttribute
    public void addAttributeToHeader(Model model) {
        model.addAttribute("listCategory", categoryService.getListCategory());
        model.addAttribute("format", formatPrice);
        model.addAttribute("countCartItem", cartService.countNumberOfItemInCart());
    }

    @GetMapping("/payment/{id}")
    public String getViewPayment(Model model, @PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        PreOrder preOrder = cartService.findById(id);
        if (cartService.checkOrderDelivered(preOrder)){
            redirectAttributes.addFlashAttribute("msg", "Chưa trả hết món!");
            return urlUtils.getPreviousPageByRequest(request).orElse("/");
        }
        model.addAttribute("preOrder", preOrder);
        model.addAttribute("orderedList", preOrder.getPreOrderDetails());
        Double totalPrice = preOrder.getPreOrderDetails().stream().mapToDouble(detail -> detail.getQuantity() * detail.getProduct().getSalePrice()).sum();
        model.addAttribute("total", totalPrice);
        return "payment";
    }

    @PostMapping("/payment/process")
    public String handlePaymentProcess(@ModelAttribute PaymentInformation paymentInformation) {
//        User user = userService.getCurrentUser();
//        List<PreOrder> listCart = cartService.getAllCartByUser();
//        List<CartItem> listProductInCart = productService.getProductFromCart(listCart);
//        Orders orders = orderService.saveNewOrder(paymentInformation);
//        orders.setUser(user);
//        Set<OrderDetail> orderDetailList = new HashSet<>();
//        Double realPrice = Double.valueOf(0f);
//        for (CartItem cart : listProductInCart) {
//            Product product = productService.getProductById(cart.getProductId());
//            OrderDetail orderDetail = orderDetailService.saveOrderDetail(
//                    product, orders,
//                    cart.getTotalPrice(), cart.getQuantity());
//            productService.saveAfterOrder(product, orderDetail);
//            realPrice += orderDetail.getPrice();
//            orderDetailList.add(orderDetail);
//        }
//        Float ship = 20000f;
//        if (realPrice > 50000) ship = 0f;
//        double totalPrice = realPrice + ship;
//        orderService.saveOrder(orders, (float) totalPrice, paymentInformation.getOrder().getNote(), orderDetailList);
//        cartService.deleteAllItemInCart();
        return "redirect:/payment/ordersucess";
    }

    @GetMapping("/payment/ordersucess")
    public String getViewOrderSucess() {
        return "success-order";
    }

}
