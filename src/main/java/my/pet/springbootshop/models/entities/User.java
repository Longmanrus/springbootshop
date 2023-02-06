package my.pet.springbootshop.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import my.pet.springbootshop.models.enums.Role;
import my.pet.springbootshop.models.enums.UserStatus;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column (name = "username", unique = true)
    private String username;
    @Column (name = "password")
    private String password;
    @Column (name = "email", unique = true)
    private String email;
    @Enumerated(value = EnumType.STRING)
    private Role role = Role.USER;
    @Column (name = "rating")
    private int rating = 0;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @ToString.Exclude
    private Set<Cart> carts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @ToString.Exclude
    private Set<Order> orders = new HashSet<>();
    @JsonManagedReference
    public Set<Order> getOrders() {
        return orders;
    }
    @JsonManagedReference
    public Set<Cart> getCarts() {
        return carts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && rating == user.rating && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && role == user.role && userStatus == user.userStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, role, rating, userStatus);
    }

}
