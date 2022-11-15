package gogitek.restaurentorder.controller.admincontroller;

import gogitek.restaurentorder.constaint.FormatPrice;
import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.modelutil.DateFilterDTO;
import gogitek.restaurentorder.service.AdminService;
import gogitek.restaurentorder.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class OrderAdminController {
    private final AdminService adminService;
    private final FormatPrice formatPrice;
    private final OrderService orderService;

    public OrderAdminController(AdminService adminService, FormatPrice formatPrice, OrderService orderService) {
        this.adminService = adminService;
        this.formatPrice = formatPrice;
        this.orderService = orderService;
    }

    @ModelAttribute
    public void addFormatService(Model model) {
        model.addAttribute("format", formatPrice);
    }

    @GetMapping("/admin/order")
    public String getListOrderAdmin(Model model, HttpServletRequest request) {
        model.addAttribute("countOrder", adminService.countOrders());
        model.addAttribute("countCart", adminService.countCart());
        model.addAttribute("dateFill", new DateFilterDTO());
        model.addAttribute("orderAdmin", adminService.getOrderAdmin());
        model.addAttribute("countProcessing", adminService.countByStatus(Status.PROCESSING.getValue()));
        model.addAttribute("countCancel", adminService.countByStatus(Status.CANCELED.getValue()));
        model.addAttribute("countDelivered", adminService.countByStatus(Status.DELIVERED.getValue()));
        model.addAttribute("countApproved", adminService.countByStatus(Status.APPROVED.getValue()));
        return "admin-page/order";
    }
    @GetMapping("/admin/preorder")
    public String getListPreOrderAdmin(Model model) {
        model.addAttribute("listPreOrder", new ArrayList<>());
        return "admin-page/preorder";
    }

    @GetMapping("/admin/order/{id}")
    public String getViewOrderAdmin(@PathVariable int id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "admin-page/view-order";
    }

    @PostMapping("/admin/order/edit/{id}")
    public String handleEditStatusOrderAdmin(@PathVariable int id, @ModelAttribute Orders orders,
                                             @RequestParam Status status) {
        orders.setStatus(status);
        orderService.updateStatus(id, orders);
        return "redirect:/admin/order/{id}";
    }


//    @GetMapping("/admin/order/fill-by-status/{value}")
//    public String handleFillOrderByStatus(@PathVariable Status value, Model model){
//        model.addAttribute("orderAdmin", adminService.findOrdersByStatus(value.getValue()));
//        return "admin-page/order";
//    }
}
