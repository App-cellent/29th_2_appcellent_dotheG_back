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

    private boolean isSocial;

    private String role;

    private boolean Noti;

    private boolean tutorial;

    @Builder
    public Member(String userName, String userLogin ,String userPassword, boolean available, boolean isSocial, String role, boolean Noti, boolean tutorial) {
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.available = available;
        this.isSocial = isSocial;
        this.role = role;
        this.Noti = Noti;
        this.tutorial = tutorial;
    }

    public void changeName(String newNickname){
        this.userName = newNickname;
    }

    public void changePassword(String newPassword){
        this.userPassword = newPassword;
    }

    public void toggleNoti(){
        this.Noti = !this.Noti;
    }

    public void changeTutorial(){
        this.tutorial = true;
    }
}