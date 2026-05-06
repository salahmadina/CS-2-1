package com.university.eventmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "ratings",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "event_id"})
    }
)
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private Integer stars;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "rated_at")
    private LocalDateTime ratedAt;

    @PrePersist
    public void prePersist() {
        this.ratedAt = LocalDateTime.now();
    }

    // ── Getters ──
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Event getEvent() { return event; }
    public Integer getStars() { return stars; }
    public String getComment() { return comment; }
    public LocalDateTime getRatedAt() { return ratedAt; }

    // ── Setters ──
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setEvent(Event event) { this.event = event; }
    public void setStars(Integer stars) { this.stars = stars; }
    public void setComment(String comment) { this.comment = comment; }
    public void setRatedAt(LocalDateTime ratedAt) { this.ratedAt = ratedAt; }
}
