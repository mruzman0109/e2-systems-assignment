package hr.e2systems.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.Instant;


@Entity
@Table(name = "metar")
@Data
@Builder
public class Metar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 4, nullable = false)
    private String icaoCode;


    @Lob
    @Column(nullable = false)
    private String rawText;

    private Instant observedAt;


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant receivedAt;
}
