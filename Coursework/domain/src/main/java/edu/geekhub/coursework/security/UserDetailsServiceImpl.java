package edu.geekhub.coursework.security;

import edu.geekhub.coursework.users.User;
import edu.geekhub.coursework.users.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User doesn't exist");
        }
        if (passwordEncoder.matches("Temporary1", user.getPassword())) {
            throw new UsernameNotFoundException("User doesn't exist");
        }

        return new SecurityUser(user);
    }
}
