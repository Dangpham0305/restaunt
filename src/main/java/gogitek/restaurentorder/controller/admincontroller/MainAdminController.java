package gogitek.restaurentorder.controller.admincontroller;

import gogitek.restaurentorder.constaint.FormatPrice;
import gogitek.restaurentorder.modelutil.ChartDTO;
import gogitek.restaurentorder.modelutil.DateFilterDTO;
import gogitek.restaurentorder.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainAdminController {
    private final AdminService adminService;
    private final FormatPrice formatPrice;

    public MainAdminController(AdminService adminService, FormatPrice formatPrice) {
        this.adminService = adminService;
        this.formatPrice = formatPrice;
    }

    @GetMapping("/admin")
    public String getViewMainAdmin(Model model) {
        model.addAttribute("format", formatPrice);
        model.addAttribute("orderDone", adminService.countOrders());
        model.addAttribute("total", adminService.countOrders());
        model.addAttribute("revenue", adminService.getRevenue());
        model.addAttribute("cost", adminService.getCost());
        return "admin-page/admin";
    }

//    @PostMapping("/admin/fill")
//    public String getViewStatisticAdmin(Model model, @ModelAttribute DateFilterDTO dateFilterDTO, BindingResult bindingResult) {
//        if (dateFilterDTO.getStartFill() == null || dateFilterDTO.getEndFill() == null || bindingResult.hasErrors()) return "redirect:/admin";
//        ChartDTO chartDTO = new ChartDTO();
//        chartDTO.setRevenue(adminService.getTotalPriceByDate(dateFilterDTO.getStartFill(),dateFilterDTO.getEndFill()));
//        chartDTO.setCost(adminService.getImportPriceByDate(dateFilterDTO.getStartFill(),dateFilterDTO.getEndFill()));
//        model.addAttribute("totalFill", chartDTO.getRevenue());
//        model.addAttribute("importFill", chartDTO.getCost());
//        model.addAttribute("countOrdersFill", adminService.getTotalOrdersByDate(dateFilterDTO.getStartFill(),dateFilterDTO.getEndFill()));
//        model.addAttribute("countUserFill", adminService.getTotalUserId(dateFilterDTO.getStartFill(),dateFilterDTO.getEndFill()));
//        model.addAttribute("dateFill", dateFilterDTO);
//        model.addAttribute("dateParam", dateFilterDTO);
//        model.addAttribute("chartDTO", chartDTO);
//        adminService.findOrderDetailByDay(dateFilterDTO.getStartFill(), dateFilterDTO.getEndFill()).forEach(orderAdmin -> System.err.println(orderAdmin.toString()));
//        model.addAttribute("dsProduct", adminService.findOrderDetailByDay(dateFilterDTO.getStartFill(), dateFilterDTO.getEndFill()));
//
//        return "admin-page/admin2";
//    }

    @GetMapping("/get-chart-information")
    @ResponseBody
    public ChartDTO handleChartInformation() {
        return adminService.getInformationForChart();
    }
//    @GetMapping("/getFillChartInformation/{fromdate}/{todate}")
//    @ResponseBody
//    public ChartDTO handleChartFillterInformation(@PathVariable("fromdate") String fromdate,
//                                                  @PathVariable("todate") String todate) throws ParseException {
//        ChartDTO chartDTO = new ChartDTO();
//        System.out.println(fromdate);
//        System.out.println(todate);
//        Date start =new SimpleDateFormat("yyyy-dd-MM").parse(fromdate);
//        Date end =new SimpleDateFormat("yyyy-dd-MM").parse(todate);
//        chartDTO.setRevenue(adminService.getTotalPriceByDate(start, end));
//        chartDTO.setCost(adminService.getImportPriceByDate(start,end));
//        System.out.println(chartDTO.getCost());
//        System.out.println(chartDTO.getRevenue());
//        return chartDTO;
//    }
}
