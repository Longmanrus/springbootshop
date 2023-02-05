package my.pet.springbootshop.models.dtos;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String username;
    private String password;
}
