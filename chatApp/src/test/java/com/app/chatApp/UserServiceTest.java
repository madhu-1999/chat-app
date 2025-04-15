package com.app.chatApp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.chatApp.model.UserResponse;
import com.app.chatApp.model.User;
import com.app.chatApp.repository.UserRepository;
import com.app.chatApp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterUsernameIsNull() {
        User user = User.builder()
                .username(null)
                .password("testUser")
                .build();

        UserResponse response = userService.registerAccount(user);

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(response.getErrMsg(), "Username is blank or already exists!");
    }

    @Test
    public void testRegisterUsernameIsEmpty() {
        User user = User.builder()
                .username("")
                .password("testUser")
                .build();

        UserResponse response = userService.registerAccount(user);

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(response.getErrMsg(), "Username is blank or already exists!");
    }

    @Test
    public void testRegisterUsernameIsBlank() {
        User user = User.builder()
                .username("  ")
                .password("testUser")
                .build();

        UserResponse response = userService.registerAccount(user);

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(response.getErrMsg(), "Username is blank or already exists!");
    }

    @Test
    public void testRegisterUsernameAlreadyExists() {
        User user = User.builder()
                .username("testUser")
                .password("testUser")
                .build();

        Mockito.when(userRepository.existsByUsername("testUser"))
                .thenReturn(true);
        UserResponse response = userService.registerAccount(user);

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(response.getErrMsg(), "Username is blank or already exists!");
    }

    @Test
    public void testRegisterPasswordIsBlank() {
        User user = User.builder()
                .username("testUser")
                .password("         ")
                .build();

        Mockito.when(userRepository.existsByUsername("testUser"))
                .thenReturn(false);
        UserResponse response = userService.registerAccount(user);

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(response.getErrMsg(), "Password is either blank or less than 8 characters!");
    }

    @Test
    public void testRegisterPasswordIsLessThan8Characters() {
        User user = User.builder()
                .username("testUser")
                .password("adwdio")
                .build();

        Mockito.when(userRepository.existsByUsername("testUser"))
                .thenReturn(false);
        UserResponse response = userService.registerAccount(user);

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(response.getErrMsg(), "Password is either blank or less than 8 characters!");
    }

    @Test
    public void testRegisterEmailIsBlank() {
        User user = User.builder()
                .username("testUser")
                .password("testUser")
                .email("  ")
                .build();

        Mockito.when(userRepository.existsByUsername("testUser"))
                .thenReturn(false);
        UserResponse response = userService.registerAccount(user);

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(response.getErrMsg(), "Email is either blank or invalid!");
    }

    @Test
    public void testRegisterEmailIsInvalid() {
        User user = User.builder()
                .username("testUser")
                .password("testUser")
                .email("adw@xx")
                .build();

        Mockito.when(userRepository.existsByUsername("testUser"))
                .thenReturn(false);
        UserResponse response = userService.registerAccount(user);

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(response.getErrMsg(), "Email is either blank or invalid!");
    }

    @Test
    public void testRegisterRegistrationSuccessful() {
        User user = User.builder()
                .username("testUser")
                .password("testUser")
                .email("testUser@gmail.com")
                .build();

        com.app.chatApp.entity.User userEntity = new com.app.chatApp.entity.User(user.getUsername(),
                user.getEmail(), passwordEncoder.encode(user.getPassword()));

        Mockito.when(userRepository.existsByUsername("testUser"))
                .thenReturn(false);
        Mockito.when(passwordEncoder.encode("testUser"))
                .thenReturn("testUser");
        Mockito.when(userRepository.save(Mockito.any(com.app.chatApp.entity.User.class)))
                .thenReturn(userEntity);

        UserResponse response = userService.registerAccount(user);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getErrMsg(), "Registration successful!");
    }

    @Test
    public void shouldSerialize_WithoutIgnored() throws IOException {
        User person = new User();
        person.setEmail("l.lawliet@gmail.com");
        person.setUsername("L");
        person.setPassword("wdqwdqwdwq");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(person);

        System.out.println(json);
    }
}
