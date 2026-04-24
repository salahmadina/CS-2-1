package com.university.eventmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name =" bookings",
    uniqueConstraints ={
        @UniqueConstraint(columnNames ={"user_id","event_id"})
        }
)

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id",nullable= false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status",nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.FREE;

    @Enumerated(EnumType.STRING)
    @Column(name="refund_status",nullable = false)
    private RefundStatus refundStatus = RefundStatus.NONE;

    @Column(name="booked_at")
    private LocalDateTime bookedAt;

    @PrePersist
    public void prePersist(){
        this.bookedAt = LocalDateTime.now();
    }

    public enum PaymentStatus{
        FREE,PENDING,PAID
    }
    public enum RefundStatus{
        NONE,IN_PROCESS,PAID;
    }

    public void setId (Long id){
        this.id = id;
    }
    public void setUser(User user){
        this.user = user;
    }
    public void setEvent(Event event){
        this.event = event;
    }
    public void setPaymentStatus(PaymentStatus paymentstatus){
        this.paymentStatus = paymentStatus;
    }
    public void SetRefundStatus(RefundStatus refundStatus){
        this.refundStatus = refundStatus;
    }
    public void setBookedAt(LocalDateTime bookedAt){
        this.bookedAt = bookedAt;
    }
    

    public Long getId(){
        return id;
    }
    public User getUser (){
        return user;
    }
    public Event getEvent(){
        return event;
    }
    public PaymentStatus getPaymentStatus(){
        return paymentStatus;
    }
    public RefundStatus getRefundStatus (){
        return refundStatus;
    }
    public LocalDateTime getBookedAt(){
        return bookedAt;
    }
}
