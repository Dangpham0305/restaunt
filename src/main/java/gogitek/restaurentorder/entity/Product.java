package gogitek.restaurentorder.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "sale_price")
    private Double salePrice;

    @Column(name = "percent_discount")
    private Double percentDiscount;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "image")
    private String image;


    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "cate_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "cost")
    private Double cost;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Set<PreOrderDetail> preOrderDetails = new HashSet<>();

    @Column(name = "is_delete")
    private Boolean delete;
}
