package com.university.eventmanagement.service;

import com.university.eventmanagement.model.User;
import com.university.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public String registerStudent(String name ,String email ,String password , String collegeId){
    
        if (userRepository.existsByEmail(email)){
            return "Email already registered";
        }
        if (!isValidCollegeId(collegeId)){
            return "College ID must be exactly 9 digits and start with '2'.";
        }
            if (userRepository.existsByCollegeId(collegeId)){
            return "College ID already registered.";
        }

        User student = new User();
        student.setName(name);
        student.setEmail(email);
        student.setPassword(password);
        student.setCollegeId(collegeId);

        userRepository.save(student);
        return "SUCESS";
    }

    public String registerAdmin(String name ,String email ,String password) {

        if (!name.toLowerCase().startsWith("admin")){
            return "Admin username must start with 'admin'.";
        }

        if (userRepository.existsByEmail(email)) {
            return "Email already registered.";
        }

        User admin = new User();
        admin.setName(name);
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setRole(User.Role.ADMIN);

        userRepository.save(admin);
        return "SUCCESS";

    }

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email).get();
    
        if (user == null) {
        throw new RuntimeException("Email not found");
        }
    
        if (user.getPassword().equals(password)) {
        return user;
     }
    
        throw new RuntimeException("Wrong password");
    }
        private boolean isValidCollegeId(String collegeId) {
        if (collegeId == null) return false;
        return collegeId.matches("^2\\d{8}$");
    }
    public User findById(Long id) {
    return userRepository.findById(id).orElse(null);
}
}
