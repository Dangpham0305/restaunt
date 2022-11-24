package gogitek.restaurentorder.service.serviceimp;

import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.entity.*;
import gogitek.restaurentorder.repository.CartRepo;
import gogitek.restaurentorder.repository.PreOrderDetailRepo;
import gogitek.restaurentorder.service.CartService;
import gogitek.restaurentorder.service.UserService;
import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.entity.Product;
import gogitek.restaurentorder.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements CartService {
    private final CartRepo cartRepo;
    private final UserService userService;
    private final PreOrderDetailRepo preOrderDetailRepo;


    @Override
    public boolean saveItemToCart(Product product, Long preorderId) {
        PreOrder preOrder = cartRepo.getById(preorderId);
        PreOrderDetail detail = new PreOrderDetail();
        detail.setPreOrder(preOrder);
        detail.setProduct(product);
        detail.setQuantity(1);
        detail.setStatus(Status.APPROVED);
        preOrderDetailRepo.save(detail);
        return true;
    }

    @Override
    public List<PreOrder> getAllCartByUser() {
        return cartRepo.findAll();
    }

    @Override
    public Integer countNumberOfItemInCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) return 0;
        return 0;
    }

    @Override
    public boolean deleteAllItemInCart() {
        List<PreOrder> cartList = getAllCartByUser();
        for (PreOrder cart : cartList
        ) {
            cartRepo.save(cart);
        }
        return true;
    }

    @Override
    public void saveNewQuantity(List<PreOrder> cartList, List<Integer> soluong) {
        for (int i = 0; i < cartList.size(); i++) {
//            cartList.get(i).setQuantity(soluong.get(i));
            cartRepo.save(cartList.get(i));
        }
    }

    @Override
    public boolean deleteAnItemInCart(int productId) {
        return true;
    }

    @Override
    public void saveItemToCartByOrder(Orders orders) {
    }

    @Override
    public PreOrder addNewCart(PreOrder preOrder) {
        return cartRepo.save(preOrder);
    }

    @Override
    public PreOrder findById(Long iod) {
        return cartRepo.findById(iod).orElse(new PreOrder());
    }

}
