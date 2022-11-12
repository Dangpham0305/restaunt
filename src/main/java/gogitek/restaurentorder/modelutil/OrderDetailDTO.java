package gogitek.restaurentorder.modelutil;

import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.entity.OrderDetail;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class OrderDetailDTO {
    private String name;
    private int order_id;
    private Date date;
    private Integer quantity;
    private Status status;

}
