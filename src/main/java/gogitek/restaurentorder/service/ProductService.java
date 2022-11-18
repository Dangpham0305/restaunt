package gogitek.restaurentorder.service;

import gogitek.restaurentorder.entity.OrderDetail;
import gogitek.restaurentorder.entity.PreOrder;
import gogitek.restaurentorder.entity.Product;
import gogitek.restaurentorder.modelutil.CartItem;
import gogitek.restaurentorder.modelutil.ProductAdminDTO;

import java.util.List;
public interface ProductService {
    List<Product> getListProductByCategoryId(int id);
    long getTotalPageByFill(float start, float end, int id);
    List<Product> getListProductFillByPage(float start, float end, long currentPage, int id);
    int getTotalByFill(float start, float end, int id);
    int getTotal();
    List<Product> getListProductByHot();
    List<Product> getListSaleProduct();
    Product getProductById(int id);
    long getTotalPage();
    List<Product> getByPage(long currentPage);
    int getCategoryId(int id);
    List<CartItem> getProductFromCart(List<PreOrder> cartList);
    Float getTempPriceOfCart(List<CartItem> itemList);
    boolean addProduct(Product product);
    boolean deleteProduct(int id);
    void updateProduct(int id, Product product);
    List<ProductAdminDTO> findAll();
    List<Product> findProductByName(int id, String keyWord, long currentPage);
    long getTotalPageByName(int id, String keyWord);
    void saveAfterOrder(Product product, OrderDetail orderDetail);
}
