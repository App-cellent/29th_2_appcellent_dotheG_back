package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    private String userName;

    private String userLogin;

    private String userPassword;

    private boolean available;

    @Column(columnDefinition = "boolean default false")
    private boolean isSocial;

    private String role;

    @Builder
    public Member(String userName,String userLogin ,String userPassword, boolean available, boolean isSocial, String role) {
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.available = available;
        this.isSocial = isSocial;
        this.role = role;
    }
}