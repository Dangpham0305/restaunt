package com.orfarmweb.service;

import com.orfarmweb.constaint.Role;
import com.orfarmweb.entity.OrderDetail;
import com.orfarmweb.entity.Product;
import com.orfarmweb.entity.User;
import com.orfarmweb.modelutil.*;

import java.util.Date;
import java.util.List;

public interface AdminService {
    Integer countOrders();
    Integer countCustomer();
    Float getRevenue();
    List<Product> getListProduct();
    List<OrderDetailDTO> getTopOrderDetail();
    List<OrderAdmin> getOrderAdmin();
    List<ProductAdminDTO> getHub();
//    List<ProductAdminDTO> searchHubByNameAndPage(String keyWord, long currentPage);
//    long getTotalPageHubByKeyWord(String keyWord);
    Float getCostOfProduct();
    ChartDTO getInformationForChart();
    List<User> getListUserByRole(Role role);
    boolean addStaff(User user);
    User getUserById(int id);
    boolean updateStaff(int id, User user);
    boolean deleteStaff(int id);
    List<OrderAdmin> getListOrderAdminByFilter(Date s, Date e);
    Integer countCart();
    Integer countByStatus(int status);
    List<OrderAdmin> findOrdersByStatus(int status);
    List<ProductFilterDTO> findOrderDetailByDay(Date s, Date e);
    Float getImportPriceByDate(Date s, Date e);
    Float getTotalPriceByDate(Date s, Date e);
    Integer getTotalOrdersByDate(Date s, Date e);
    Integer getTotalUserId(Date s, Date e);
}
