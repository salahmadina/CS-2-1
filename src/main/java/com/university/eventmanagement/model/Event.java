package com.university.eventmanagement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.university.eventmanagement.model.Booking;
import com.university.eventmanagement.model.Rating;

@Entity
@Table(name = " events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length= 200)
    private String name;

    @Column(nullable = false ,columnDefinition= "TEXT")
    private String description;

    @Column(name="event_date",nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false , precision = 10 , scale =2)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name ="total_seats")
    private Integer totalSeats;

    @Column(name ="booked_seats", nullable = false)
    private Integer bookedSeats = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.UPCOMING;

    @Column(nullable = false)
    private Boolean cancelled = false;

    @Column(length = 255)
    private String location;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy ="event",cascade =CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy="event",cascade = CascadeType.ALL)
    private List<Rating> Ratings;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        if(this.bookedSeats == null) this.bookedSeats = 0;
        if(this.price ==null) this.price= BigDecimal.ZERO;
        if(this.cancelled == null) this.cancelled = false;
    }
    
    public boolean isFree(){
            return this.price == null ||this.price.compareTo(BigDecimal.ZERO) ==0;
        }
    public boolean isUnlimited(){
        return this.totalSeats == null;
    }
    public int getRemainingSeats(){
        if (isUnlimited()) return -1;
        return this.totalSeats - this.bookedSeats;
    }
    public boolean hasAvaliableSeats(){
        if(isUnlimited()) return true;
        return getRemainingSeats() > 0;
    }

    public enum Status {
        UPCOMING, PAST , POSTPONED ;
    }

    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public LocalDateTime getEventDate(){
        return eventDate;
    }
    public BigDecimal getPrice(){
        return price;
    }
    public Integer getTotalSeats(){
        return totalSeats;
    }
    public Integer getBookedSeats(){
        return bookedSeats;
    }
    public Status getStatus(){
        return status;
    }
    public Boolean getCancelled(){
        return cancelled;
    }
     public boolean isCancelled(){
        return Boolean.TRUE.equals(cancelled);
    }
    public String getLocation() {
        return location;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public List<Booking> getBookings() {
        return bookings;
    }
    public List<Rating> getRatings (){
        return Ratings;
    }


    public void setId(Long id){
        this.id = id;
    }
     public void setname(String name){
        this.name = name;
    }
     public void setDescription(String description){
         this.description = description;
    }
    public void setEventDate(LocalDateTime eventDate){
        this.eventDate = eventDate;
    }
    public void setPrice (BigDecimal price){
        this.price=price;
    }
    public void setTotalSeats(Integer totalSeats){
        this.totalSeats=totalSeats;
    }
    public void setBookedSeats(Integer bookedSeats){
        this.bookedSeats=bookedSeats;
    }
    public void setStatus(Status status){
        this.status=status;
    }
    public void setCancelled(Boolean cancelled){
        this.cancelled=cancelled;
    }
    public void setLocation( String location){
        this.location=location;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt=createdAt;
    }
    public void setBookings(List<Booking> bookings){
        this.bookings = bookings;
    }
    public void setRatings(List<Rating> ratings){
        this.Ratings= ratings;
    }
}
