package hr.e2systems.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.Instant;


@Entity
@Table(name = "subscriptions")
@Data
public class Subscription {
    @Id
    @Column(length = 4, nullable = false, unique = true)
    private String icaoCode; // uppercase 4 letters


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}