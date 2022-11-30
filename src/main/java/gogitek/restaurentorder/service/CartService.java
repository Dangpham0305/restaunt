package gogitek.restaurentorder.service;

import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.entity.PreOrder;
import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.entity.PreOrderDetail;
import gogitek.restaurentorder.entity.Product;

import java.util.List;

public interface CartService {
    boolean saveItemToCart(Product product, Long preorderId, Integer quantity);

    List<PreOrder> getAllCartByUser();
    Integer countNumberOfItemInCart();
    boolean deleteAllItemInCart(Long id);
    void saveNewQuantity(List<PreOrder> cartList, List<Integer> soluong);
    boolean deleteAnItemInCart(int productId);
    void saveItemToCartByOrder(Orders orders);
    PreOrder addNewCart(PreOrder preOrder);
    PreOrder findById(Long iod);
    boolean orderAllItem(Long id);
    List<PreOrderDetail> getAllOrder();
    void changeStatus(Long detailId, Status status);
    boolean checkOrderDelivered(PreOrder preOrder);
}
