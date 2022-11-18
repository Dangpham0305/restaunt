package gogitek.restaurentorder.service;

import gogitek.restaurentorder.entity.PreOrder;
import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.entity.Product;

import java.util.List;

public interface CartService {
    boolean saveItemToCart(Product product, Integer quantity);
    List<PreOrder> getAllCartByUser();
    Integer countNumberOfItemInCart();
    boolean deleteAllItemInCart();
    void saveNewQuantity(List<PreOrder> cartList, List<Integer> soluong);
    boolean deleteAnItemInCart(int productId);
    void saveItemToCartByOrder(Orders orders);

    void createNewPreOrder();
}
