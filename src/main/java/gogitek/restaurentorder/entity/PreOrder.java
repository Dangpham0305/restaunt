package gogitek.restaurentorder.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

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

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "preOrder")
    private Set<PreOrderDetail> preOrderDetails;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "employee", referencedColumnName = "id")
    private User employee;

    private Long orderId;

    private Boolean delete;
}
