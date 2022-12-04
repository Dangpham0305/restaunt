package gogitek.restaurentorder.service;

import gogitek.restaurentorder.constaint.Role;
import gogitek.restaurentorder.entity.Product;
import gogitek.restaurentorder.entity.User;
import gogitek.restaurentorder.modelutil.*;
import gogitek.restaurentorder.modelutil.*;

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
    List<User> getListUserByRole(List<Role> role);
    boolean addStaff(User user);
    User getUserById(Long id);
    boolean updateStaff(Long id, User user);
    boolean deleteStaff(Long id);
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
