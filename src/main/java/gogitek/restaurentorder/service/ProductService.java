package gogitek.restaurentorder.service;

import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.entity.OrderDetail;
import gogitek.restaurentorder.entity.PreOrder;
import gogitek.restaurentorder.entity.PreOrderDetail;
import gogitek.restaurentorder.entity.Product;
import gogitek.restaurentorder.modelutil.CartItem;

import java.util.List;
public interface ProductService {
    Product getProductById(Long id);
    List<Product> findAllProduct();
    List<PreOrderDetail> getProductFromCart(PreOrder cartList, List<Status> status);
    List<CartItem> getProductInOrder(PreOrder cartList);
    boolean addProduct(Product product);
    boolean deleteProduct(Long id);
    void updateProduct(Long id, Product product);

    boolean checkProductInUse(Long productId);
}
