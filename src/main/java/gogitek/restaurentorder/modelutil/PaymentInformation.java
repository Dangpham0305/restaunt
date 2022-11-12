package gogitek.restaurentorder.modelutil;

import gogitek.restaurentorder.entity.Orders;
import gogitek.restaurentorder.entity.Product;
import gogitek.restaurentorder.entity.User;
import lombok.Data;

@Data
public class PaymentInformation {
     User user;
     Product product;
     Orders order;
}
