package gogitek.restaurentorder.service.serviceimp;

import gogitek.restaurentorder.entity.OrderDetail;
import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.entity.Product;
import gogitek.restaurentorder.repository.OrderDetailRepo;
import gogitek.restaurentorder.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImp implements OrderDetailService {

    private final OrderDetailRepo orderDetailRepo;

    public OrderDetailServiceImp(OrderDetailRepo orderDetailRepo) {
        this.orderDetailRepo = orderDetailRepo;
    }

    @Override
    public OrderDetail saveOrderDetail(
            Product product, Orders orders, Float price, Integer quantity) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setPrice(orderDetail.getQuantity()* product.getSalePrice() * (100-product.getPercentDiscount())/100);
        return orderDetailRepo.saveAndFlush(orderDetail);
    }
}
