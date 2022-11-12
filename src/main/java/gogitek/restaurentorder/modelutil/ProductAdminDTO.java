package gogitek.restaurentorder.modelutil;

import gogitek.restaurentorder.entity.Product;
import lombok.Data;

@Data
public class ProductAdminDTO {
    private Integer id;
    private String name;
    private Float salePrice;
    private Integer quantityProd;
    private Float cost;
    private String cate_name;

}
