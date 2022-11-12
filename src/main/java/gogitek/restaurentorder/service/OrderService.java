package gogitek.restaurentorder.service;

import gogitek.restaurentorder.entity.OrderDetail;
import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.modelutil.PaymentInformation;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Orders saveNewOrder(PaymentInformation information);
    boolean saveOrder(Orders orders, Float totalPrice, String note,Set<OrderDetail> orderDetailList);
    List<Orders> getListOrderByCurrentUser();
    Orders getOrderById(int id);
    void updateStatus(int id, Orders orders);
}
