package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.Size;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Size(min = 2, max = 10)
    private String userName;

    private String userLogin;

    private String userPassword;

    private boolean available;

    @Column(columnDefinition = "boolean default false")
    private boolean isSocial;

    private String role;

    private String email;

    @Builder
    public Member(String userName,String userLogin ,String userPassword, boolean available, boolean isSocial, String role, String email) {
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.available = available;
        this.isSocial = isSocial;
        this.role = role;
        this.email = email;
    }

    public void updateUserInfo(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }
}