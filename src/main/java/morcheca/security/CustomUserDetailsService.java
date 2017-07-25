package morcheca.security;

import morcheca.entities.User;
import morcheca.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("CustomUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService service;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = service.getByName(name);
        if(user == null) throw new UsernameNotFoundException("User " + name + " not found");
        return user;
    }

}
