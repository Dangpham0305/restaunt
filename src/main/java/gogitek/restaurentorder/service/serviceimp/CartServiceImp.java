package gogitek.restaurentorder.service.serviceimp;

import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.entity.*;
import gogitek.restaurentorder.repository.CartRepo;
import gogitek.restaurentorder.repository.PreOrderDetailRepo;
import gogitek.restaurentorder.service.CartService;
import gogitek.restaurentorder.service.UserService;
import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements CartService {
    private final CartRepo cartRepo;
    private final UserService userService;
    private final PreOrderDetailRepo preOrderDetailRepo;


    @Override
    public boolean saveItemToCart(Product product, Long preorderId, Integer quantity) {
        PreOrder preOrder = cartRepo.getById(preorderId);
        PreOrderDetail detail = new PreOrderDetail();
        detail.setPreOrder(preOrder);
        detail.setProduct(product);
        detail.setQuantity(quantity);
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
    public boolean deleteAllItemInCart(Long id) {
        try {
            List<PreOrderDetail> list = new ArrayList<>(cartRepo.findById(id).orElseThrow(RuntimeException::new).getPreOrderDetails());
            List<PreOrderDetail> listfilter = list.stream().filter(item -> Status.APPROVED.equals(item.getStatus())).collect(Collectors.toList());
            preOrderDetailRepo.deleteAllInBatch(listfilter);
            return true;
        }catch (Exception e){
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void saveNewQuantity(List<PreOrder> cartList, List<Integer> soluong) {
        //            cartList.get(i).setQuantity(soluong.get(i));
        cartRepo.saveAll(cartList);
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

    @Override
    public boolean orderAllItem(Long id) {
        try{
            List<PreOrderDetail> list = new ArrayList<>(cartRepo.getById(id).getPreOrderDetails());
            List<PreOrderDetail> filter = list.stream().filter(preOrderDetail -> Status.APPROVED.equals(preOrderDetail.getStatus())).peek(preOrderDetail -> preOrderDetail.setStatus(Status.PROCESSING)).collect(Collectors.toList());
            preOrderDetailRepo.saveAll(filter);
            return true;
        }catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    @Override
    public List<PreOrderDetail> getAllOrder(){
        return preOrderDetailRepo.findByStatusIn(Arrays.asList(Status.PROCESSING, Status.DELIVERED));
    }

    @Override
    public void changeStatus(Long detailId, Status status) {
        Optional<PreOrderDetail> detail = preOrderDetailRepo.findById(detailId);
        if (detail.isPresent()){
            PreOrderDetail orderDetail = detail.get();
            orderDetail.setStatus(status);
            preOrderDetailRepo.save(orderDetail);
        }
    }

    @Override
    public boolean checkOrderDelivered(PreOrder preOrder) {
        List<Status> acceptedStatus = Arrays.asList(Status.PROCESSING, Status.APPROVED);
        return preOrder.getPreOrderDetails().stream().anyMatch(preOrderDetail -> acceptedStatus.contains(preOrderDetail.getStatus()));
    }

}
