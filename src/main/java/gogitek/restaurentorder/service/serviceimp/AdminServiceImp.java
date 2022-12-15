package gogitek.restaurentorder.service.serviceimp;

import gogitek.restaurentorder.constaint.DateFormat;
import gogitek.restaurentorder.constaint.Role;
import gogitek.restaurentorder.entity.OrderDetail;
import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.entity.Product;
import gogitek.restaurentorder.entity.User;
import gogitek.restaurentorder.modelutil.*;
import gogitek.restaurentorder.repository.*;
import gogitek.restaurentorder.service.AdminService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AdminServiceImp implements AdminService {

    private final OrdersRepo ordersRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final PasswordEncoder passwordEncoder;
    private final CartRepo cartRepo;
    private final DateFormat dateFormat;

    public AdminServiceImp(OrdersRepo ordersRepo, UserRepo userRepo, ProductRepo productRepo, OrderDetailRepo orderDetailRepo, PasswordEncoder passwordEncoder, CartRepo cartRepo, DateFormat dateFormat) {
        this.ordersRepo = ordersRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.orderDetailRepo = orderDetailRepo;
        this.passwordEncoder = passwordEncoder;
        this.cartRepo = cartRepo;
        this.dateFormat = dateFormat;
    }

    @Override
    public Integer countOrders() {
        return ordersRepo.countOrders();
    }


    @Override
    public Double getRevenue() {
        List<Orders> list = ordersRepo.findAll();
        return list.stream().mapToDouble(Orders::getTotalPrice).sum();
    }
    @Override
    public Double getCost() {
        List<Orders> list = ordersRepo.findAll();
        Double cost = 0D;
        for (Orders orders: list) {
            Set<OrderDetail> details = orders.getOrderDetails();
            cost += details.stream().mapToDouble(value -> value.getProduct().getCost() * value.getQuantity()).sum();
        }
        return cost;
    }

    @Override
    public List<Product> getListProduct() {
        return productRepo.getAllByDelete(false);
    }

    @Override
    public ChartDTO getInformationForChart() {
        return ChartDTO.builder().cost(getCost()).revenue(getRevenue()).build();
    }

    @Override
    public List<User> getListUserByRole(List<Role> role) {
        return userRepo.getUserByRoleIn(role);
    }

    @Override
    public boolean addStaff(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;

    }

    @Override
    public User getUserById(Long id) {
        return userRepo.getById(id);
    }

    @Override
    public boolean updateStaff(Long id, User user) {
        User staff = userRepo.getById(id);
        staff.setFirstName(user.getFirstName());
        staff.setLastName(user.getLastName());
        staff.setPhoneNumber(user.getPhoneNumber());
        staff.setAddress(user.getAddress());
        staff.setRole(user.getRole());
        userRepo.save(staff);
        return true;
    }

    @Override
    public boolean deleteStaff(Long id) {
        userRepo.delete(userRepo.getById(id));
        return true;
    }


    @Override
    public Integer countCart() {
        return 0;
    }

    @Override
    public Integer countByStatus(int status) {
        return ordersRepo.countOrdersByStatus(status);
    }


}
