package com.orfarmweb.modelutil;

import com.orfarmweb.entity.OrderDetail;
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
    public ProductFilterDTO(OrderDetail orderDetail, int quantity){
        this.id = orderDetail.getProduct().getId();
        this.image = orderDetail.getProduct().getImage();
        this.name = orderDetail.getProduct().getName();
        this.cateName = orderDetail.getProduct().getCategory().getName();
        this.quantity = quantity;
        this.totalPrice = orderDetail.getProduct().getSalePrice()* (100-orderDetail.getProduct().getPercentDiscount())/100 * quantity;
        this.importPrice = orderDetail.getProduct().getCost()*quantity;
    }
}
