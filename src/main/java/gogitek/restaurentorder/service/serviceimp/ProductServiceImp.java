package gogitek.restaurentorder.service.serviceimp;

import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.entity.*;
import gogitek.restaurentorder.modelutil.CartItem;
import gogitek.restaurentorder.repository.CartRepo;
import gogitek.restaurentorder.repository.ProductRepo;
import gogitek.restaurentorder.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    private final ProductRepo productRepo;
    private final CartRepo cartRepo;


    @Override
    public Product getProductById(Long id) {
        return productRepo.getProductsByIdAndDelete(id, false);
    }


    @Override
    public List<Product> findAllProduct() {
        return productRepo.getAllByDelete(false);
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
    public boolean addProduct(Product product) {
        product.setDelete(false);
        productRepo.save(product);
        return true;
    }

    @Override
    public boolean deleteProduct(Long id) {
        Optional<Product> product = productRepo.findById(id);
        if (product.isPresent()){
            Product p = product.get();
            p.setDelete(true);
            productRepo.save(p);
            return true;
        }
        return false;
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
    public boolean checkProductInUse(Long productId) {
        List<PreOrder> ordersList = cartRepo.findAllByDelete(false);
        AtomicBoolean check = new AtomicBoolean(false);
        ordersList.stream().forEach(preOrder -> {
            Set<PreOrderDetail > details = preOrder.getPreOrderDetails();
            details.stream().forEach(detail -> {
                if (detail.getProduct().getId().equals(productId)) {
                    check.set(true);
                }
            });
        });
        return check.get();
    }


}
