package com.university.eventmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.university.eventmanagement.model.Booking;
import com.university.eventmanagement.model.Ratings;

@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 100)
    private String name;

    @Column(nullable = false , unique = true, length =150)
    private String email;

    @Column(nullable = false , length = 100)
    private String password;

    @Column(name="college_id",length = 20)
    private String collegeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking>bookings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List <Rating> ratings;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
    public enum Role {
        STUDENT,ADMIN;
    }

    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getCollegeId(){
        return collegeId;
    }
    public Role getRole(){
        return role;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public List<Booking> getBookings(){
        return bookings;
    }
    public List <Rating> getRatings(){
        return ratings;
    }


    public void setId(Long id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPassword(String password){
        this.password= password;
    }
    public void setCollegeId(String collegeId){
        this.collegeId = collegeId;
    }
    public void setRole(Role role){
        this.role = role;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
    public void setBookings(List<Booking> bookings){
        this.bookings = bookings;
    }
    public void setRatings(List<Ratings> ratings){
        this.ratings = ratings;
    }

    
}
