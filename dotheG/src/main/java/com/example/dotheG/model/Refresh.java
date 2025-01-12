package com.example.dotheG.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userLogin;
    private String refresh;
    private String expiration;

    @Builder
    public Refresh(String userLogin, String refresh, String expiration) {
        this.userLogin = userLogin;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}