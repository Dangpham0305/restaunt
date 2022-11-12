package com.orfarmweb.service.serviceimp;

import com.orfarmweb.constaint.DateFormat;
import com.orfarmweb.constaint.Role;
import com.orfarmweb.entity.OrderDetail;
import com.orfarmweb.entity.Orders;
import com.orfarmweb.entity.Product;
import com.orfarmweb.entity.User;
import com.orfarmweb.modelutil.*;
import com.orfarmweb.repository.*;
import com.orfarmweb.service.AdminService;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public Integer countCustomer() {
        return userRepo.countCustomer();
    }

    @Override
    public Float getRevenue() {
        if (ordersRepo.getRevenue() == null) return 0f;
        return ordersRepo.getRevenue();
    }

    @Override
    public List<Product> getListProduct() {
        return productRepo.findAll();
    }

    @Override
    public List<OrderDetailDTO> getTopOrderDetail() {
        List<OrderDetail> listOrderDetail = orderDetailRepo.getTopOrder();
        List<OrderDetailDTO> list = new ArrayList<>();
        listOrderDetail.forEach(orderDetail -> list.add(new OrderDetailDTO(orderDetail)));
        return list;
    }

    @Override
    public List<OrderAdmin> getOrderAdmin() {
        List<Orders> listOrder = ordersRepo.getOrderUser();
        List<OrderAdmin> list = new ArrayList<>();
        listOrder.forEach(orders -> list.add(new OrderAdmin(orders, orderDetailRepo.getTotalProduct(orders.getId()))));
        return list;
    }

    @Override
    public List<ProductAdminDTO> getHub() {
        List<Product> list = productRepo.findAll();
        List<ProductAdminDTO> productAdminDTOS = new ArrayList<>();
        list.forEach(product -> productAdminDTOS.add(new ProductAdminDTO(product)));
        return productAdminDTOS;
    }

//    @Override
//    public List<ProductAdminDTO> searchHubByNameAndPage(String keyWord, long currentPage) {
//        List<Product> list = productRepo.searchByNameAndPage(keyWord,(currentPage - 1) * pageSize, pageSize);
//        List<ProductAdminDTO> productAdminDTOS = new ArrayList<>();
//        list.forEach(product -> productAdminDTOS.add(new ProductAdminDTO(product)));
//        return productAdminDTOS;
//    }

