package com.university.eventmanagement.repository;

import com.university.eventmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface  UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByCollegeId(String collegeId);

    boolean existsByEmail(String email);
     
    boolean existsByCollegeId(String collegeId);
    
}
