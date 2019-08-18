package ke.paystep.mpesaservicefull.security;

import ke.paystep.mpesaservicefull.exception.ResourceNotFoundException;
import ke.paystep.mpesaservicefull.model.User;
import ke.paystep.mpesaservicefull.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by Austin Oyugi on 17/8/2019.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameOrEmailAddress(usernameOrEmail, usernameOrEmail)
                .orElseThrow(()->
                        new UsernameNotFoundException("User not found with username or email :" + usernameOrEmail));

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("User","id",id));

        return UserPrincipal.create(user);
    }
}
