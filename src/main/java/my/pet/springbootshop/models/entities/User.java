package my.pet.springbootshop.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.pet.springbootshop.models.enums.Role;
import my.pet.springbootshop.models.enums.UserStatus;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", rating=" + rating +
                ", userStatus=" + userStatus +
                '}';
    }
}
