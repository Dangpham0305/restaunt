package gogitek.restaurentorder.service;

import gogitek.restaurentorder.entity.OrderDetail;
import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.entity.Product;

public interface OrderDetailService {
    OrderDetail saveOrderDetail(Product product, Orders orders, Float price, Integer quantity);
}
