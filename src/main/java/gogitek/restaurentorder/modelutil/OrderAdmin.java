package gogitek.restaurentorder.modelutil;
import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.entity.Orders;
import lombok.Data;

import java.util.Date;

@Data
public class OrderAdmin {
    private Integer order_id;
    private String name;
    private Float totalPrice;
    private Status status;
    private Integer totalProduct;
    private String address;
    private String phoneNumber;
    private Float salePrice;
    private Date createAt;

}
