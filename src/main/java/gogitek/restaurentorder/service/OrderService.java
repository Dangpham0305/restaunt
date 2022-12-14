package gogitek.restaurentorder.service;

import gogitek.restaurentorder.entity.OrderDetail;
import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.entity.PreOrder;
import gogitek.restaurentorder.modelutil.PaymentInformation;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Orders saveNewOrder(PreOrder preOrder);
    Double loadNewPrice(Long id);
    List<Orders> getListOrderByCurrentUser();
    Orders getOrderById(Long id);
    void updateStatus(Double discount, Long ordersid);
}
