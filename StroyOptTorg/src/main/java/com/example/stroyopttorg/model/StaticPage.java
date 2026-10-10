package com.example.stroyopttorg.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** О компании, Доставка, Оплата, Возврат, Контакты, Политика конфиденциальности ... */
@Entity
@Table(name = "static_pages")
@Getter
@Setter
@NoArgsConstructor
public class StaticPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String slug;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String content;
}
