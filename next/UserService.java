package com.example.hardlab5.next;


//
//import com.example.hardlab5.model.User;
//import com.example.hardlab5.repo.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // Сохранение нового пользователя
//    public User saveUser(User user) {
//        return userRepository.save(user);
//    }
//
//    // Поиск пользователя по имени
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//}



import com.example.hardlab5.model.User;
import com.example.hardlab5.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Сохранение нового пользователя
    public User saveUser(User user) {
        // Кодируем пароль перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Поиск пользователя по имени
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
