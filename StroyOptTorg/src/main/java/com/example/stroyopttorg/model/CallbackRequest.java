package com.example.stroyopttorg.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Обратный звонок / форма "Контакты" */
@Entity
@Table(name = "callback_requests")
@Getter
@Setter
@NoArgsConstructor
public class CallbackRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;

    @Column(columnDefinition = "text")
    private String message;

    private boolean processed;
    private Instant createdAt = Instant.now();
}
