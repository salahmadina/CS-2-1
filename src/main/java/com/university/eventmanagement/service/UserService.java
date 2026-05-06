package com.university.eventmanagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.eventmanagement.model.User;
import com.university.eventmanagement.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerStudent(String name, String email, String password, String collegeId) {

        if (userRepository.existsByEmail(email)) {
            return "Email already registered.";
        }

        if (!isValidCollegeId(collegeId)) {
            return "College ID must be exactly 9 digits and start with '2'.";
        }

        if (userRepository.existsByCollegeId(collegeId)) {
            return "College ID already registered.";
        }

        User student = new User();
        student.setName(name);
        student.setEmail(email);
        student.setPassword(password);
        student.setCollegeId(collegeId);
        student.setRole(User.Role.STUDENT);

        userRepository.save(student);
        return "SUCCESS";
    }

    public String registerAdmin(String name, String email, String password) {

        if (!name.toLowerCase().startsWith("admin")) {
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
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt.get();
        }
        return null;
    }

    private boolean isValidCollegeId(String collegeId) {
        if (collegeId == null) return false;
        return collegeId.matches("^2\\d{8}$");
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
