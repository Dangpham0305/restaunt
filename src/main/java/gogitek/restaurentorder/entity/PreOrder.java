package gogitek.restaurentorder.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class PreOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "employee", referencedColumnName = "id")
    private User employee;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "customer", referencedColumnName = "id")
    private User customer;

}
