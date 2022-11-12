package gogitek.restaurentorder.modelutil;

import gogitek.restaurentorder.entity.OrderDetail;
import lombok.*;

@Data
public class ProductFilterDTO {
    private int id;
    private String image;
    private String name;
    private String cateName;
    private int quantity;
    private float totalPrice;
    private float importPrice;
}
