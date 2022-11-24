package gogitek.restaurentorder.entity;

import gogitek.restaurentorder.constaint.Status;
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
public class PreOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    private PreOrder preOrder;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Product product;

    private Status status;

}
