//package com.example.hardlab5.next;
//
//
////
////import com.example.hardlab5.model.User;
////import com.example.hardlab5.repo.UserRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////
////
////@Service
////public class UserService {
////
////    @Autowired
////    private UserRepository userRepository;
////
////    // Сохранение нового пользователя
////    public User saveUser(User user) {
////        return userRepository.save(user);
////    }
////
////    // Поиск пользователя по имени
////    public User findByUsername(String username) {
////        return userRepository.findByUsername(username);
////    }
////}
//
//
//
//import com.example.hardlab5.model.User;
//import com.example.hardlab5.repo.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
////
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//
//
//
//    public void saveUser(User user) {
//        if (userRepository.findByEmail(user.getEmail()) != null) {
//            throw new IllegalArgumentException("Email is already in use!");
//        }
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }
//
//
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//    public User findByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
//
//
//
//}
//
//



package com.example.hardlab5.next;

import com.example.hardlab5.model.User;
import com.example.hardlab5.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        // Проверяем, если пароль уже зашифрован, не перезаписываем его
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) { // например, проверка для BCrypt
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }




    // Найти пользователя по имени
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Найти пользователя по email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Найти пользователя по ID
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    // Удалить пользователя по ID
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    // Получить список всех пользователей
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }



}

