package gogitek.restaurentorder.controller.admincontroller;

import gogitek.restaurentorder.constaint.Role;
import gogitek.restaurentorder.entity.User;
import gogitek.restaurentorder.modelutil.PasswordDTO;
import gogitek.restaurentorder.service.AdminService;
import gogitek.restaurentorder.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
public class ManagementController {
    private final AdminService adminService;
    private final UserService userService;

    public ManagementController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/admin/staffManager")
    public String getViewStaff(Model model) {
        model.addAttribute("staffList", adminService.getListUserByRole(Arrays.asList(Role.WAITER, Role.CHEF)));
        return "admin-page/staff";
    }

    @GetMapping("/admin/staffManager/addStaff")
    public String getViewAddStaff(Model model) {
        model.addAttribute("staff", new User());
        return "/admin-page/add-staff";
    }

    @PostMapping("/admin/staffManager/addStaff")
    public String handleAddStaff(RedirectAttributes redirectAttributes, @ModelAttribute User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) return "redirect:/admin/staffManager/addStaff";
        if (adminService.addStaff(user))
            redirectAttributes.addFlashAttribute("msg", "Thêm nhân viên thành công");
        return "redirect:/admin/staffManager";
    }

    @GetMapping("/admin/staffManager/editStaff/{id}")
    public String getViewEditStaff(Model model, @PathVariable("id") Long id) {
        model.addAttribute("staff", adminService.getUserById(id));
        return "/admin-page/add-staff";
    }

    @PostMapping("/admin/staffManager/editStaff/{id}")
    public String handleEditStaff(RedirectAttributes redirectAttributes,
                                  @ModelAttribute User user, @PathVariable("id") Long id) {
        if (adminService.updateStaff(id, user))
            redirectAttributes.addFlashAttribute("msg", "Thêm nhân viên thành công");
        return "redirect:/admin/staffManager";
    }

    @GetMapping("admin/staffManager/deleteStaff/{id}")
    public String handleDeleteStaff(@PathVariable("id") Long id) {
        adminService.deleteStaff(id);
        return "redirect:/admin/staffManager";
    }


    @GetMapping("/admin/personal-infor")
    public String getViewPersonalInfo(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("checkpassword", new PasswordDTO());
        return "/admin-page/personal-infor-admin";
    }

    @PostMapping("/admin/edit-user")
    public String handleEditUser(@ModelAttribute User user) {
        return "redirect:/admin/personal-infor";
    }

    @PostMapping("/admin/edit-password")
    public String handleEditPassword(RedirectAttributes redirectAttributes, @ModelAttribute PasswordDTO passwordDTO) {
        String msg;
        if (userService.updatePassword(passwordDTO)) msg = "Thay đổi mật khẩu thành công";
        else msg = "Thay đổi mật khẩu thất bại";
        redirectAttributes.addAttribute("msg", msg);
        return "redirect:/admin/personal-infor";
    }
}
