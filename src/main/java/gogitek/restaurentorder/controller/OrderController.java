package gogitek.restaurentorder.controller;

import gogitek.restaurentorder.constaint.FormatPrice;
import gogitek.restaurentorder.constaint.UrlUtils;
import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.entity.PreOrder;
import gogitek.restaurentorder.modelutil.CartItem;
import gogitek.restaurentorder.modelutil.PaymentInformation;
import gogitek.restaurentorder.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/staff")
public class OrderController {
    private final CategoryService categoryService;
    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;
    private final FormatPrice formatPrice;
    private final UrlUtils urlUtils;
    private final PythonUtils pythonUtils;

    private static final String currentDirectory = System.getProperty("user.dir");
    private static final Path path = Paths.get(currentDirectory + Paths.get("/target/classes/static/image/faceimage"));


    @ModelAttribute
    public void addAttributeToHeader(Model model) {
        model.addAttribute("listCategory", categoryService.getListCategory());
        model.addAttribute("format", formatPrice);
        model.addAttribute("countCartItem", cartService.countNumberOfItemInCart());
    }
    @GetMapping("/cart/{id}")
    public String getViewCart(Model model, @PathVariable Long id) {
        PreOrder preOrder = cartService.findById(id);
        List<CartItem> listProductInCart = productService.getProductInOrder(preOrder);
        model.addAttribute("listProductInCart", listProductInCart);
        model.addAttribute("tempPrice", listProductInCart.stream().mapToDouble(CartItem::getTotalPrice).sum());
        model.addAttribute("preorderId", id);
        return "ViewCart";
    }

    @GetMapping("/payment/{id}")
    public String getViewPayment(Model model, @PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        PreOrder preOrder = cartService.findById(id);
        if (cartService.checkOrderDelivered(preOrder)){
            redirectAttributes.addFlashAttribute("msg", "Chưa trả hết món!");
            return urlUtils.getPreviousPageByRequest(request).orElse("/");
        }
        Orders order = orderService.saveNewOrder(preOrder.getId());
        model.addAttribute("preOrder", preOrder);
        model.addAttribute("order", order);
        List<CartItem> listProductInCart = productService.getProductInOrder(preOrder);
        model.addAttribute("listProductInCart", listProductInCart);
        model.addAttribute("orderedList", preOrder.getPreOrderDetails());
        Double temp = preOrder.getPreOrderDetails().stream().mapToDouble(detail -> detail.getQuantity() * detail.getProduct().getSalePrice()).sum();
        model.addAttribute("tempPrice", temp);
        return "payment";
    }
    @PostMapping("/upload-file")
    public @ResponseBody ResponseEntity<?> uploadFile(MultipartFile photo, @RequestParam("order") Long orderId) {
        if (photo == null || photo.isEmpty()) {
            return ResponseEntity.badRequest().body("Photo not captured");
        }
        double discount = 0D;
        try {
            byte[] bytes = photo.getBytes();
            Path pt = Paths.get(path+photo.getOriginalFilename());
            Files.write(pt, bytes);
            discount = Double.parseDouble(pythonUtils.recognize(pt.toAbsolutePath().toString()));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("failed to recognize");
        }
        if (discount != 0) {
            orderService.updateStatus(discount, orderId);
        }
        return ResponseEntity.ok(discount);
    }
    @PostMapping("/reload")
    public ResponseEntity<?> reloadPrice(@RequestBody Long id){
        Double totalPrice = orderService.loadNewPrice(id);
        return ResponseEntity.ok(totalPrice);
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

}