//    @Override
//    public long getTotalPageHubByKeyWord(String keyWord) {
//        return (productRepo.countByKeyWord(keyWord).get(0) % pageSize == 0) ? productRepo.countByKeyWord(keyWord).get(0) / pageSize
//                : (productRepo.countByKeyWord(keyWord).get(0) / pageSize) + 1;
//    }

    @Override
    public Float getCostOfProduct() {
        if (ordersRepo.getRevenue() == null) return 0f;
        List<OrderDetail> orderDetails = orderDetailRepo.getListRevenueOrder();
        float sum = 0f;
        for (OrderDetail orderDetail : orderDetails) {
            sum = sum + orderDetail.getProduct().getCost();
        }
        return sum;
    }

    @Override
    public ChartDTO getInformationForChart() {
        List<OrderDetail> orderDetails = orderDetailRepo.getListRevenueOrder();
        float sum = 0f;
        for (OrderDetail orderDetail : orderDetails) {
            sum = sum + orderDetail.getProduct().getCost();
        }
        ChartDTO chartDTO = new ChartDTO();
        chartDTO.setRevenue(getRevenue());
        chartDTO.setCost(sum);
        return chartDTO;
    }

    @Override
    public List<User> getListUserByRole(Role role) {
        return userRepo.getUserByRole(role);
    }

    @Override
    public boolean addStaff(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.STAFF);
        userRepo.save(user);
        return true;

    }

    @Override
    public User getUserById(int id) {
        return userRepo.getById(id);
    }

    @Override
    public boolean updateStaff(int id, User user) {
        User staff = userRepo.getById(id);
        staff.setFirstName(user.getFirstName());
        staff.setLastName(user.getLastName());
        staff.setPhoneNumber(user.getPhoneNumber());
        staff.setAddress(user.getAddress());
        userRepo.save(staff);
        return true;
    }

    @Override
    public boolean deleteStaff(int id) {
        userRepo.delete(userRepo.getById(id));
        return true;
    }

    @Override
    public List<OrderAdmin> getListOrderAdminByFilter(Date s, Date e) {
        if(s.compareTo(e)>0){
            Date temp = s;
            s = e;
            e = temp;
        }
        e = dateFormat.addOneDay(e);
        List<Orders> ordersList = ordersRepo.getOrderUserFilter(s, e);
        List<OrderAdmin> list = new ArrayList<>();
        Date finalE = e;
        Date finalS = s;
        ordersList.forEach(orders -> list.add(new OrderAdmin(orders, orderDetailRepo.getTotalProductByFilterAndOrderId(orders.getId(), finalS, finalE))));
        return list;
    }

    @Override
    public Integer countCart() {
        return cartRepo.countCart();
    }

    @Override
    public Integer countByStatus(int status) {
        return ordersRepo.countOrdersByStatus(status);
    }

    @Override
    public List<OrderAdmin> findOrdersByStatus(int status) {
        List<Orders> ordersList = ordersRepo.findOrdersByStatus(status);
        List<OrderAdmin> orderAdmins = new ArrayList<>();
        for (Orders orderAdmin : ordersList) {
            orderAdmins.add(new OrderAdmin(orderAdmin, orderDetailRepo.getTotalProductByOrdersIdAndStatus(orderAdmin.getId(), status)));
        }
        return orderAdmins;
    }

    @Override
    public List<ProductFilterDTO> findOrderDetailByDay(Date s, Date e) {
        if(s.compareTo(e)>0){
            Date temp = s;
            s = e;
            e = temp;
        }
        e = dateFormat.addOneDay(e);
        List<ProductFilterDTO> lists = new ArrayList<>();
        List<OrderDetail> orderDetails = orderDetailRepo.findOrderDetailByDay(s,e);
        for (OrderDetail orderDetail:orderDetails) {
            int quantity = orderDetailRepo.getTotalProductByDay(s,e,orderDetail.getProduct().getId());
                lists.add(new ProductFilterDTO(orderDetail, quantity));
        }
        return lists;
    }

    @Override
    public Float getImportPriceByDate(Date s, Date e) {
        if(s.compareTo(e)>0){
            Date temp = s;
            s = e;
            e = temp;
        }
        e = dateFormat.addOneDay(e);
        List<ProductFilterDTO> lists = findOrderDetailByDay(s,e);
        float sum = 0f;
        for(ProductFilterDTO product:lists){
            sum+= product.getImportPrice();
        }
        return sum;
    }

    @Override
    public Float getTotalPriceByDate(Date s, Date e) {
        if(s.compareTo(e)>0){
            Date temp = s;
            s = e;
            e = temp;
        }
        e = dateFormat.addOneDay(e);
        List<ProductFilterDTO> lists = findOrderDetailByDay(s,e);
        float sum = 0f;
        for(ProductFilterDTO product:lists){
            sum+= product.getTotalPrice();
        }
        return sum;
    }

    @Override
    public Integer getTotalOrdersByDate(Date s, Date e) {

        if(s.compareTo(e)>0){
            Date temp = s;
            s = e;
            e = temp;
        }
        e = dateFormat.addOneDay(e);
        return orderDetailRepo.getTotalOrderByDate(s,e);
    }

    @Override
    public Integer getTotalUserId(Date s, Date e) {
        if(s.compareTo(e)>0){
            Date temp = s;
            s = e;
            e = temp;
        }
        e = dateFormat.addOneDay(e);
        return userRepo.getTotalUserId(s,e);
    }
}
