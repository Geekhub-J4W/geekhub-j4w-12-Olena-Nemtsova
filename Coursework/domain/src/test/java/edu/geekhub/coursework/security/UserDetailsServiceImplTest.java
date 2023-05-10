package edu.geekhub.coursework.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import edu.geekhub.coursework.users.Role;
import edu.geekhub.coursework.users.User;
import edu.geekhub.coursework.users.interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void can_get_security_user_by_username() {
        User user = new User(1, "Mark", "Pearce", "Qwerty1", "some@gmail.com", Role.USER);
        UserDetails expectedUserDetails = new SecurityUser(user);
        doReturn(user).when(userRepository).getUserByEmail(any());

        UserDetails userDetails = userDetailsService.loadUserByUsername("some@gmail.com");

        assertEquals(expectedUserDetails, userDetails);
    }

    @Test
    void can_throws_UsernameNotFoundException_by_wrong_username() {
        doReturn(null).when(userRepository).getUserByEmail(any());

        assertThrows(
            UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername("some@gmail.com")
        );
    }
}
