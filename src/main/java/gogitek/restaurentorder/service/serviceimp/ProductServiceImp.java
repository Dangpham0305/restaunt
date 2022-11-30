package gogitek.restaurentorder.service.serviceimp;

import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.entity.OrderDetail;
import gogitek.restaurentorder.entity.PreOrder;
import gogitek.restaurentorder.entity.PreOrderDetail;
import gogitek.restaurentorder.entity.Product;
import gogitek.restaurentorder.modelutil.CartItem;
import gogitek.restaurentorder.modelutil.ProductAdminDTO;
import gogitek.restaurentorder.repository.ProductRepo;
import gogitek.restaurentorder.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {
    private final ProductRepo productRepo;
    final long pageSize = 6;

    public ProductServiceImp(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public List<Product> getListProductByCategoryId(int id) {
        return productRepo.findProductByCategoryId(id);
    }

    @Override
    public int getTotalByFill(float start, float end, int id) {
        return productRepo.getTotalProductByFill(start, end, id);
    }

    @Override
    public int getTotal() {
        return productRepo.getTotal();
    }

    @Override
    public List<Product> getListProductByHot() {
        return productRepo.getProductByHot();
    }

    @Override
    public List<Product> getListSaleProduct() {
        return productRepo.getSaleProduct();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepo.getById(id);
    }

    @Override
    public long getTotalPage() {
        return (productRepo.countProduct().get(0) % pageSize == 0) ?
                productRepo.countProduct().get(0) / pageSize
                : (productRepo.countProduct().get(0) / pageSize) + 1;
    }

    @Override
    public List<Product> getByPage() {
        return productRepo.findAll();
    }
    @Override
    public long getTotalPageByFill(float start, float end, int id) {
        return (productRepo.countByCategoryIdAndFill(start,end,id).get(0) % pageSize == 0) ?
                productRepo.countByCategoryIdAndFill(start,end,id).get(0) / pageSize
                : (productRepo.countByCategoryIdAndFill(start,end,id).get(0) / pageSize) + 1;
    }

    @Override
    public List<Product> getListProductFillByPage(float start, float end, long currentPage, int id) {
        return productRepo.listFill(start,end,id,(currentPage-1)*pageSize,pageSize);
    }
    @Override
    public int getCategoryId(int id) {
        return productRepo.findCateGoryIdByProdId(id);
    }

    @Override
    public List<PreOrderDetail> getProductFromCart(PreOrder cartList, List<Status> status) {
        return cartList.getPreOrderDetails().stream().filter(preOrderDetail -> status.contains(preOrderDetail.getStatus())).collect(Collectors.toList());
    }

    @Override
    public List<CartItem> getProductInOrder(PreOrder cartList) {
        List<CartItem> itemList = new ArrayList<>();
        cartList.getPreOrderDetails().stream().filter(detail -> !detail.getStatus().equals(Status.CANCELED)).forEach(detail -> {
            CartItem item = CartItem.builder()
                    .productId(detail.getProduct().getId())
                    .productName(detail.getProduct().getName())
                    .quantity(detail.getQuantity())
                    .image(detail.getProduct().getImage())
                    .salePrice(detail.getProduct().getSalePrice())
                    .totalPrice(detail.getProduct().getSalePrice() * detail.getQuantity())
                    .status(detail.getStatus())
                    .build();
            itemList.add(item);
        });
        return itemList;
    }

    @Override
    public Float getTempPriceOfCart(List<CartItem> itemList) {
//        Float tempPrice = 0f;
//        for (CartItem cartItem: itemList) {
//            tempPrice += cartItem.getTotalPrice();
//        }
//        return tempPrice;
        return null;
    }

    @Override
    public boolean addProduct(Product product) {
//        product.setQuantityProd(0);
        productRepo.save(product);
        return true;
    }

    @Override
    public boolean deleteProduct(Long id) {
        productRepo.delete(productRepo.getById(id));
        return true;
    }

    @Override
    public void updateProduct(Long id, Product product) {
        Product baseProduct = productRepo.getById(id);
        if(!product.getName().isEmpty()) baseProduct.setName(product.getName());
        if(!product.getDescription().isEmpty()) baseProduct.setDescription(product.getDescription());
        if(product.getSalePrice()!=null) baseProduct.setSalePrice(product.getSalePrice());
        if(product.getCategory()!=null) baseProduct.setCategory(product.getCategory());
        if(product.getPercentDiscount()!=null) baseProduct.setPercentDiscount(product.getPercentDiscount());
        if(product.getCost()!=null) baseProduct.setCost(product.getCost());
        productRepo.save(baseProduct);
    }

    @Override
    public List<ProductAdminDTO> findAll() {
        List<Product> list = productRepo.findAll();
        List<ProductAdminDTO> productAdminDTOS = new ArrayList<>();
        return productAdminDTOS;
    }

    @Override
    public List<Product> findProductByName(int id, String keyWord, long currentPage) {
        return productRepo.searchByNameAndPage(id,keyWord,(currentPage-1)*pageSize,pageSize);
    }

    @Override
    public long getTotalPageByName(int id, String keyWord) {
        return (productRepo.countByKeyWord(id,keyWord).get(0) % pageSize == 0) ?
                productRepo.countByKeyWord(id, keyWord).get(0) / pageSize
                : (productRepo.countByKeyWord(id, keyWord).get(0) / pageSize) + 1;
    }

    @Override
    public void saveAfterOrder(Product product, OrderDetail orderDetail) {
        productRepo.save(product);
    }


}
