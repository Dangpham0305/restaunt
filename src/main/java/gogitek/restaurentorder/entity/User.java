package gogitek.restaurentorder.entity;

import com.sun.istack.NotNull;
import gogitek.restaurentorder.constaint.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true, name = "email")
    @NotNull
    private String email;

    @NotNull
    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "role")
    @Enumerated
    private Role role;

    @OneToMany(targetEntity = Orders.class, mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Orders> orders;

    public String getName() {
        return lastName +" "+ firstName;
    }
}
