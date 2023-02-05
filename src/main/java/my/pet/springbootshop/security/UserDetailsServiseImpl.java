package my.pet.springbootshop.security;

import my.pet.springbootshop.models.entities.User;
import my.pet.springbootshop.models.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiseImpl implements UserDetailsService {

    private final UsersRepository usersRepository;
    @Autowired
    public UserDetailsServiseImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't not exist"));
        return SecurityUser.fromUser(user);
    }
}


