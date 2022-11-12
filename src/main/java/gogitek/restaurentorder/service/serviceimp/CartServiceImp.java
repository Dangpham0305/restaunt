package gogitek.restaurentorder.service.serviceimp;

import gogitek.restaurentorder.entity.*;
import gogitek.restaurentorder.repository.CartRepo;
import gogitek.restaurentorder.service.CartService;
import gogitek.restaurentorder.service.UserService;
import gogitek.restaurentorder.entity.OrderDetail;
import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.entity.Product;
import gogitek.restaurentorder.entity.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServiceImp implements CartService {
    private final CartRepo cartRepo;
    private final UserService userService;

    public CartServiceImp(CartRepo cartRepo, UserService userService) {
        this.cartRepo = cartRepo;
        this.userService = userService;
    }

    @Override
    public boolean saveItemToCart(Product product, Integer quantity) {
        User user = userService.getCurrentUser();
        return true;
    }

    @Override
    public List<PreOrder> getAllCartByUser() {
        return null;
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
            cartList.get(i).setQuantity(soluong.get(i));
            cartRepo.save(cartList.get(i));
        }
    }

    @Override
    public boolean deleteAnItemInCart(int productId) {
        List<PreOrder> cartList = getAllCartByUser();
        cartList.forEach(cart -> {
            if (cart.getProduct().getId().equals(productId)) {
                cartRepo.save(cart);
            }
        });
        return true;
    }

    @Override
    public void saveItemToCartByOrder(Orders orders) {
    }
}
