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
    Double getRevenue();
    Double getCost();
    List<Product> getListProduct();
    ChartDTO getInformationForChart();
    List<User> getListUserByRole(List<Role> role);
    boolean addStaff(User user);
    User getUserById(Long id);
    boolean updateStaff(Long id, User user);
    boolean deleteStaff(Long id);
    Integer countCart();
    Integer countByStatus(int status);
}
