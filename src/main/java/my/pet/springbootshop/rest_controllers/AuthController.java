package my.pet.springbootshop.rest_controllers;

import my.pet.springbootshop.models.dtos.AuthRequestDTO;
import my.pet.springbootshop.models.dtos.RegistrationRequestDTO;
import my.pet.springbootshop.models.entities.User;
import my.pet.springbootshop.models.repositories.UsersRepository;
import my.pet.springbootshop.security.JWTProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/*
    В основном классе Application есть метод забивающий бд тестовыми данными
    Аутентификация происходит посредством получения JWT токена и помещении его в header Authorization для каждого запроса.
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;
    private final JWTProvider jwtProvider;

    public AuthController(AuthenticationManager authenticationManager, UsersRepository usersRepository, JWTProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.jwtProvider = jwtProvider;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate (@RequestBody AuthRequestDTO request){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            User user = usersRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
            String token = jwtProvider.createToken(request.getUsername(), user.getRole().name());
            Map<Object, Object> response = new HashMap<>();
            response.put("username", request.getUsername());
            response.put("token", token);

            return ResponseEntity.ok(response);
        }catch (AuthenticationException e){
            return new ResponseEntity<>("Invalid username/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout (HttpServletRequest request, HttpServletResponse response){
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request,response, null);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration (@RequestBody RegistrationRequestDTO requestDTO){


        if (usersRepository.findByUsername(requestDTO.getUsername()).isEmpty() ||
            usersRepository.findByEmail(requestDTO.getEmail()).isEmpty()){

            User user = new User();
            user.setUsername(requestDTO.getUsername());
            user.setEmail(requestDTO.getEmail());
            user.setPassword(BCrypt.hashpw(requestDTO.getPassword(),BCrypt.gensalt()));
            usersRepository.save(user);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Username or Email already exist",HttpStatus.BAD_REQUEST);
        }

    }

}
