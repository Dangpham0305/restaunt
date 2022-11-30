package gogitek.restaurentorder.modelutil;

import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.entity.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    String image;
    Long productId;
    String productName;
    Integer quantity;
    Double salePrice;
    Double totalPrice;
    Status status;
}
