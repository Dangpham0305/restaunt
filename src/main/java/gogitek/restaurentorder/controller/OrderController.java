package gogitek.restaurentorder.controller;

import gogitek.restaurentorder.constaint.FormatPrice;
import gogitek.restaurentorder.constaint.Status;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
    private static final Path pathimage = Paths.get(currentDirectory + Paths.get("/target/classes/static/faceimage"));
    private static final Path pathdata = Paths.get(currentDirectory + Paths.get("/target/classes/static/data"));


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
        List<Status> acceptedStatus = Arrays.asList(Status.PROCESSING, Status.APPROVED, Status.DONE);
        if (cartService.checkOrderDelivered(preOrder, acceptedStatus)){
            redirectAttributes.addFlashAttribute("msg", "Chưa trả hết món!");
            return urlUtils.getPreviousPageByRequest(request).orElse("/");
        }
        Orders order;
        if (preOrder.getOrderId() != null){
             order = orderService.getOrderById(preOrder.getOrderId());
        }
        else {
             order = orderService.saveNewOrder(preOrder);
        }
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
            if (!Files.exists(pathimage)){
                Files.createDirectories(pathimage);
            }
            if (!Files.exists(pathdata)){
                Files.createDirectories(pathdata);
            }
            Path pt = Paths.get(pathimage+ "/" + photo.getOriginalFilename());
            Files.write(pt, bytes);
            pythonUtils.update();
            String rs = pythonUtils.recognize(pt.toAbsolutePath().toString());
            discount = Double.parseDouble(rs);
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


    @GetMapping("/payment/process/{id}")
    public String handlePaymentProcess(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        cartService.deleteOrder(id);
        redirectAttributes.addFlashAttribute("msg", "Thanh toan thanh cong");
        return "redirect:/staff/list-order";
    }

}
