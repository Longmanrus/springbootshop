package my.pet.springbootshop.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import my.pet.springbootshop.models.enums.OrderStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(name = "create_date", nullable = false)
    @CreationTimestamp
    private LocalDate createDate;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private OrderStatus status=OrderStatus.ADDED;

    @OneToMany(mappedBy = "order")
    private Set<OrderProduct> orderPositions = new HashSet<>();
    @JsonBackReference
    public User getUser() {
        return user;
    }
    @JsonManagedReference
    public Set<OrderProduct> getOrderPositions() {
        return orderPositions;
    }

    @Transient
    private double totalPrice = 0d;

}
