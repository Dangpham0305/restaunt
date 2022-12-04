package gogitek.restaurentorder.entity;

import gogitek.restaurentorder.constaint.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    @Enumerated
    private Status status;

    @Column(name = "created_at")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @CreatedDate
    private Date createAt;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "note")
    private String note;

    @Column(name = "discount")
    private Double discount;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "orders")
    private Set<OrderDetail> orderDetails;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
