package gogitek.restaurentorder.modelutil;

import gogitek.restaurentorder.entity.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    String image;
    Integer productId;
    String productName;
    Integer quantity;
    Float discount;
    Float salePrice;
    Float totalPrice;

}
