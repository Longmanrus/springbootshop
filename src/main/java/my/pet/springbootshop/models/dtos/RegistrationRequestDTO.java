package my.pet.springbootshop.models.dtos;

import lombok.Data;

@Data
public class RegistrationRequestDTO {
    private String username;
    private String email;
    private String password;
    private String matchedPassword;

}
